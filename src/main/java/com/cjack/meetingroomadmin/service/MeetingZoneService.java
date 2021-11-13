package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.MeetingZoneDao;
import com.cjack.meetingroomadmin.model.MeetingZoneModel;
import com.cjack.meetingroomadmin.table.MeetingZoneTable;
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
public class MeetingZoneService {

    @Autowired
    private MeetingZoneDao dao;

    public void list(LayPage page, MeetingZoneModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<MeetingZoneTable> specification = handleConditon( model);
        Page<MeetingZoneTable> pageTable = dao.findAll( specification, pageable);
        page.setData( ModelUtils.copyListModel( pageTable.getContent(), MeetingZoneModel.class));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<MeetingZoneTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            MeetingZoneTable table = dao.getOne( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( MeetingZoneModel model){
        dao.save( ModelUtils.copySignModel( model, MeetingZoneTable.class));
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<MeetingZoneTable> handleConditon(MeetingZoneModel model){
        Specification< MeetingZoneTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getDetail())){
                predicate.getExpressions().add( cb.like( root.get("detail"), "%" + model.getDetail() + "%"));
            }
            return predicate;
        };
        return specification;
    }
}
