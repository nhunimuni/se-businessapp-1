package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Rental;

import java.time.LocalDate;
import java.util.Collection;

// Das Akronym CRUD umfasst die grundlegenden Datenbankoperationen Create, Read oder Retrieve, Update und Delete oder Destroy.
// CREATE, READ, UPDATE, DELETE

/**
 * Created by nhunimuni on 08.06.18.
 */
public interface RentalDataIntf extends ControllerIntf {

    /**
     * Factory method that returns a Customer data source.
     * @return new instance of Customer data source.
     */
    public static RentalDataIntf getController() {
        return new RentalDataMockImpl();
    }

    /**
     * READ
     * Public access methods to Customer data.
     */
    Rental findRentalById(String id );

    public Collection<Rental> findAllRentals();

    //CREATE
    public Rental newRental(String firstName, String lastName, String articleId , String quantity, LocalDate from, LocalDate to);

    //UPDATE
    public void updateRental( Rental c );

    //DELETE
    public void deleteRentals( Collection<String> ids);
}
