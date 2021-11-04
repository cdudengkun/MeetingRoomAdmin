package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.ArticleDao;
import com.cjack.meetingroomadmin.dao.ArticleParagraphDao;
import com.cjack.meetingroomadmin.model.ArticleParagraphModel;
import com.cjack.meetingroomadmin.table.ArticleParagraphTable;
import com.cjack.meetingroomadmin.table.ArticleTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleParagraphService {

    @Autowired
    private ArticleParagraphDao dao;
    @Autowired
    private ArticleDao articleDao;

    public List<ArticleParagraphModel> list( Long articleId){
        ArticleTable article = articleDao.getOne( articleId);
        return ModelUtils.copyListModel(article.getParagraphs(), ArticleParagraphModel.class);
    }

    public void del( String ids){
        List<ArticleParagraphTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ArticleParagraphTable table = new ArticleParagraphTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( ArticleParagraphModel model){
        ArticleParagraphTable table = new ArticleParagraphTable();
        table.setOriginal( model.getOriginal());
        table.setTranslation( model.getTranslation());
        table.setId( model.getId());
        table.setCreateTime( model.getCreateTime());
        table.setUpdateTime( model.getUpdateTime());
        table.setArticle( articleDao.getOne( model.getArticleId()));
        dao.save( table);
    }
}
