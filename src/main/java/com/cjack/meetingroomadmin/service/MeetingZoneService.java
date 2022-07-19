package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AppUserOrderDao;
import com.cjack.meetingroomadmin.dao.CityDao;
import com.cjack.meetingroomadmin.dao.MeetingZoneDao;
import com.cjack.meetingroomadmin.dao.WorkStationReservationDao;
import com.cjack.meetingroomadmin.model.MeetingZoneModel;
import com.cjack.meetingroomadmin.table.MeetingZoneTable;
import com.cjack.meetingroomadmin.util.CustomerStringUtil;
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

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class MeetingZoneService {

    @Autowired
    private MeetingZoneDao dao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private MeetingRoomService meetingRoomService;
    @Autowired
    private AppUserOrderDao appUserOrderDao;
    @Autowired
    private WorkStationReservationDao workStationReservationDao;

    public void list(LayPage page, MeetingZoneModel condition){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<MeetingZoneTable> specification = handleConditon( condition);
        Page<MeetingZoneTable> pageTable = dao.findAll( specification, pageable);
        List<MeetingZoneModel> models = new ArrayList<>();
        for( MeetingZoneTable table : pageTable.getContent()){
            MeetingZoneModel model = ModelUtils.copySignModel( table, MeetingZoneModel.class);
            if( table.getProvince() != null){
                model.setProvinceId( table.getProvince().getAreaCode());
                model.setProvinceName( table.getProvince().getName());
            }
            if( table.getCity() != null){
                model.setCityId( table.getCity().getAreaCode());
                model.setCityName( table.getCity().getName());
            }
            if( table.getCounty() != null){
                model.setCountyId( table.getCounty().getAreaCode());
                model.setCountyName( table.getCounty().getName());
            }
            models.add( model);
        }
        page.setData( models);
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public List<MeetingZoneModel> listAll( MeetingZoneModel condition){
        Specification<MeetingZoneTable> specification = handleConditon( condition);
        List<MeetingZoneTable> tables = dao.findAll( specification);
        List<MeetingZoneModel> models = new ArrayList<>();
        for( MeetingZoneTable table : tables){
            MeetingZoneModel model = ModelUtils.copySignModel( table, MeetingZoneModel.class);
            if( table.getProvince() != null){
                model.setProvinceId( table.getProvince().getAreaCode());
                model.setProvinceName( table.getProvince().getName());
            }
            if( table.getCity() != null){
                model.setCityId( table.getCity().getAreaCode());
                model.setCityName( table.getCity().getName());
            }
            if( table.getCounty() != null){
                model.setCountyId( table.getCounty().getAreaCode());
                model.setCountyName( table.getCounty().getName());
            }
            models.add( model);
        }
        return models;
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<MeetingZoneTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            MeetingZoneTable table = dao.getOne( Long.valueOf( id));
            if( !CollectionUtils.isEmpty( table.getOrders())){
                appUserOrderDao.deleteInBatch( table.getOrders());
                appUserOrderDao.flush();
            }
            if( !CollectionUtils.isEmpty( table.getWorkStationReservations())){
                workStationReservationDao.deleteInBatch( table.getWorkStationReservations());
                workStationReservationDao.flush();
            }
            meetingRoomService.del( table.getMeetingRooms());
            dao.delete( table);
        }
    }

    public void save( MeetingZoneModel model){
        MeetingZoneTable table = null;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, MeetingZoneTable.class);
        }
        if( EmptyUtil.isNotEmpty( model.getProvinceId())){
            table.setProvince( cityDao.getByAreaCode( model.getProvinceId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCityId())){
            table.setCity( cityDao.getByAreaCode( model.getCityId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCountyId())){
            table.setCounty( cityDao.getByAreaCode( model.getCountyId()));
        }
        dao.save( table);
    }

    public void uploadImg( MeetingZoneModel model){
        MeetingZoneTable table = dao.findOne( model.getId());
        if( EmptyUtil.isNotEmpty( table.getImgs())){
            if( !table.getImgs().contains( model.getImg())){
                table.setImgs( table.getImgs() + "," + model.getImg());
            }
        }else{
            table.setImgs( model.getImg());
        }

        dao.save( table);
    }

    public void delImg( MeetingZoneModel model){
        MeetingZoneTable table = dao.findOne( model.getId());
        if( EmptyUtil.isNotEmpty( table.getImgs()) && EmptyUtil.isNotEmpty( model.getImgs())){
           for( String img : model.getImgs().split( ",")){
               table.setImgs( table.getImgs().replace( img + ",", ""));
           }
        }
        dao.save( table);
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
                predicate.getExpressions().add( cb.like( root.get("detail"), CustomerStringUtil.toLikeStr( model.getDetail())));
            }
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( root.get("name"), CustomerStringUtil.toLikeStr( model.getName())));
            }
            return predicate;
        };
        return specification;
    }
}
