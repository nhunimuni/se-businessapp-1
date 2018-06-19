package com.businessapp.DTO;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

import com.businessapp.pojos.Article;
import com.businessapp.pojos.LogEntry;

/**
 * Created by nhunimuni on 05.06.18.
 * Private JSON DTO (Data Access Object) class that is associated with Customer Pojo.
 */
public class ArticleJSON extends Article implements JSONIntf{
    private static final long serialVersionUID = 1L;

    /**
     * Required by JSON de-serialization.
     */
    private ArticleJSON() {
        super( null, null , null);
        this.getNotes().clear();
    }


    /**
     * Public copy constructor to create JSON DTO from original POJO.
     * @param c copied Customer object.
     */
    public ArticleJSON( Article c ) {
        super( c.getId(), c.getArticleName(), c.getQuantity() );
        this.getNotes().clear();
        for( LogEntry le : c.getNotes() ) {
            this.getNotes().add( le );
        }
        this.setStatus( c.getStatus() );
    }

    /**
     * Public method to create original POJO from JSON DTO.
     * @return Customer POJO.
     */
    @JsonIgnore
    public Article getArticle() {
        Article c = new Article( this.getId(), this.getArticleName(), this.getQuantity() );
        c.getNotes().clear();
        for( LogEntry le : this.getNotes() ) {
            c.getNotes().add( le );
        }
        c.setStatus( this.getStatus() );
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
    public Article setNotesAsStringList( String[] notesAsStr ) {
        for( String noteAsStr : notesAsStr ) {
            LogEntry note = new LogEntry( noteAsStr );
            getNotes().add( note );
        }
        return this;
    }

}
