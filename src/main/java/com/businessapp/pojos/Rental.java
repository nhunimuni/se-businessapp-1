package com.businessapp.pojos;

import com.businessapp.logic.IDGen;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nhunimuni on 08.06.18.
 */
public class Rental implements EntityIntf{

    private static final long serialVersionUID = 1L;
    private static IDGen IDG = new IDGen( "R.", IDGen.IDTYPE.AIRLINE, 6 );

    public enum RentalStatus {
        ACTIVE, EXCEEDED
    }
    /*
     * Properties.
     */
    private String rentalId = null;
    //private Date rentalDateStart;
    //private String rentalDateEnd;
    private LocalDate rentalDateStart;
    private LocalDate rentalDateEnd;
    private Customer lentBy = null;
    private String lentArticle  = null;
    private String quantity  = null;
    private List<LogEntry> notes = new ArrayList<LogEntry>();
    private RentalStatus status = RentalStatus.ACTIVE;

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
    public Rental( String id, String firstName, String lastName, String articleID, String quantity, LocalDate from, LocalDate to) {
        rentalId = id==null? IDG.nextId() : id;
        rentalDateStart = from;
        rentalDateEnd = to;
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

    @JsonGetter("rentalDateStart")
    public String getRentalStartAsString() {
        return rentalDateStart.toString();
    }

    public LocalDate getRentalStart() {
        return rentalDateStart;
    }

    @JsonGetter("rentalDateEnd")
    public String getRentalEndAsString() {
        return rentalDateEnd.toString();
    }

    public LocalDate getRentalDateEnd() {
        return rentalDateEnd;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public String getQuantity() {return quantity;}

    @JsonGetter("notes")
    public List<String> getNotesAsStringList() {
        List<String> res = new ArrayList<>();
        for (LogEntry n : notes) {
            res.add(n.toString());
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

   @JsonSetter("rentalDateStart")
    public Rental setRentalDateStartAsString(String publishedAsString) {
        String[] stringSplit = publishedAsString.split("[-./]");
        setRentalDateStart(LocalDate.of(
                Integer.parseInt(stringSplit[0]),
                Integer.parseInt(stringSplit[1]),
                Integer.parseInt(stringSplit[2])));
        return this;
    }

    public void setRentalDateStart(LocalDate from) {
        rentalDateStart = from;
    }

    @JsonSetter("rentalDateEnd")
    public Rental setRentalEndAsString(String publishedAsString) {
        String[] stringSplit = publishedAsString.split("[-./]");
        setRentalEnd(LocalDate.of(
                Integer.parseInt(stringSplit[0]),
                Integer.parseInt(stringSplit[1]),
                Integer.parseInt(stringSplit[2])));
        return this;
    }

    public void setRentalEnd(LocalDate to) {
        rentalDateEnd = to;
    }

    public String setQuantity(String qty){
        quantity = qty;
        return  quantity;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public void addNote(String note) {
        notes.add( new LogEntry(note));
    }

    /**
     * Custom Json‐de‐Serializer for 'notes' property.
     * Maps notes from String list to LogEntry array.
     *
     * @param notesAsStr notes as String list.
     * @return self reference.
     */
    @JsonSetter("notes")
    public Rental setNotesAsStringList(String[] notesAsStr) {
        for (String noteAsStr : notesAsStr) {
            LogEntry note = new LogEntry(noteAsStr);
            getNotes().add(note);
        }
        return this;
    }
}
