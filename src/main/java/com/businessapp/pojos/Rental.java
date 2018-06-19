package com.businessapp.pojos;

import com.businessapp.logic.IDGen;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nhunimuni on 08.06.18.
 */
public class Rental implements EntityIntf{

    private static final long serialVersionUID = 1L;
    private static IDGen IDG = new IDGen( "R.", IDGen.IDTYPE.AIRLINE, 6 );

    /*
     * Properties.
     */
    private String rentalId = null;
    private Date rentalDate;
    private Customer lentBy = null;
    private String lentArticle  = null;
    private String quantity  = null;
    private List<LogEntry> notes = new ArrayList<LogEntry>();

    /**
     * Private default constructor (required by JSON deserialization).
     */
    @SuppressWarnings("unused")
    private Rental() { }

    /**
     * Public constructor.
     * @param id if customer id is null, an id is generated for the new customer object.
     * @param firstName
     * @param lastName
     * @param articleID
     */
    public Rental( String id, String firstName, String lastName, String articleID, String quantity) {
        rentalId = id==null? IDG.nextId() : id;
        rentalDate = new Date();
        lentBy = new Customer(null, firstName, lastName);
        lentArticle = articleID;
        this.quantity = quantity;
        this.notes.add( new LogEntry( "Rental record created." ) );
    }

    /**
     * Public getter/setter methods.
     */
    public String getId() {
        return rentalId;
    }

    public Customer getCustomerOfRental() {return lentBy; }

    public String getLentArticle() {return lentArticle; }

    public String getRentalDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String date = sdf.format(rentalDate);
        return date;
    }

    public String getQuantity() {return quantity;}

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

    public Customer setCustomerOfRental(Customer cust) {
        lentBy = cust;
        return lentBy;
    }

    public String setLentArticle(String art) {
        lentArticle = art;
        return lentArticle;
    }

    public Date setRentalDate(String dateInString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = sdf.parse(dateInString);
        return date;
    }

    public String setQuantity(String qty){
        quantity = qty;
        return  quantity;
    }

    public void addNote(String note) {
        notes.add( new LogEntry(note));
    }
}
