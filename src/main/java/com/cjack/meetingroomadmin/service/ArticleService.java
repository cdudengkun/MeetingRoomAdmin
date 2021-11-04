package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.ArticleDao;
import com.cjack.meetingroomadmin.dao.ArticleParagraphDao;
import com.cjack.meetingroomadmin.model.ArticleModel;
import com.cjack.meetingroomadmin.table.ArticleTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao dao;
    @Autowired
    private ArticleParagraphDao articleParagraphDao;

    public void list(LayPage page){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Page<ArticleTable> pageTable = dao.findAll( pageable);
        page.setData( ModelUtils.copyListModel( pageTable.getContent(), ArticleModel.class));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<ArticleTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ArticleTable table = dao.getOne( Long.valueOf( id));
            if( table.getParagraphs() != null){
                articleParagraphDao.delete( table.getParagraphs());
            }
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( ArticleModel model){
        dao.save( ModelUtils.copySignModel( model, ArticleTable.class));
    }
}
