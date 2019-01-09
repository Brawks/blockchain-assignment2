//Student : Samuel Saint-Fleur
// ID : 300008314
// Course : CSI2110
// Assignment 2
// File: BlockChain

/**
 * This class is the main System of the Blockchain that operates transactions and blocks
 * through the ID of the user (session), the sender, receiver and amount of the currency. 
 * 
 *
 * @author Samuel Saint-Fleur
 * @version October 2018
 */

import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.stream.Collectors;
import java.util.Random;

public class BlockChain {

	private static int curIndex = -1;
	private static ArrayList<Block> chainBlock = new ArrayList<Block>();
	private int guessCounter;


	/**
	 * Constructor of the class BlockChain
	 * @param chain ArrayList that holds every Block within the BlockChain.
	 * @see Transaction
	 * @see Block
	 */
	public BlockChain(ArrayList<Block> chain){
		chainBlock = chain;
	}


	/**
	 * main method of class BlockChain.
	 * @param args[] String arguments passed to method
	 */
	public static void main(String args[]){
		//We will use a Scanner to define every user input of the class.
		Scanner scanner = new Scanner(System.in);
		String fileName = "";

		BlockChain blockChain = null;

		while(blockChain == null){
				System.out.println("From what file would you like to read (input e.g.: blockchain.txt)\n");
				fileName = scanner.next();
				blockChain = BlockChain.fromFile(fileName);
		}

		String minerID = "";
		System.out.println("Please enter your minder ID.");
		minerID = scanner.next();

		//The following steps process the Transaction of a validated BlockChain
		System.out.println("Would you like to make a transaction? Enter 'yes' to make a new transaction, or any other character to exit");
		String loopBack = "YES";
		loopBack = (String) scanner.next().toUpperCase();
		while(loopBack.equals("YES")) {
		if (blockChain.validateBlockchain()) {
			System.out.println("Blockhain "+fileName+" has been validated.\nPlease enter new transaction sender: ");
			String userS = scanner.next();
			while(userS.equals("bitcoin") && blockChain.getIndex() != -1){
				System.out.println("Blockchain has already been initiated by bitcoin. Can no longer send as bitcoin.");
				userS = scanner.next();
			}
			System.out.println("Please enter a receiver: ");
			String userR = scanner.next();
			while(userR.equals("bitcoin")){
				System.out.println("Cannot have 'bitcoin' as receiver. Please re-enter:");
				userR = scanner.next();
			}
			System.out.println("Please enter an amount: ");
			int amountS = Integer.parseInt(scanner.next());
			int userVal = blockChain.getBalance(userS);
			while(userVal-amountS < 0){
				System.out.println("sender "+userS+" does not have enough balance (User,Balance) : ("+userS+","+userVal+"). Cancelling transaction.");
				System.out.println("Please enter new transaction sender: ");
				userS = scanner.next();
				System.out.println("Please enter a receiver: ");
				userR = scanner.next();
				System.out.println("Please enter an amount: ");
				amountS = Integer.parseInt(scanner.next());
			}
			Transaction toAdd = new Transaction(userS,userR,amountS);
			blockChain.add(blockChain.createBlock(toAdd));
			System.out.println("New transaction was processed. For more information, " + blockChain.getNumberOfGuesses() + " guesses for nonce were admitted before a valid hash was obtained.");

		} else {
			System.out.println("This BlockChain is invalid. Possible temperement.");
		}
		System.out.println("Would you like to make a new transaction with the same BlockChain? Enter 'yes' to make a new transaction, or any other character to exit");
		loopBack = (String) scanner.next().toUpperCase();
	}
		String newFile = fileName.substring(0,fileName.length()-4);
		blockChain.toFile(newFile+"_"+minerID+".txt");
	}


