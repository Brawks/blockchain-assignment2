//Student : Samuel Saint-Fleur
// ID : 300008314
// Course : CSI2110
// Assignment 2
// File: Transaction

/**
 * This class is the object holder for every transaction that occurs within the blockchain. 
 * Every transaction consists of a sender, a receiver, and an amount.
 *
 * @author Samuel Saint-Fleur
 * @version October 2018
 */
public class Transaction {
	
	private String sender;
	private String receiver;
	private int amount;

	/**
	 * Constructor of the class Transaction
	 * @param sen String corresponding to the Sender of the transaction.
	 * @param rec String corresponding to the Receiver of the transaction.
	 * @param amount Integer corresponding to the value (currency) being transfered from Sender to Receiver.
	 */
	public Transaction(String sen, String rec, int amount){
		//We set all three values for the Transaction
		sender = sen;
		receiver = rec;
		this.amount = amount;
	}

	/**
	 * Returns the String representing Sender of transaction.
	 * @return the String of the transaction Sender.
	 */
	public String getSender(){
		return sender;
	}

	/**
	 * Returns the String representing Receiver of transaction.
	 * @return the String of the transaction Receiver.
	 */
	public String getReceiver(){
		return receiver;
	}

	/**
	 * Returns the Integer representing Amount of transaction.
	 * @return the int of the transaction value.
	 */
	public int getAmount(){
		return amount;
	}

	/**
	 * Returns the String representing the transaction.
	 * @return the String of the transaction.
	 */
	public String toString() {
		return sender + ":" + receiver + "=" + amount;
	}

}