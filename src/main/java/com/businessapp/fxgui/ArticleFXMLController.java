package com.businessapp.fxgui;

import com.businessapp.logic.ArticleDataIntf;
import com.businessapp.pojos.Article;

import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.businessapp.App;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.LogEntry;

/**
 * FXML Controller class for Katalog.fxml
 *
 */
public class ArticleFXMLController implements FXMLControllerIntf {
    private ArticleDataIntf DS;

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
    private AnchorPane fxCustomer_AnchorPane;

    @FXML
    private GridPane fxCustomer_GridPane;

    @FXML
    private TableView<Article> fxCustomer_TableView;

    @FXML
    private TableColumn<Article,String> fxCustomer_TableCol_ID;


    @FXML
    private HBox fxCustomer_HBox;	// Bottom area container for buttons, search box, etc.

    /*
     * TableView model.
     */
    private final ObservableList<Article> cellDataObservable = FXCollections.observableArrayList();

    private final String LABEL_ID		= "Art.-Nr.";
    private final String LABEL_NAME	    = "Name";
    private final String LABEL_STATUS	= "Status";
    private final String LABEL_NOTES	= "Anmerk.";
    private final String LABEL_QUANTITY	= "Qty.";


    @Override
    public void inject( ControllerIntf dep ) {
        this.DS = (ArticleDataIntf)dep;
    }

    @Override
    public void inject( Component parent ) {
    }