	/**
	 * Static BlockChain that is obtained from an input file (.txt)
	 * @param fileName String corresponding to the file name of the input file (e.g: blockchain.txt)
	 * @return new BlockChain object 
	 */
	public static BlockChain fromFile(String fileName){
		//In here, we will iterate through every line of the file in order to add Blocks to the temporary ArrayList.

		ArrayList<Block> chainT = new ArrayList<Block>();
		List<String> lineList = new ArrayList<String>();

		BufferedReader reader = null;

		try { //We now open a BufferedReader (efficient) to read to the new file
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			while((line = reader.readLine()) != null) {
				lineList.add(line);
			}

			//ORDER OF EACH LINE : Index, Timestamp, Sender, Receiver, Amount, Nonce, Hash
			//*Note that if this is the first Block, the hash will have a value of "00000"
			if(lineList.size() % 7 == 0 && lineList.size() != 0){
				int index = 0;
				long timestamp = 0;
				String sender = "";
				String receiver = "";
				int amount = 0;
				String nonce = "";
				String hash = "";
				for(int i =0; i< lineList.size(); i++) {
					switch(i % 7){
						case 0:
							index = Integer.parseInt((String) lineList.get(i));
							curIndex = index;
							break;
						case 1:
							timestamp = Long.parseLong((String) lineList.get(i));
							break;
						case 2:
							sender = (String) lineList.get(i);
							break;
						case 3:
							receiver = (String) lineList.get(i);
							break;
						case 4:
							amount = Integer.parseInt((String) lineList.get(i));
							break;
						case 5:
							nonce = (String) lineList.get(i);
							break;
						case 6:
							if (i == 6) {
								hash = (String) lineList.get(i);
								Transaction temp = new Transaction(sender, receiver, amount);
								chainT.add(new Block(index, timestamp, temp, "00000", hash, nonce));
							} else {
								String tem = hash;
								Transaction temp = new Transaction(sender, receiver, amount);
								hash = (String) lineList.get(i);
								chainT.add(new Block(index, timestamp, temp, tem, hash, nonce));
							}
							break;
					}
				}
				return new BlockChain(chainT);
			} else {
				System.out.println("Invalid file. Not a BlockChain.");
				return null;
			}

		} catch (IOException e) { e.printStackTrace();} 
		finally { //Close the File reader
			try {
				if (reader != null)
				reader.close();
			} catch (IOException e) {}
		}
		return null;
	}


	/**
	 * Method to output Blockchain to output file (.txt)
	 * @param fileName String corresponding to the file name of the output file (e.g: blockchain.txt)
	 */
	public void toFile(String fileName){
		String wholeText = "";

		// We run a for loop over the Arraylist of Blocks to get all information for the file.
		// THe order is specific, that being 7 lines : index, timestamp, sender, receiver, amount, nonce, hash
		for(int i = 0; i < chainBlock.size();i++){
			Block currentBlock = chainBlock.get(i);

			wholeText += currentBlock.getIndex() + "\n";
			wholeText += currentBlock.getTime() + "\n";

			//To avoid redundancy, we will create the transaction for each object instead of calling the method 3 times
			Transaction currentTransaction = currentBlock.getTransaction();

			wholeText += currentTransaction.getSender() + "\n";
			wholeText += currentTransaction.getReceiver() + "\n";
			wholeText += currentTransaction.getAmount() + "\n";
			wholeText += currentBlock.getNonce() + "\n";
			wholeText += currentBlock.getHash();
			if (i != chainBlock.size()-1){
				wholeText+="\n";
			}
		}

		//We now open a BufferedWriter (efficient) to write to the new file
		BufferedWriter fileWriter = null;
		try {
			fileWriter = new BufferedWriter(new FileWriter(fileName));
			fileWriter.write(wholeText);

		} catch (IOException e) {e.printStackTrace();} 

		finally {
			try { //Close FileWriter
				if (fileWriter != null)
				fileWriter.close();
			} catch (IOException e) {}
		}
	}


	/**
	 * Boolean representating the if the Blockchain is valid.
	 * @return boolean of the valid Blockchain.
	 */
	public boolean validateBlockchain(){
		//We create a for loop to look at each index of each blocks, as well as hash comparison of the Arraylist
		// Only the index of the Block is not included in the hash, therefore we also create a case for an incorrect index
		for(int i = 0; i < chainBlock.size(); i++){
			Block currentBlock = chainBlock.get(i);
			String generateHash = "";
			int userBalance = 0;
			String currentSender = currentBlock.getTransaction().getSender();

			try {generateHash = Sha1.hash(currentBlock.toString());}
			catch (UnsupportedEncodingException e) {e.printStackTrace();}
			if (currentBlock.getIndex() != i || !currentBlock.getHash().equals(generateHash)) {
				return false;
			}

			//We also need to check for counterfeit amounts, in case the hash was generated properly but with false amounts
			for (int j = i; j >= 0; j--){
				Transaction transact = chainBlock.get(j).getTransaction();
				if (currentSender.equals(transact.getReceiver())){
					userBalance+=transact.getAmount();
				}
				if (currentSender.equals(transact.getSender()) && !currentSender.equals("bitcoin")){
					userBalance-=transact.getAmount();
				} 
			}

			if (userBalance < 0 && !currentSender.equals("bitcoin"))
				return false;
		}
		return true;
	}

