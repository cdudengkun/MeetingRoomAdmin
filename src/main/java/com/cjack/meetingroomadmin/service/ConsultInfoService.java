package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.ConsultInfoDao;
import com.cjack.meetingroomadmin.model.ConsultInfoModel;
import com.cjack.meetingroomadmin.table.ConsultInfoTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultInfoService {

    @Autowired
    private ConsultInfoDao dao;

    public void list( LayPage layPage, ConsultInfoModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<ConsultInfoTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<ConsultInfoTable> pageTable = dao.findAll( specification, pageable);
        List<ConsultInfoModel> datas = new ArrayList<>();
        for( ConsultInfoTable table : pageTable.getContent()){
            ConsultInfoModel data = ModelUtils.copySignModel( table, ConsultInfoModel.class);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<ConsultInfoTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ConsultInfoTable table = new ConsultInfoTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( ConsultInfoModel model){
        ConsultInfoTable message;
        if( EmptyUtil.isNotEmpty( model.getId())){
            message = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, message);
        }else{
            message = ModelUtils.copySignModel( model, ConsultInfoTable.class);
        }
        dao.save( message);
    }

    private Specification<ConsultInfoTable> handleConditon( ConsultInfoModel model){
        Specification< ConsultInfoTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTitle())){
                predicate.getExpressions().add( cb.equal( root.get("title"), model.getTitle()));
            }
            return predicate;
        };
        return specification;
    }

}
