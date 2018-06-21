package com.businessapp.fxgui;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.logic.RentalDataIntf;
import com.businessapp.pojos.Article;
import com.businessapp.pojos.Customer;
import com.businessapp.pojos.LogEntry;
import com.businessapp.pojos.Rental;
import com.sun.org.apache.regexp.internal.RE;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by nhunimuni on 09.06.18.
 */
public class RentalFXMLController implements FXMLControllerIntf {
    private RentalDataIntf DS;

    /**
     * FXML skeleton defined as:
     * AnchorPane > GridPane > TableView	- GridPane as resizable container for TableView
     * AnchorPane > HBox > Button			- buttons in footer area
     *
     * Defined CSS style classes:
     *   .tableview-customer-column-id
     *   .tableview-customer-column-name
     *   .tableview-customer-column-status
     *   .tableview-customer-column-contacts
     *   .tableview-customer-column-notes
     *   .tableview-customer-column-notes-button
     *   .tableview-customer-hbox
     */

    @FXML
    private AnchorPane fxRental_AnchorPane;

    @FXML
    private GridPane fxRental_GridPane;

    @FXML
    private TableView<Rental> fxRental_TableView;

    @FXML
    private TableColumn<Rental,String> fxRental_TableCol_ID;

    @FXML
    private HBox fxRental_HBox;	// Bottom area container for buttons, search box, etc.

    /*
     * TableView model.
     */
    private final ObservableList<Rental> cellDataObservable = FXCollections.observableArrayList();

    private final String LABEL_ID		        = "Rental.-Nr.";
    private final String LABEL_DATE_FROM		= "From";
    private final String LABEL_DATE_TO	        = "To";
    private final String LABEL_CUSTOMER_ID      = "Customer-ID";
    private final String LABEL_STATUS           = "Status";
    private final String LABEL_ARTICLE	        = "Article";
    private final String LABEL_QUANTITY        	= "Qty.";
    private final String LABEL_NOTES	        = "Anmerk.";


    @Override
    public void inject(ControllerIntf dep) {
        this.DS = (RentalDataIntf)dep;
    }

    @Override
    public void inject(Component parent) {

    }

    @Override
    public void start() {
        // Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
        fxRental_HBox.prefWidthProperty().bind( ((AnchorPane) fxRental_AnchorPane).widthProperty().subtract( 12 ) );
        fxRental_HBox.prefHeightProperty().bind( ((AnchorPane) fxRental_AnchorPane).heightProperty() );

        fxRental_GridPane.prefWidthProperty().bind( ((AnchorPane) fxRental_AnchorPane).widthProperty().subtract( 16 ) );
        fxRental_GridPane.prefHeightProperty().bind( ((AnchorPane) fxRental_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 *
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
        fxRental_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
        //fxCustomer_GridPane.setStyle( "-fx-border-color: red;" );
        //fxCustomer_HBox.setStyle( "-fx-border-color: blue;" );

        fxRental_HBox.getStyleClass().add( "tableview-customer-hbox" );


		/*
		 * Construct TableView columns.
		 *
		 * TableView presents a row/column cell rendering of an ObservableList<Object>
		 * model. Each cell computes a "value" from the associated object property that
		 * defines how the object property is visualized in a TableView.
		 * See also: https://docs.oracle.com/javafx/2/ui_controls/table-view.htm
		 *
		 * TableView columns define how object properties are visualized and cell values
		 * are computed.
		 *
		 * In the simplest form, cell values are bound to object properties, which are
		 * public getter-names of the object class, and visualized in a cell as text.
		 *
		 * More complex renderings such as with graphical elements, e.g. buttons in cells,
		 * require overloading of the built-in behavior in:
		 *   - CellValueFactory - used for simple object property binding.
		 *   - CellFactory - overriding methods allows defining complex cell renderings.
		 *
		 * Constructing a TableView means defining
		 *   - a ObservableList<Object> model
		 *   - columns with name, css-style and Cell/ValueFactory.
		 *
		 * Variation 1: Initialize columns defined in FXML.
		 *  - Step 1: associate a .css class with column.
		 *  - Step 2: bind cell value to object property (must have public property getters,
		 *            getId(), getName()).
		 */
        fxRental_TableCol_ID.getStyleClass().add( "tableview-customer-column-id" );
        fxRental_TableCol_ID.setText( LABEL_ID );
        fxRental_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );

        TableColumn<Rental,String> tableCol_DATE_FROM = new TableColumn<>( LABEL_DATE_FROM );
        tableCol_DATE_FROM.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_DATE_FROM.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Rental c = cellData.getValue();
            observable.set( String.valueOf(c.getRentalStart()));
            return observable;
        });

