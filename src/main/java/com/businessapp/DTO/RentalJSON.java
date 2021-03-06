package com.businessapp.DTO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.businessapp.pojos.Rental;
import com.fasterxml.jackson.annotation.*;

import com.businessapp.pojos.Article;
import com.businessapp.pojos.LogEntry;

/**
 * Created by nhunimuni on 05.06.18.
 * Private JSON DTO (Data Access Object) class that is associated with Customer Pojo.
 */
public class RentalJSON extends Rental implements JSONIntf{
    private static final long serialVersionUID = 1L;

    /**
     * Required by JSON de-serialization.
     */
    private RentalJSON() {
        super( null, null , null, null, null, null, null);
        this.getNotes().clear();
    }


    /**
     * Public copy constructor to create JSON DTO from original POJO.
     * @param c copied Customer object.
     */
    public RentalJSON( Rental c ) {
        super( c.getId(), c.getCustomerOfRental().getFirstName(), c.getCustomerOfRental().getLastName(), c.getLentArticle(), c.getQuantity(), c.getRentalStart(), c.getRentalDateEnd() );
        this.getNotes().clear();
        for( LogEntry le : c.getNotes() ) {
            this.getNotes().add( le );
        }
        this.setStatus( c.getStatus() );
    }

    /*@JsonGetter("rentalDateStart")
    public String getRentalStartAsString() {
        return getRentalDateEnd().toString();
    }

    @JsonGetter("rentalDateEnd")
    public String getRentalEndAsString() {
        return getRentalStart().toString();
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

    @JsonSetter("rentalDateStart")
    public Rental setRentalDateStartAsString(String publishedAsString) {
        String[] stringSplit = publishedAsString.split("[-./]");
        setRentalDateStart(LocalDate.of(
                Integer.parseInt(stringSplit[0]),
                Integer.parseInt(stringSplit[1]),
                Integer.parseInt(stringSplit[2])));
        return this;
    }*/

    /**
     * Public method to create original POJO from JSON DTO.
     * @return Rental POJO.
     */
    @JsonIgnore
    public Rental getRentals() {
        Rental c = new Rental( this.getId(), this.getCustomerOfRental().getFirstName(), this.getCustomerOfRental().getLastName(), this.getLentArticle(), this.getQuantity(), this.getRentalStart(), this.getRentalDateEnd() );
        c.getNotes().clear();
        for( LogEntry le : this.getNotes() ) {
            c.getNotes().add( le );
        }
        //c.setStatus( this.getStatus() );
        return c;
    }

    /**
     * Custom DTO-Serializer for 'notes' property.
     * Maps notes as LogEntry array to String list.
     * @return notes as String list.
     */
    @JsonGetter("notes")
    public List<String> getNotesAsStringList() {
        List<String>res = new ArrayList<String>();
        for( LogEntry n : getNotes() ) {
            res.add( n.toString() );
        }
        return res;
    }

    /**
     * Custom DTO-de-Serializer for 'notes' property.
     * Maps notes from String list to LogEntry array.
     * @param notesAsStr notes as String list.
     * @return self reference.
     */
    @JsonSetter("notes")
    public Rental setNotesAsStringList( String[] notesAsStr ) {
        for( String noteAsStr : notesAsStr ) {
            LogEntry note = new LogEntry( noteAsStr );
            getNotes().add( note );
        }
        return this;
    }
}
