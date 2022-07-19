package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AppUserCouponDao;
import com.cjack.meetingroomadmin.dao.AppUserDao;
import com.cjack.meetingroomadmin.dao.CouponDao;
import com.cjack.meetingroomadmin.dao.EnterpriseServiceTypeDao;
import com.cjack.meetingroomadmin.model.CouponModel;
import com.cjack.meetingroomadmin.model.MessageModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.DateFormatUtil;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CouponService {

    @Autowired
    private CouponDao dao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private EnterpriseServiceTypeDao typeDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private AppUserCouponDao appUserCouponDao;

    public void list(LayPage page, CouponModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<CouponTable> specification = handleConditon( model);
        Page<CouponTable> pageTable = dao.findAll( specification, pageable);
        List<CouponModel> datas = new ArrayList<>();
        for( CouponTable table : pageTable.getContent()){
            CouponModel data = ModelUtils.copySignModel( table, CouponModel.class);
            if( table.getType() != null){
                data.setTypeId( table.getType().getId());
                data.setTypeName( table.getType().getName());
            }
            if( !CollectionUtils.isEmpty( table.getDrawedCoupons())){
                data.setDrawedCount( table.getDrawedCoupons().size());
            }else{
                data.setDrawedCount( 0);
            }
            datas.add( data);
        }
        page.setData( datas);
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //将关联了这个type的数据的type设置为null
    public void delType( Long typeId){
        dao.updateType( typeId);
    }

    public List<CouponModel> summary( CouponModel model) {
        List<Object[]> columns = dao.sumViewCountByType();
        List<CouponModel> datas = new ArrayList<>( columns.size());
        for( Object[] column : columns){
            CouponModel data = new CouponModel();
            data.setTypeName( typeDao.getOne( Long.valueOf( column[0].toString())).getName());
            data.setViewCount( ((BigDecimal) column[1]).intValue());
            datas.add( data);
        }
        return datas;
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
        CouponTable table = null;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, CouponTable.class);
        }
        if( EmptyUtil.isNotEmpty( model.getTypeId())){
            table.setType( typeDao.getOne( model.getTypeId()));
        }else{
            table.setType( null);
        }
        dao.save( table);
    }

    public void publish( CouponModel model){
        CouponTable table = dao.findOne( model.getId());
        table.setStatus( 2);
        dao.save( table);
        //给每个人发送优惠券
        if( table.getModel().equals( 1)){
            List<AppUserTable> appUserTables = appUserDao.findAll( );
            for( AppUserTable appUserTable : appUserTables){
                AppUserCouponTable appUserCouponTable = new AppUserCouponTable();
                appUserCouponTable.setCoupon( table);
                appUserCouponTable.setAppUser( appUserTable);
                appUserCouponTable.setCreateTime( System.currentTimeMillis());
                appUserCouponTable.setStatus( 2);
                appUserCouponTable.setEndTime( table.getEndTime());
                appUserCouponTable.setUpdateTime( System.currentTimeMillis());
                appUserCouponDao.save( appUserCouponTable);
            }
        }
        //发送通知消息
        MessageModel messageModel = new MessageModel();
        messageModel.setType( 2);
        messageModel.setTitle( "优惠券领取通知");
        messageModel.setContent( "系统给您发送了优惠券[" + table.getName() + "]，有效期["
                + DateFormatUtil.format( new Date( table.getStartTime()), DateFormatUtil.DATE_RULE_2) + "至"
                + DateFormatUtil.format( new Date( table.getEndTime()), DateFormatUtil.DATE_RULE_2)  + "]");
        messageService.sendMessage( messageModel);
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
            if( EmptyUtil.isNotEmpty( model.getTypeId())){
                Join<EnterpriseSupportTable, EnterpriseServiceTypeTable> join = root.join("type", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getTypeId()));
            }
            return predicate;
        };
        return specification;
    }
}
