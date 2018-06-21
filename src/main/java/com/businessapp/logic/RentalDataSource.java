package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Article;
import com.businessapp.pojos.Customer;
import com.businessapp.pojos.Rental;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collection;

/**
 * Created by nhunimuni on 08.06.18.
 */
public class RentalDataSource implements RentalDataIntf {
    private final GenericEntityContainer<Rental> rentals;
    private PersistenceProviderIntf persistenceProvider = null;

    /**
     * Factory method that returns a CatalogItem data source.
     * @return new instance of data source.
     */
    public static RentalDataIntf getController( String name, PersistenceProviderIntf persistenceProvider ) {
        RentalDataIntf cds = new RentalDataSource( name );
        cds.inject( persistenceProvider );
        return cds;
    }
    /**
     * Private constructor.
     */
    private RentalDataSource( String name ) {
        this.rentals = new GenericEntityContainer<Rental>( name, Rental.class );
    }

    @Override
    public void inject(ControllerIntf dep) {
        if( dep instanceof PersistenceProviderIntf ) {
            this.persistenceProvider = (PersistenceProviderIntf)dep;
        }
    }

    @Override
    public void inject(Component parent) {

    }

    @Override
    public void start() {
        if( persistenceProvider != null ) {
            try {
                //Attempt to load container from persistent storage.
                persistenceProvider.loadInto( rentals.getId(), entity -> {
                    this.rentals.store( (Rental) entity );
                    return true;
                });
            } catch( IOException e ) {
                System.out.print( ", " );
                System.err.print( "No data: " + rentals.getId() );
                /*‐‐‐ BEGIN ‐‐‐ */
                RentalDataIntf mockDS = new RentalDataMockImpl();
                Component parent = new Component( rentals.getId(), null, null );
                mockDS.inject( parent );
                mockDS.start();
                for( Rental mockRentals: mockDS.findAllRentals() ) {
                    rentals.update( mockRentals );
                }
                persistenceProvider.save( rentals, rentals.getId() );
                /*‐‐‐ END ‐‐‐ */
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public Rental findRentalById(String id) {
        return rentals.findById(id);
    }

    @Override
    public Collection<Rental> findAllRentals() {
        return rentals.findAll();
    }

    @Override
    public Rental newRental(String firstName, String lastName, String articleId, String quantity, LocalDate from, LocalDate to) {
        Rental r = new Rental( null, firstName, lastName, articleId, quantity, from, to );
        rentals.update( r );
        if( persistenceProvider != null ) {
            persistenceProvider.save( rentals, rentals.getId() );
        }
        return r;
    }


    @Override
    public void updateRental(Rental c) {
        rentals.update(c);
        if( persistenceProvider != null ) {
            persistenceProvider.save( rentals, rentals.getId() );
        }
    }

    @Override
    public void deleteRentals(Collection<String> ids) {
        rentals.delete(ids);
        if( persistenceProvider != null ) {
            persistenceProvider.save( rentals, rentals.getId() );
        }
    }
}