	/**
	 * Integer representing amount of currency of in the sender's account
	 * @param username String corresponding to the Sender of the Transaction.
	 * @return int of the sender's balance (account inquiry).
	 */
	public int getBalance(String username){
		Integer userBalance = null;

		//We create a for loop to look at each Transaction of each block of the Arraylist
		for(int i = 0; i < chainBlock.size(); i++){
			Block currentBlock = chainBlock.get(i);
			Transaction currentTransaction = currentBlock.getTransaction();

			//In each transaction, we check the sender, receiver and amount ; deduction will be determined whether its a sender or receiver
			if (currentTransaction != null) {
				String sender = currentTransaction.getSender();
				String receiver = currentTransaction.getReceiver();
				int amount = currentTransaction.getAmount();

				//Special case on the first block (sender must be "bitcoin", in order to initiate currency in BlockChain)
				switch(i){
					case 0:
						if (sender.equals("bitcoin")){
							userBalance = 0;
						} else {
							System.out.println("ERROR. Currency has not been initiated by user 'bitcoin'. Incorrect sender for first block. Terminating process.");
							return 0;
						} if (username.equals(receiver)){
							userBalance = amount;
						}
						break;
					default:
						if (username.equals(sender)){
							userBalance -= amount;
						}
						if (username.equals(receiver)){
							userBalance += amount;
						}
				}
			}

		}

		if (userBalance == null)
			userBalance = 0;
		//We need to check if the ArrayList was empty.
		return chainBlock.size() == 0 ? 0 : userBalance;
	}


	/**
	 * Method to create a new Block with user input and randomized hash.
	 * @param transaction Transaction corresponding to the user input store in Transaction object.
	 * @return new Block object.
	 * @see Block
	 * @see Transaction
	 */
	public Block createBlock(Transaction transaction){
		//We create a temporary Block that excludes the hash in order to have an identical toString output when hashing
		long timestamp = System.currentTimeMillis();
		String prev = chainBlock.get(curIndex).getHash();
		String nonce = "";
		String hash = "123456";
		String subHash = hash.substring(0, Math.min(hash.length(), 5));

		if (curIndex == -1)
			prev = "00000";

		Block newBlock = new Block(curIndex+1, timestamp, transaction, prev, nonce);

		int leftSide = 33;
		int rightSide = 126;
		guessCounter = 0;
		Random randomizer = new Random();

		// Continue to randomize nonce until the hash begins with "00000"
		while(!hash.startsWith("00000")){
			guessCounter++;
			if (nonce.length() >= 19)
				nonce = Character.toString((char) (leftSide + (int) (randomizer.nextFloat() * (rightSide-leftSide+1)))) +
				Character.toString((char) (leftSide + (int) (randomizer.nextFloat() * (rightSide-leftSide+1))));

			int guess = leftSide + (int) (randomizer.nextFloat() * (rightSide-leftSide+1));
			nonce += Character.toString((char) guess);
			newBlock.setNonce(nonce);

			try {hash = Sha1.hash(newBlock.toString()); }
			catch (UnsupportedEncodingException e) {e.printStackTrace();}

			newBlock.setHash(hash);
		}

		return newBlock;
	}


	/**
	 * Method to add a Block to the existing BlockChain
	 * @param block Block object to be added to the BlockChain.
	 * @see Block
	 */
	public void add(Block block){
		chainBlock.add(block);
		curIndex++;
	}


	/**
	 * Integer represensting index position of BlockChain, also known as the length.
	 * @return int of current index position (also referred as the size of the BlockChain)
	 */
	public static int getIndex(){
		return curIndex;
	}

	/**
	 * Integer represensting amount of guesses were made for nonce before valid hash.
	 * @return int of tries to create nonce.
	 */
	public int getNumberOfGuesses(){
		return guessCounter;
	}
}