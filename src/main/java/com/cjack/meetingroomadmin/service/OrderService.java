package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AppUserOrderDao;
import com.cjack.meetingroomadmin.model.*;
import com.cjack.meetingroomadmin.table.AppUserOrderTable;
import com.cjack.meetingroomadmin.table.AppUserTable;
import com.cjack.meetingroomadmin.util.CustomerStringUtil;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.MathUtil;
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
import java.util.Date;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private AppUserOrderDao dao;

    public void list( LayPage page, AppUserOrderModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<AppUserOrderTable> specification = handleConditon( model);
        Page<AppUserOrderTable> pageTable = dao.findAll( specification, pageable);
        List<AppUserOrderModel> datas = new ArrayList<>();
        for( AppUserOrderTable table : pageTable.getContent()){
            AppUserTable appUser = table.getAppUser();
            AppUserOrderModel data = ModelUtils.copySignModel( table, AppUserOrderModel.class);
            data.setAppUserModel( ModelUtils.copySignModel( table.getAppUser(), AppUserModel.class));
            if( table.getAppUserCoupon() != null){
                data.setCouponModel( ModelUtils.copySignModel( table.getAppUserCoupon().getCoupon(), CouponModel.class));
            }

            data.setMeetingZoneModel( ModelUtils.copySignModel( table.getMeetingZone(), MeetingZoneModel.class));
            if( table.getMeetingRoom() != null){
                MeetingRoomReservationModel meetingRoomReservationModel = ModelUtils.copySignModel( table.getMeetingRoom().getMeetingRoom(), MeetingRoomReservationModel.class);
                meetingRoomReservationModel.setStartTime( table.getMeetingRoom().getStartTime());
                meetingRoomReservationModel.setEndTime( table.getMeetingRoom().getEndTime());
                meetingRoomReservationModel.setHour( table.getMeetingRoom().getHour());
                meetingRoomReservationModel.setName( table.getMeetingRoom().getName());
                meetingRoomReservationModel.setPhone( table.getMeetingRoom().getPhone());

                ModelUtils.copySignModel( table.getMeetingRoom(), meetingRoomReservationModel);
                data.setMeetingRoomReservationModel( meetingRoomReservationModel);
            }

            if( table.getWorkStation() != null){
                WorkStationReservationModel workStationReservationModel = ModelUtils.copySignModel(table.getWorkStation(), WorkStationReservationModel.class);

                data.setWorkStationReservationModel( workStationReservationModel);
            }

            data.setRecommender( appUser.getRecommender());
            data.setVipExpireTime( appUser.getAppUserAccount().getVipExpireTime() == null ? System.currentTimeMillis() : appUser.getAppUserAccount().getVipExpireTime());
            data.setAmount( MathUtil.divide( data.getAmount(), 100));
            data.setPayAmount( MathUtil.divide( data.getPayAmount(), 100));
            datas.add( data);
        }
        page.setData( datas);
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public List<OrderStatementModel> statement( OrderStatementQueryModel condition){

        return null;
    }


    public void updateOrderStatus( AppUserOrderModel model){
        AppUserOrderTable order = dao.getOne( model.getId());
        order.setStatus( model.getStatus());
        order.setMemo( model.getMemo());
        dao.save( order);
    }

    public Integer queryTradeMount( Date date){
        return dao.queryTradeMount( date.getTime());
    }

    public Integer queryTradeMountWeek( Date date){
        return dao.queryTradeMountWeek( date.getTime());
    }

    public Integer queryTradeMountMonth( Date date){
        return dao.queryTradeMountMonth( date.getTime());
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<AppUserOrderTable> handleConditon( AppUserOrderModel model ){
        Specification< AppUserOrderTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getStatus())){
                predicate.getExpressions().add( cb.equal( root.get("status"), model.getStatus()));
            }
            if( EmptyUtil.isNotEmpty( model.getPayTimeStart())){
                predicate.getExpressions().add( cb.greaterThan( root.get("payTime"), model.getPayTimeStart()));
            }
            if( EmptyUtil.isNotEmpty( model.getPayTimeEnd())){
                predicate.getExpressions().add( cb.lessThan( root.get("payTime"), model.getPayTimeEnd()));
            }
            Join<AppUserOrderTable, AppUserTable> join = root.join("appUser", JoinType.LEFT);
            if( EmptyUtil.isNotEmpty( model.getRecommender())){
                predicate.getExpressions().add( cb.like( join.get("recommender"), CustomerStringUtil.toLikeStr( model.getRecommender())));
            }
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( join.get("name"), CustomerStringUtil.toLikeStr( model.getName())));
            }
            if( EmptyUtil.isNotEmpty( model.getPhone())){
                predicate.getExpressions().add( cb.like( join.get("phone"), CustomerStringUtil.toLikeStr( model.getPhone())));
            }
            return predicate;
        };
        return specification;
    }
}
