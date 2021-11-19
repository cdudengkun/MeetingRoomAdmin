package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.DefineSignValueDao;
import com.cjack.meetingroomadmin.dao.MeetingRoomDao;
import com.cjack.meetingroomadmin.dao.MeetingZoneDao;
import com.cjack.meetingroomadmin.model.MeetingRoomModel;
import com.cjack.meetingroomadmin.model.MeetingZoneModel;
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
import java.util.stream.Collectors;

@Service
public class MeetingRoomService {

    @Autowired
    private MeetingRoomDao dao;
    @Autowired
    private MeetingZoneDao meetingZoneDao;
    @Autowired
    private DefineSignValueDao defineSignValueDao;

    public void list(LayPage page, MeetingRoomModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<MeetingRoomTable> specification = handleConditon( model);
        Page<MeetingRoomTable> pageTable = dao.findAll( specification, pageable);
        page.setData( pageTable.getContent().stream().map( e->{
            MeetingRoomModel m = ModelUtils.copySignModel( e, MeetingRoomModel.class);
            m.setMeetingZoneModel( ModelUtils.copySignModel( e.getMeetingZone(), MeetingZoneModel.class));
            m.setMeetingZoneId( e.getMeetingZone().getId());
            m.setLevelName( e.getLevel().getDataKey());
            m.setLevelId( e.getLevel().getId());
            return m;
        }).collect(Collectors.toList()));
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<MeetingRoomTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            MeetingRoomTable table = dao.getOne( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( MeetingRoomModel model){
        MeetingRoomTable table = null;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, MeetingRoomTable.class);
        }
        table.setLevel( defineSignValueDao.getOne( model.getLevelId()));
        table.setMeetingZone( meetingZoneDao.getOne( model.getMeetingZoneId()));
        dao.save( table);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<MeetingRoomTable> handleConditon(MeetingRoomModel model){
        Specification< MeetingRoomTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getMeetingZoneId())){
                Join<MeetingZoneTable, MeetingRoomTable> join = root.join("meetingZone", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getMeetingZoneId()));
            }
            if( EmptyUtil.isNotEmpty( model.getStatus())){
                predicate.getExpressions().add( cb.equal( root.get("status"), model.getStatus()));
            }
            return predicate;
        };
        return specification;
    }
}
