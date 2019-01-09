//Student : Samuel Saint-Fleur
// ID : 300008314
// Course : CSI2110
// Assignment 2
// File: Block

import java.sql.Timestamp;


/**
 * This class is the object holder for every block inside of the blockchain. 
 * Every block consists of an index, a timestamp since its creation, a transaction,
 * a nonce (randomized string), the hash of the previous block and the block's own hash.
 *
 * @author Samuel Saint-Fleur
 * @version October 2018
 */
public class Block {

	private int index;
	private Timestamp timestamp;

	private Transaction transaction;
	private String nonce;
	private String previousHash;
	private String hash;

	/**
	 * Constructor of the class Transaction
	 * @param index Integer corresponding to the index of the Block in the BlockChain.
	 * @param time Long corresponding to the System milliseconds of when the Block was created.
	 * @param transaction Transaction corresponding the Transaction tied to the Block.
	 * @param prev String corresponding to the generated hash of the previous Block in the BlockChain
	 * @param hash String corresponding to the generated hash of the Block object.
	 * @param nonce String that is randomly generated in order to create hash.
	 * @see Transaction
	 */
	public Block(int index, long time, Transaction transaction, String prev, String hash, String nonce){
		this.index = index;
		timestamp = new Timestamp(time);
		this.transaction = transaction;
		previousHash = prev;
		this.hash = hash;
		this.nonce = nonce;
	}

	/**
	 * Constructor of the class Transaction
	 * @param index Integer corresponding to the index of the Block in the BlockChain.
	 * @param time Long corresponding to the System milliseconds of when the Block was created.
	 * @param transaction Transaction corresponding the Transaction tied to the Block.
	 * @param prev String corresponding to the generated hash of the previous Block in the BlockChain
	 * @param nonce String that is randomly generated in order to create hash.
	 * @see Transaction
	 */
	public Block(int index, long time, Transaction transaction, String prev, String nonce){
		this.index = index;
		timestamp = new Timestamp(time);
		this.transaction = transaction;
		previousHash = prev;
		this.nonce = nonce;
	}

	/**
	 * Returns the String representing nonce of block.
	 * @return the String of the Block object's nonce.
	 */
	public String getNonce() {
		return nonce;
	}

	/**
	 * Sets the String representing nonce of block.
	 * @param s New value of nonce.
	 */
	public void setNonce(String s) {
		nonce = s;
	}

	/**
	 * Sets the String representing hash of block.
	 * @param s New value of hash.
	 */
	public void setHash(String s) {
		hash = s;
	}

	/**
	 * Returns the Long representing time since timestamp was created.
	 * @return the Long of the timestamp.
	 * @see java.sql.Timestamp
	 */
	public long getTime(){
		return timestamp.getTime();
	}

	/**
	 * Returns the Integer representing the index of Block in the BlockChain.
	 * @return the index of the Block object.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Returns the String representing has of the previous Block in the BlockChain.
	 * @return the String of the previous hash.
	 */
	public String getPreviousH(){
		return previousHash;
	}

	/**
	 * Returns the String representing hash of the Block object.
	 * @return the String of the hash.
	 */
	public String getHash(){
		return hash;
	}

	/**
	 * Returns a Transaction object representing Transaction linked to the Block.
	 * @return a new Transaction object.
	 */
	public Transaction getTransaction(){
		return new Transaction(transaction.getSender(), transaction.getReceiver(), transaction.getAmount());
	}

	/**
	 * Returns the String representing of the Block.
	 * @return the String of the Block.
	 */
	public String toString() {
		return timestamp.toString() + ":" + transaction.toString() + "." + nonce + previousHash;
	}

}