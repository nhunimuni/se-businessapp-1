package com.businessapp.logic;

import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Article;
import com.businessapp.pojos.Customer;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by nhunimuni on 25.05.18.
 */
class ArticleDataMockImpl implements ArticleDataIntf{

    private final HashMap<String,Article> _data;	// HashMap as data container
    private final ArticleDataIntf DS;				// Data Source/Data Store Intf
    private Component parent;						// parent component

    /**
     * Constructor.
     */
    ArticleDataMockImpl() {
        this._data = new HashMap<String,Article>();
        this.DS = this;
    }

    /**
     * Dependency injection methods.
     */
    @Override
    public void inject( ControllerIntf dep ) {
    }

    @Override
    public void inject( Component parent ) {
        this.parent = parent;
    }

    /**
     * Start.
     */
    @Override
    public void start() {

        Article chafingDishGN = DS.newArticle("Chafing Dish 1/2 GN", "10");
        chafingDishGN.addNote("65 mm tief, CNS, stapelbar, 375 x 290 x H 390 mm, 2,7 kg, 1 Brennpastenbehälter");

        Article chafingDishTrio = DS.newArticle("Chafing Dish TRIO", "10");
        chafingDishTrio.addNote("max GN-Behältertiefe 65 mm");

        Article suppenSchöpfer = DS.newArticle("Suppenschöpfer, Serie ERGONOM", "10");
        suppenSchöpfer.addNote("aus Edelstahl 18/0, hochglänzend, ergonomisch profilierter Griff aus Edelstahl 18/10, fugenlos");

        Article suppenKelleOval = DS.newArticle("Suppenkelle Oval, Serie Le Buffet", "10");
        suppenKelleOval.addNote("mit schwarzem ABS Griff, Kelle aus Edelstahl 18/10, schwere Qualität");

        Article gnTablett = DS.newArticle("GN-Tablett", "10");
        gnTablett.addNote("Melamin, GN 1/1, Tiefe: 20 mm");

        Article tablettPure = DS.newArticle("Tablett \"PURE\"", "10");
        tablettPure.addNote("Tablett \"PURE\", GN 2/4, Höhe 3 cm, Melamin, schwarz, extrem bruchsicher, glänzende und harte Oberfläche, spülmaschinenfest, spülmaschinenfest, lebensmittelecht, temperaturbeständig -30°c bis +70 °C");

        Article kunsstoffThermo = DS.newArticle("Kunststoff Thermobehälter", "10");
        kunsstoffThermo.addNote("stapelbar, mit Fronttür, 11 Einschübe GN 1/1, hellgrau, 65 x 45 x H 61 cm, H innen 50 cm, ohne Tronsport-Trolly");

        Article doppelThermo = DS.newArticle("Doppel-Thermobehälter", "10");
        doppelThermo.addNote("2 Fronttüren, 20 Einschübe für GN 1/1, rollbar, 2 Räder mit Bremse, dunkelgrau, Innenhöhe 2 x 50 cm, Länge 68 x Breite 48 x Höhe 120 cm");

        Article besteckbehälter = DS.newArticle("Besteckbehälter", "10");
        besteckbehälter.addNote("Edelstahl, ca. 26 x 30 x 20 cm, für 4 Besteckköcher Ø 100 mm x H 130 mm");

        Article tischSetVarioII = DS.newArticle("Tisch Set \"VARIO II\"", "10");
        tischSetVarioII.addNote("22 x 22 x H 26 cm, 4-teilig: 1 Ständer + 3 Behälter (innen (Ø 9 cm) für Besteck, Servietten etc., vielseitig einsetzbar, Melamin/verchromt, spülmaschinenfest");
    }

    @Override
    public void stop() {
    }

    @Override
    public Article findArticleById( String id ) {
        return _data.get( id );
    }

    @Override
    public Collection<Article> findAllArticles() {
        return _data.values();
    }

    @Override
    public Article newArticle( String articleName, String quantity ) {
        Article a = new Article(null, articleName, quantity);
        _data.put( a.getId(), a );
        //save( "created: ", c );
        return a;
    }

    @Override
    public void updateArticle( Article e ) {
        String msg = "updated: ";
        if( e != null ) {
            Article e2 = _data.get( e.getId() );
            if( e != e2 ) {
                if( e2 != null ) {
                    _data.remove( e2.getId() );
                }
                msg = "created: ";
                _data.put( e.getId(), e );
            }
            //save( msg, c );
            System.err.println( msg + e.getId() );
        }
    }

    @Override
    public void deleteArticle( Collection<String> ids ) {
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