        TableColumn<Rental,String> tableCol_DATE_TO = new TableColumn<>( LABEL_DATE_TO );
        tableCol_DATE_TO.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_DATE_TO.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Rental c = cellData.getValue();
            observable.set( String.valueOf(c.getRentalDateEnd()));
            return observable;
        });

        TableColumn<Rental,String> tableCol_CUSTOMER_ID = new TableColumn<>( LABEL_CUSTOMER_ID );
        tableCol_CUSTOMER_ID.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_CUSTOMER_ID.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Rental c = cellData.getValue();
            observable.set( c.getCustomerOfRental().getId());
            return observable;
        });

        TableColumn<Rental,String> tableCol_ARTICLE= new TableColumn<>( LABEL_ARTICLE);
        tableCol_ARTICLE.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_ARTICLE.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Rental c = cellData.getValue();
            observable.set( c.getLentArticle());
            return observable;
        });

        TableColumn<Rental,String> tableCol_QUANTITY = new TableColumn<>( LABEL_QUANTITY);
        tableCol_QUANTITY.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_QUANTITY.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            // Render status as 3-letter shortcut of Customer state enum.
            Rental c = cellData.getValue();
            observable.set(c.getQuantity());
            return observable;
        });

        TableColumn<Article,String> tableCol_STATUS = new TableColumn<>( LABEL_STATUS );
        tableCol_STATUS.getStyleClass().add( "tableview-customer-column-status" );
        tableCol_STATUS.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            // Render status as 3-letter shortcut of Customer state enum.
            Article c = cellData.getValue();
            observable.set( c.getStatus().name().substring( 0, 3 ) );
            return observable;
        });

        // TableColumn<Customer,String> tableCol_NOTES = new TableColumn<>( "Notes" );
        TableColumn<Rental,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
        tableCol_NOTES.getStyleClass().add( "tableview-customer-column-notes" );

        tableCol_NOTES.setCellFactory(

                // Complex rendering of Notes column as clickable button with number of notes indicator.
                new Callback<TableColumn<Rental,String>, TableCell<Rental, String>>() {

                    @Override
                    public TableCell<Rental, String> call( TableColumn<Rental, String> col ) {

                        col.setCellValueFactory( cellData -> {
                            Rental c = cellData.getValue();
                            StringProperty observable = new SimpleStringProperty();
                            observable.set( c.getId() );
                            return observable;
                        });

                        TableCell<Rental, String> tc = new TableCell<Rental, String>() {
                            final Button btn = new Button();

                            @Override public void updateItem( final String item, final boolean empty ) {
                                super.updateItem( item, empty );
                                int rowIdx = getIndex();
                                ObservableList<Rental> cust = fxRental_TableView.getItems();

                                if( rowIdx >= 0 && rowIdx < cust.size() ) {
                                    Rental rentals = cust.get( rowIdx );
                                    setGraphic( null );		// always clear, needed for refresh
                                    if( rentals != null ) {
                                        btn.getStyleClass().add( "tableview-customer-column-notes-button2" );
                                        List<LogEntry> nL = rentals.getNotes();
                                        btn.setText( "notes: " + nL.size() );
                                        setGraphic( btn );	// set button as rendering of cell value

                                        //Event updateEvent = new ActionEvent();
                                        btn.setOnMouseClicked( event -> {
                                            String fn = rentals.getCustomerOfRental().getLastName();
                                            String label = ( fn==null || fn.length()==0 )? rentals.getId() : fn;

                                            PopupNotes popupNotes = new PopupNotes( label, nL );

                                            popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
                                                // Notification that List<Note> has been updated.
                                                // update button label [note: <count>]
                                                btn.setText( "notes: " + rentals.getNotes().size() );
                                                // -> save node
                                                DS.updateRental( rentals );
                                            });

                                            popupNotes.show();
                                        });
                                    }
                                } else {
                                    //System.out.println( "OutOfBounds rowIdx() ==> " + rowIdx );
                                    setGraphic( null );		// reset button in other rows
                                }
                            }
                        };
                        return tc;
                    }
                });

        // Add programmatically generated columns to TableView. Columns appear in order.
        fxRental_TableView.getColumns().clear();
        fxRental_TableView.getColumns().addAll( Arrays.asList(
                fxRental_TableCol_ID,
                tableCol_DATE_FROM,
                tableCol_DATE_TO,
                tableCol_NOTES,
                tableCol_CUSTOMER_ID,
                tableCol_ARTICLE,
                tableCol_QUANTITY
        ));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
        fxRental_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
        fxRental_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
        fxRental_TableView.setRowFactory( tv -> {
            TableRow<Rental> row = new TableRow<>();
            row.setOnMouseClicked( event -> {
                if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
                    // Customer rowData = row.getItem();
                    // fxCustomer_TableView.getSelectionModel().select( row.getIndex() );
                    //table.getSelectionModel().select( Math.min( i, size - 1 ) );
                    fxRental_Update();
                }
            });
            return row;
        });

		/*
		 * Load objects into TableView model.
		 */
        fxRental_TableView.getItems().clear();
        Collection<Rental> col = DS.findAllRentals();
        if( col != null ) {
            cellDataObservable.addAll( col );
        }
        fxRental_TableView.setItems( cellDataObservable );
    }

    @Override
    public void stop() {

    }

    @FXML
    void fxRental_Delete() {
        ObservableList<Rental> selection = fxRental_TableView.getSelectionModel().getSelectedItems();
        List<Rental> toDel = new ArrayList<Rental>();
        List<String> ids = new ArrayList<String>();
        for( Rental c : selection ) {
            toDel.add( c );
        }
        fxRental_TableView.getSelectionModel().clearSelection();
        for( Rental c : toDel ) {
            ids.add( c.getId() );
            // should not alter cellDataObservable while iterating over selection
            cellDataObservable.remove( c );
        }
        DS.deleteRentals( ids );
    }

    @FXML
    void fxRental_New() {
        Rental art = DS.newRental( null, null, null , null, LocalDate.now(), LocalDate.now());
        openUpdateDialog( art, true );
    }

    @FXML
    void fxRental_Update() {
        Rental rental = fxRental_TableView.getSelectionModel().getSelectedItem();
        if( rental != null ) {
            openUpdateDialog( rental, false );
        } else {
            System.err.println( "nothing selected." );
        }
    }

    @FXML
    void fxCustomer_Exit() {
        App.getInstance().stop();
    }

    /*
     * Private helper methods.
     */
    private void openUpdateDialog( Rental c, boolean newItem ) {
        List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
        String fn = c.getCustomerOfRental().getId();
        String label = ( fn==null || fn.length()==0 )? c.getId() : fn;

        PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
                new StringTestUpdateProperty( LABEL_ID, c.getId(), false ),
                new StringTestUpdateProperty( LABEL_DATE_FROM, c.getRentalStart().toString(), true ),
                new StringTestUpdateProperty( LABEL_DATE_TO, c.getRentalDateEnd().toString(), true ),
                new StringTestUpdateProperty( LABEL_CUSTOMER_ID, c.getId(), false),
                new StringTestUpdateProperty( LABEL_ARTICLE, c.getLentArticle(), true ),
                new StringTestUpdateProperty( LABEL_QUANTITY, c.getQuantity(), true )
        ));

        // called when "OK" button in EntityEntryDialog is pressed
        dialog.addEventHandler( ActionEvent.ACTION, event -> {
            updateObject( c, altered, newItem );
            System.out.println(DS.findRentalById(c.getId()).getCustomerOfRental().getLastName() + " " + DS.findRentalById(c.getId()).getLentArticle());
        });

        dialog.show();
    }

    private void updateObject( Rental rental, List<StringTestUpdateProperty> altered, boolean newItem ) {
        for( StringTestUpdateProperty dp : altered ) {
            String pName = dp.getName();
            String alteredValue = dp.getValue();
            //System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

            if( pName.equals( LABEL_QUANTITY ) ) {
                rental.setQuantity(alteredValue);
            } else {
                rental.setQuantity(rental.getQuantity());
            }
            if( pName.equals( LABEL_ARTICLE ) ) {
                rental.setLentArticle(alteredValue);
            } else {
                rental.setLentArticle(rental.getLentArticle());
            }
            if (pName.equals(LABEL_DATE_TO)) {
                String[] stringSplit = alteredValue.split("[-./]");
                rental.setRentalEnd(LocalDate.of(
                        Integer.parseInt(stringSplit[0]),
                        Integer.parseInt(stringSplit[1]),
                        Integer.parseInt(stringSplit[2])));
            }
            if (pName.equals(LABEL_DATE_FROM)) {
                String[] stringSplit = alteredValue.split("[-./]");
                rental.setRentalEnd(LocalDate.of(
                        Integer.parseInt(stringSplit[0]),
                        Integer.parseInt(stringSplit[1]),
                        Integer.parseInt(stringSplit[2])));
            }
        }
        if( altered.size() > 0 ) {
            DS.updateRental( rental );	// update object in persistent store
            if( newItem ) {
                int last = cellDataObservable.size();
                cellDataObservable.add( last, rental );
            }
            // refresh TableView (trigger update
            fxRental_TableView.getColumns().get(0).setVisible(false);
            fxRental_TableView.getColumns().get(0).setVisible(true);

            altered.clear();	// prevent double save if multiple events fire
        }
    }
}
