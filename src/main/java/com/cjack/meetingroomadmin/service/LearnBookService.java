package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.dao.LearnBookDao;
import com.cjack.meetingroomadmin.model.LearnBookModel;
import com.cjack.meetingroomadmin.table.AppUserTable;
import com.cjack.meetingroomadmin.table.LearnBookTable;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LearnBookService {

    @Autowired
    private LearnBookDao dao;

    public List<LearnBookModel> list(LearnBookModel model){
        return ModelUtils.copyListModel( dao.findAll(), LearnBookModel.class);
    }

    public LearnBookTable getCurrentBook( Long appUserId){
        Specification<LearnBookTable> specification = handleConditon( appUserId);
        LearnBookTable todayLearnInfoTable = dao.findOne( specification);
        return todayLearnInfoTable;
    }

    public void del( String ids){
        List<LearnBookTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            LearnBookTable table = new LearnBookTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( LearnBookModel model){
        dao.save( ModelUtils.copySignModel( model, LearnBookTable.class));
    }

    /**
     * 组装查询条件
     * @param appUserId
     * @return
     */
    private Specification<LearnBookTable> handleConditon( Long appUserId){
        Specification< LearnBookTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<LearnBookTable, AppUserTable> join = root.join("appUser", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( join.get("id"), appUserId));
            predicate.getExpressions().add( cb.equal( root.get("currentBook"), CommonConfig.YES));
            return predicate;
        };
        return specification;
    }
}
