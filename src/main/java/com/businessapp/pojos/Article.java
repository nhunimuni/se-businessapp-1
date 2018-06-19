package com.businessapp.pojos;

import com.businessapp.logic.IDGen;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nhunimuni on 25.05.18.
 */
public class Article implements EntityIntf {
    private static final long serialVersionUID = 1L;

    private static IDGen IDG = new IDGen("A.", IDGen.IDTYPE.AIRLINE, 6);

    // Customer states.
    public enum ArticleStatus {
        AVAILABLE, IN_USE, LOST, DAMAGED
    };

    /*
     * Properties.
     */
    private String articleID = null;
    private String articleName = null;
    private String quantity = null;
    private List<LogEntry> notes = new ArrayList<LogEntry>();
    private Article.ArticleStatus status = ArticleStatus.AVAILABLE;

    private ArrayList<Rental> rentals = new ArrayList<Rental>();

    /**
     * Private default constructor (required by JSON deserialization).
     */
    @SuppressWarnings("unused")
    private Article() {
    }

    /**
     * Public constructor
     *
     * @param articleID
     * @param articleName
     * @param quantity
     */
    public Article(String articleID, String articleName, String quantity) {
        this.articleID = articleID == null ? IDG.nextId() : articleID;
        this.articleName = articleName;
        this.quantity = quantity;
        this.notes.add(new LogEntry("Article record created."));
    }

    public void addRentals( Rental rental ) {
        rentals.add( rental );
    }

    public void listRentals()
    {
        for ( Rental lent : rentals )
            System.out.println( lent.getLentArticle() );
    }

    /**
     * Public getter/setter methods.
     */
    public String getId() {
        return articleID;
    }

    public String getArticleName() {
        return articleName;
    }

    public List<String> getNotesAsStringList() {
        List<String> res = new ArrayList<String>();
        for (LogEntry n : notes) {
            res.add(n.toString());
        }
        return res;
    }

    public List<LogEntry> getNotes() {
        return notes;
    }

    public String getQuantity() {
        return quantity;
    }

   /* @JsonGetter("quantity")
    public String quantAsStr() {
        return quantity + "";
    }

    @JsonSetter("quantity")
    public Article setQuantAsString(String quant) {
        setQuantity(Integer.parseInt(quant));
        return this;
    }*/

    public Article.ArticleStatus getStatus() {
        return status;
    }

    public Article setArticleName(String articleName) {
        this.articleName = articleName;
        return this;
    }

    public Article setStatus(Article.ArticleStatus status) {
        this.status = status;
        return this;
    }

    public String setQuantity(String quantity) {
        this.quantity = quantity;
        return quantity;
    }

    public void addNote(String note) {
        notes.add(new LogEntry(note));
    }


}
