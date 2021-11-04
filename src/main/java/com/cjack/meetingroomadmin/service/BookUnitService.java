package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.BookUnitDao;
import com.cjack.meetingroomadmin.table.BookTable;
import com.cjack.meetingroomadmin.table.BookUnitTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.Date;

@Service
public class BookUnitService {

    @Autowired
    private BookUnitDao dao;

    public BookUnitTable getUnit( Long bookId, String unitName){
        Specification<BookUnitTable> specification = handleConditon( bookId, unitName);
        BookUnitTable table = dao.findOne( specification);
        return table;
    }

    //添加单元，单词数为0
    public BookUnitTable addUnit( BookTable book, String unitName){
        BookUnitTable bookUnitTable = new BookUnitTable();
        bookUnitTable.setBook( book);
        bookUnitTable.setName( unitName);
        bookUnitTable.setCreateTime( new Date());
        bookUnitTable.setUpdateTime( new Date());
        bookUnitTable.setWordNum( 0);
        bookUnitTable = dao.save( bookUnitTable);
        return bookUnitTable;
    }

    //设置单元单词数增加
    public BookUnitTable updateUnitWordsNum( BookUnitTable bookUnitTable, Integer wordNum){
        bookUnitTable.setWordNum( wordNum);
        bookUnitTable = dao.save( bookUnitTable);
        return bookUnitTable;
    }


    private Specification<BookUnitTable> handleConditon( Long bookId, String unitName){
        Specification< BookUnitTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<BookUnitTable, BookTable> join = root.join("book", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( join.get( "id"), bookId));
            predicate.getExpressions().add( cb.equal( root.get("name"), unitName));
            return predicate;
        };
        return specification;
    }
}
