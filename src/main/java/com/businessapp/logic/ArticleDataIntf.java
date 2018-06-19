package com.businessapp.logic;

import com.businessapp.ControllerIntf;
import com.businessapp.pojos.Article;
import com.businessapp.pojos.Customer;

import java.util.Collection;

/**
 * Created by nhunimuni on 25.05.18.
 */
public interface ArticleDataIntf extends ControllerIntf{

    /**
     * Factory method that returns a Customer data source.
     * @return new instance of Customer data source.
     */
    public static ArticleDataIntf getController() {
        return new ArticleDataMockImpl();
    }

    /**
     * Public access methods to Customer data.
     */
    Article findArticleById(String id );

    public Collection<Article> findAllArticles();

    public Article newArticle( String articleName, String quantity );

    public void updateArticle( Article c );

    public void deleteArticle( Collection<String> ids);
}
