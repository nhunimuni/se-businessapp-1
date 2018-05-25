package com.businessapp.pojos;

import java.util.ArrayList;
import java.util.List;

import com.businessapp.logic.IDGen;


/**
 * Customer is an entity that represents a person (or a business)
 * to which a business activity can be associated.
 *
 */
public class Customer implements EntityIntf  {
	private static final long serialVersionUID = 1L;

	private static IDGen IDG = new IDGen( "C.", IDGen.IDTYPE.AIRLINE, 6 );

	// Customer states.
	public enum CustomerStatus { ACTIVE, SUSPENDED, TERMINATED };


	/*
	 * Properties.
	 */
	private String id = null;
	private String lastName = null;
	private String firstName = null;

	//private String name = null;

	/*
	private String firstName = null;
	private String lastName = null;
	*/

	private List<String> contacts = new ArrayList<String>();

	private List<LogEntry> notes = new ArrayList<LogEntry>();

	private CustomerStatus status = CustomerStatus.ACTIVE;


	/**
	 * Private default constructor (required by JSON deserialization).
	 */
	@SuppressWarnings("unused")
	private Customer() { }


	/*public Customer( String id, String name ) {
		this.id = id==null? IDG.nextId() : id;
		this.name = name;
		this.notes.add( new LogEntry( "Customer record created." ) );
	}*/

	/**
	 * Public constructor.
	 * @param id if customer id is null, an id is generated for the new customer object.
	 * @param firstName
	 * @param lastName
	 */
	public Customer( String id, String firstName, String lastName) {
		this.id = id==null? IDG.nextId() : id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.notes.add( new LogEntry( "Customer record created." ) );
	}

	/**
	 * Public getter/setter methods.
	 */
	public String getId() {
		return id;
	}

	/*public String getName() {
		return name;
	}*/

	public String getFirstName() {return firstName; }

	public String getLastName() {return lastName; }

	public List<String> getContacts() {
		return contacts;
	}

	public List<String> getNotesAsStringList() {
		List<String>res = new ArrayList<String>();
		for( LogEntry n : notes ) {
			res.add( n.toString() );
		}
		return res;
	}

	public List<LogEntry> getNotes() {
		return notes;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	/*public Customer setName( String name ) {
		this.name = name;
		return this;
	}*/

	public String setFirstName(String firstName) {
		this.firstName = firstName;
		return firstName;
	}

	public String setLastName(String lastName) {
		this.lastName = lastName;
		return lastName;
	}

	public Customer addContact( String contact ) {
		contacts.add( contact );
		return this;
	}

	public Customer setStatus( CustomerStatus status ) {
		this.status = status;
		return this;
	}

	public void addNote(String note) {
		notes.add( new LogEntry(note));
	}

}
