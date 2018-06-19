package com.businessapp.logic;
import com.businessapp.Component;
import com.businessapp.ControllerIntf;
import com.businessapp.persistence.GenericEntityContainer;
import com.businessapp.persistence.PersistenceProviderIntf;
import com.businessapp.pojos.Article;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by nhunimuni on 04.06.18.
 */
public class ArticleDataSource implements ArticleDataIntf {
    private final GenericEntityContainer<Article> articles;
    private PersistenceProviderIntf persistenceProvider = null;
    /**
     * Factory method that returns a CatalogItem data source.
     * @return new instance of data source.
     */
    public static ArticleDataIntf getController( String name, PersistenceProviderIntf persistenceProvider ) {
        ArticleDataIntf cds = new ArticleDataSource( name );
        cds.inject( persistenceProvider );
        return cds;
    }
    /**
     * Private constructor.
     */
    private ArticleDataSource( String name ) {
        this.articles = new GenericEntityContainer<Article>( name, Article.class );
    }
    @Override
    public void inject( ControllerIntf dep ) {
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
                persistenceProvider.loadInto( articles.getId(), entity -> {
                    this.articles.store( (Article)entity );
                    return true;
                });
            } catch( IOException e ) {
                System.out.print( ", " );
                System.err.print( "No data: " + articles.getId() );
                /*‐‐‐ BEGIN ‐‐‐ */
                ArticleDataIntf mockDS = new ArticleDataMockImpl();
                Component parent = new Component( articles.getId(), null, null );
                mockDS.inject( parent );
                mockDS.start();
                for( Article mockArticles : mockDS.findAllArticles() ) {
                    articles.update( mockArticles );
                }
                persistenceProvider.save( articles, articles.getId() );
                /*‐‐‐ END ‐‐‐ */
            }
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public Article findArticleById(String id) {
        return articles.findById(id);
    }

    @Override
    public Collection<Article> findAllArticles() {
        return articles.findAll();
    }

    @Override
    public Article newArticle(String articleName, String quantity) {
        Article a = new Article( null, articleName, quantity);
        articles.update( a );
        if( persistenceProvider != null ) {
            persistenceProvider.save( articles, articles.getId() );
        }
        return a;
    }

    @Override
    public void updateArticle(Article a) {
        articles.update(a);
        if( persistenceProvider != null ) {
            persistenceProvider.save( articles, articles.getId() );
        }
    }

    @Override
    public void deleteArticle(Collection<String> ids) {
        articles.delete(ids);
        if( persistenceProvider != null ) {
            persistenceProvider.save( articles, articles.getId() );
        }

    }
// TODO: remaining methods of CustomerDataIntf.java
}