    @Override
    public void start() {
        // Width adjustment assumes layoutX="12.0", layoutY="8.0" offset.
        fxCustomer_HBox.prefWidthProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).widthProperty().subtract( 12 ) );
        fxCustomer_HBox.prefHeightProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).heightProperty() );

        fxCustomer_GridPane.prefWidthProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).widthProperty().subtract( 16 ) );
        fxCustomer_GridPane.prefHeightProperty().bind( ((AnchorPane) fxCustomer_AnchorPane).heightProperty().subtract( 70 ) );

		/*
		 * Bottom area HBox extends from the top across the entire AnchorPane hiding
		 * GridPane/TableView underneath (depending on z-stacking order). This prevents
		 * Mouse events from being propagated to TableView.
		 *
		 * Solution 1: Disable absorbing Mouse events in HBox layer and passing them through
		 * to the underlying GridPane/TableView layer (Mouse event "transparency").
		 */
        fxCustomer_HBox.setPickOnBounds( false );

		/*
		 * Visualize resizing propagation by colored bounding boxes.
		 */
        //fxCustomer_GridPane.setStyle( "-fx-border-color: red;" );
        //fxCustomer_HBox.setStyle( "-fx-border-color: blue;" );

        fxCustomer_HBox.getStyleClass().add( "tableview-customer-hbox" );


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
        fxCustomer_TableCol_ID.getStyleClass().add( "tableview-customer-column-id" );
        fxCustomer_TableCol_ID.setText( LABEL_ID );
        fxCustomer_TableCol_ID.setCellValueFactory( new PropertyValueFactory<>( "id" ) );

        TableColumn<Article,String> tableCol_NAME = new TableColumn<>( LABEL_NAME );
        tableCol_NAME.getStyleClass().add( "tableview-customer-column-name" );
        tableCol_NAME.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            Article c = cellData.getValue();
            observable.set( c.getArticleName() );
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

        TableColumn<Article,String> tableCol_QUANTITY = new TableColumn<>( LABEL_QUANTITY);
        tableCol_QUANTITY.getStyleClass().add( "tableview-customer-column-contacts" );
        tableCol_QUANTITY.setCellValueFactory( cellData -> {
            StringProperty observable = new SimpleStringProperty();
            // Render status as 3-letter shortcut of Customer state enum.
            Article c = cellData.getValue();
            observable.set(c.getQuantity());
            return observable;
        });

        // TableColumn<Customer,String> tableCol_NOTES = new TableColumn<>( "Notes" );
        TableColumn<Article,String> tableCol_NOTES = new TableColumn<>( LABEL_NOTES );
        tableCol_NOTES.getStyleClass().add( "tableview-customer-column-notes" );

        tableCol_NOTES.setCellFactory(

                // Complex rendering of Notes column as clickable button with number of notes indicator.
                new Callback<TableColumn<Article,String>, TableCell<Article, String>>() {

                    @Override
                    public TableCell<Article, String> call( TableColumn<Article, String> col ) {

                        col.setCellValueFactory( cellData -> {
                            Article c = cellData.getValue();
                            StringProperty observable = new SimpleStringProperty();
                            observable.set( c.getId() );
                            return observable;
                        });

                        TableCell<Article, String> tc = new TableCell<Article, String>() {
                            final Button btn = new Button();

                            @Override public void updateItem( final String item, final boolean empty ) {
                                super.updateItem( item, empty );
                                int rowIdx = getIndex();
                                ObservableList<Article> cust = fxCustomer_TableView.getItems();

                                if( rowIdx >= 0 && rowIdx < cust.size() ) {
                                    Article article = cust.get( rowIdx );
                                    setGraphic( null );		// always clear, needed for refresh
                                    if( article != null ) {
                                        btn.getStyleClass().add( "tableview-customer-column-notes-button2" );
                                        List<LogEntry> nL = article.getNotes();
                                        btn.setText( "notes: " + nL.size() );
                                        setGraphic( btn );	// set button as rendering of cell value

                                        //Event updateEvent = new ActionEvent();
                                        btn.setOnMouseClicked( event -> {
                                            String fn = article.getArticleName();
                                            String label = ( fn==null || fn.length()==0 )? article.getId() : fn;

                                            PopupNotes popupNotes = new PopupNotes( label, nL );

                                            popupNotes.addEventHandler( ActionEvent.ACTION, evt -> {
                                                // Notification that List<Note> has been updated.
                                                // update button label [note: <count>]
                                                btn.setText( "notes: " + article.getNotes().size() );
                                                // -> save node
                                                DS.updateArticle( article );
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
        fxCustomer_TableView.getColumns().clear();
        fxCustomer_TableView.getColumns().addAll( Arrays.asList(
                fxCustomer_TableCol_ID,
                tableCol_STATUS,
                tableCol_NOTES,
                tableCol_NAME,
                tableCol_QUANTITY

        ));

		/*
		 * Define selection model that allows to select multiple rows.
		 */
        fxCustomer_TableView.getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );

		/*
		 * Allow horizontal column squeeze of TableView columns. Column width can be fixed
		 * with -fx-pref-width: 80px;
		 */
        fxCustomer_TableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );


		/*
		 * Double-click on row: open update dialog.
		 */
        fxCustomer_TableView.setRowFactory( tv -> {
            TableRow<Article> row = new TableRow<>();
            row.setOnMouseClicked( event -> {
                if( event.getClickCount() == 2 && ( ! row.isEmpty() ) ) {
                    // Customer rowData = row.getItem();
                    // fxCustomer_TableView.getSelectionModel().select( row.getIndex() );
                    //table.getSelectionModel().select( Math.min( i, size - 1 ) );
                    fxCustomer_Update();
                }
            });
            return row;
        });

		/*
		 * Load objects into TableView model.
		 */
        fxCustomer_TableView.getItems().clear();
        Collection<Article> col = DS.findAllArticles();
        if( col != null ) {
            cellDataObservable.addAll( col );
        }
        fxCustomer_TableView.setItems( cellDataObservable );
    }

    @Override
    public void stop() {
    }


    @FXML
    void fxCustomer_Delete() {
        ObservableList<Article> selection = fxCustomer_TableView.getSelectionModel().getSelectedItems();
        List<Article> toDel = new ArrayList<Article>();
        List<String> ids = new ArrayList<String>();
        for( Article c : selection ) {
            toDel.add( c );
        }
        fxCustomer_TableView.getSelectionModel().clearSelection();
        for( Article c : toDel ) {
            ids.add( c.getId() );
            // should not alter cellDataObservable while iterating over selection
            cellDataObservable.remove( c );
        }
        DS.deleteArticle( ids );
    }

    @FXML
    void fxCustomer_New() {
        Article art = DS.newArticle( null, null );
        openUpdateDialog( art, true );
    }

    @FXML
    void fxCustomer_Update() {
        Article article = fxCustomer_TableView.getSelectionModel().getSelectedItem();
        if( article != null ) {
            openUpdateDialog( article, false );
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
    private void openUpdateDialog( Article c, boolean newItem ) {
        List<StringTestUpdateProperty> altered = new ArrayList<StringTestUpdateProperty>();
        String fn = c.getArticleName();
        String label = ( fn==null || fn.length()==0 )? c.getId() : fn;

        PopupUpdateProperties dialog = new PopupUpdateProperties( label, altered, Arrays.asList(
                new StringTestUpdateProperty( LABEL_ID, c.getId(), false ),
                new StringTestUpdateProperty( LABEL_NAME, c.getArticleName(), true ),
                new StringTestUpdateProperty( LABEL_STATUS, c.getStatus().name(), true ),
                new StringTestUpdateProperty( LABEL_QUANTITY, c.getQuantity(), true )
        ));

        // called when "OK" button in EntityEntryDialog is pressed
        dialog.addEventHandler( ActionEvent.ACTION, event -> {
            updateObject( c, altered, newItem );
            System.out.println(DS.findArticleById(c.getId()).getArticleName());
        });

        dialog.show();
    }

    private void updateObject( Article article, List<StringTestUpdateProperty> altered, boolean newItem ) {
        for( StringTestUpdateProperty dp : altered ) {
            String pName = dp.getName();
            String alteredValue = dp.getValue();
            //System.err.println( "altered: " + pName + " from [" + dp.prevValue() + "] to [" + alteredValue + "]" );

			/*if( pName.equals( LABEL_NAME ) ) {
				customer.setName( alteredValue );
			}*/
            if( pName.equals( LABEL_NAME ) ) {
                article.setArticleName(alteredValue);
            }
            if( pName.equals( LABEL_STATUS ) ) {
                String av = alteredValue.toUpperCase();
                if( av.startsWith( "AVA" ) ) {
                    article.setStatus(Article.ArticleStatus.AVAILABLE);
                }
                if( av.startsWith( "USE" ) ) {
                    article.setStatus( Article.ArticleStatus.IN_USE );
                }
                if( av.startsWith( "LOST" ) ) {
                    article.setStatus( Article.ArticleStatus.LOST );
                }
                if( av.startsWith( "DAM" ) ) {
                    article.setStatus( Article.ArticleStatus.DAMAGED );
                }
            }
            if( pName.equals( LABEL_QUANTITY ) ) {
                article.setQuantity(alteredValue);
            }
        }
        if( altered.size() > 0 ) {
            DS.updateArticle( article );	// update object in persistent store
            if( newItem ) {
                int last = cellDataObservable.size();
                cellDataObservable.add( last, article );
            }
            // refresh TableView (trigger update
            fxCustomer_TableView.getColumns().get(0).setVisible(false);
            fxCustomer_TableView.getColumns().get(0).setVisible(true);

            altered.clear();	// prevent double save if multiple events fire
        }
    }
}
