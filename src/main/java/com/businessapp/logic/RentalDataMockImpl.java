package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Article;
import com.businessapp.pojos.Customer;
import com.businessapp.pojos.Rental;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by nhunimuni on 08.06.18.
 */
public class RentalDataMockImpl implements RentalDataIntf{

    private final HashMap<String,Rental> _data;	    // HashMap as data container
    private final RentalDataIntf DS;				// Data Source/Data Store Intf
    private Component parent;						// parent component

    /**
     * Constructor.
     */
    RentalDataMockImpl() {
        this._data = new HashMap<String,Rental>();
        this.DS = this;
    }

    /**
     * Dependency injection methods.
     */
    @Override
    public void inject(ControllerIntf dep) {

    }

    @Override
    public void inject(Component parent) {

    }

    /**
     * Start.
     */
    @Override
    public void start() {

        Customer c1 = new Customer(null, "Minto", "Tran");
        Rental rent1 = DS.newRental("Minto", "Tran", "A123456", "10", LocalDate.of(2018, 4, 11), LocalDate.of(2018, 5, 12));

        Customer c2 = new Customer(null, "Minto", "Tran");
        Rental rent2 = DS.newRental("Minto", "Tran", "A123456", "5", LocalDate.of(2018, 5, 12), LocalDate.of(2019, 4, 30));

        Customer c3 = new Customer(null, "Minto", "Tran");
        Rental rent3 = DS.newRental("Minto", "Tran", "A123456", "1", LocalDate.of(2018, 6, 13), LocalDate.of(2018, 7, 10));

    }

    @Override
    public void stop() {
    }

    @Override
    public Rental findRentalById(String id) {
        return _data.get(id);
    }

    @Override
    public Collection<Rental> findAllRentals() {
        return _data.values();
    }

    @Override
    public Rental newRental(String firstName, String lastName, String articleId, String quantity, LocalDate from, LocalDate to) {
        Rental r = new Rental(null, firstName, lastName, articleId, quantity, to, from);
        _data.put(r.getId(), r);
        return r;
    }

    @Override
    public void updateRental(Rental c) {
        String msg = "updated: ";
        if( c != null ) {
            Rental e2 = _data.get( c.getId() );
            if( c != e2 ) {
                if( e2 != null ) {
                    _data.remove( e2.getId() );
                }
                msg = "created: ";
                _data.put( c.getId(), c );
            }
            //save( msg, c );
            System.err.println( msg + c.getId() );
        }
    }

    @Override
    public void deleteRentals(Collection<String> ids) {
        String showids = "";
        for( String id : ids ) {
            _data.remove( id );
            showids += ( showids.length()==0? "" : ", " ) + id;
        }
        if( ids.size() > 0 ) {
            //save( "deleted: " + idx, customers );
            System.err.println( "deleted: " + showids );
        }
    }
}
