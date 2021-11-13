package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.CouponDao;
import com.cjack.meetingroomadmin.model.CouponModel;
import com.cjack.meetingroomadmin.model.MeetingRoomModel;
import com.cjack.meetingroomadmin.table.CouponTable;
import com.cjack.meetingroomadmin.table.MeetingRoomTable;
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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponDao dao;

    public void list(LayPage page, CouponModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<CouponTable> specification = handleConditon( model);
        Page<CouponTable> pageTable = dao.findAll( specification, pageable);
        page.setData( ModelUtils.copyListModel( pageTable.getContent(), MeetingRoomModel.class));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<CouponTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            CouponTable table = dao.getOne( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( CouponModel model){
        dao.save( ModelUtils.copySignModel( model, CouponTable.class));
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<CouponTable> handleConditon(CouponModel model){
        Specification< CouponTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getMeetingZoneId())){
                Join<MeetingZoneTable, MeetingRoomTable> join = root.join("meetingZone", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getMeetingZoneId()));
            }
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.equal( root.get("name"), model.getName()));
            }
            return predicate;
        };
        return specification;
    }
}
