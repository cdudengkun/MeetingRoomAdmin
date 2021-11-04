package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.*;
import com.cjack.meetingroomadmin.exception.JPushException;
import com.cjack.meetingroomadmin.model.MessageModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageDao dao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private SchoolDao schoolDao;
    @Autowired
    private SchoolClassDao schoolClassDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private MessageReadDao messageReadDao;
    @Autowired
    private JpushService jpushService;

    public void list( LayPage layPage, MessageModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<MessageTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), new Sort( orders));

        Page<MessageTable> pageTable = dao.findAll( specification, pageable);
        List<MessageModel> datas = new ArrayList<>();
        for( MessageTable table : pageTable.getContent()){
            MessageModel data = ModelUtils.copySignModel( table, MessageModel.class);
            if( table.getSchool() != null){
                data.setSchoolId( table.getSchool().getId());
                data.setSchoolName( table.getSchool().getName());
            }
            if( table.getAdminUser() != null){
                data.setAdminUserId( table.getAdminUser().getId());
                data.setAdminUserName( table.getAdminUser().getName());
            }
            if( table.getSchoolClass() != null){
                data.setSchoolClassId( table.getSchoolClass().getId());
                data.setSchoolClassName( table.getSchoolClass().getName());
            }
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<MessageTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            MessageTable table = new MessageTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( MessageModel model) throws JPushException {
        MessageTable message;
        if( EmptyUtil.isNotEmpty( model.getId())){
            message = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, message);
        }else{
            message = ModelUtils.copySignModel( model, MessageTable.class);
            message.setAdminUser( adminUserDao.getOne( model.getAdminUserId()));
        }
        if( EmptyUtil.isNotEmpty( model.getSchoolClassId())){
            message.setSchoolClass( schoolClassDao.getOne( model.getSchoolClassId()));
        }
        if( EmptyUtil.isNotEmpty( model.getSchoolId())){
            message.setSchool( schoolDao.getOne( model.getSchoolId()));
        }
        dao.save( message);
        List<AppUserTable> appUserTables;
        if( model.getType().equals( 3)){
            Specification<AppUserTable> specification = handleConditon( model.getSchoolClassId());
            appUserTables = appUserDao.findAll( specification);
        }else{
            //查询系统所有app用户
            appUserTables = appUserDao.findAll();
        }
        //保存到message_read表
        List<MessageReadTable> reads = new ArrayList<>();
        for( AppUserTable appUser : appUserTables){
            MessageReadTable read = new MessageReadTable();
            read.setAppUser( appUser);
            read.setCreateTime( new Date());
            read.setUpdateTime( new Date());
            read.setMessage( message);
            read.setState( CommonConfig.NO);
            reads.add( read);
        }
        messageReadDao.save( reads);/*
        //调用极光推送
        switch ( model.getType()){
            case 1://系统消息
                JsonObject jsonObject =new JsonObject() ;
                jsonObject.addProperty( "id", message.getId());
                jsonObject.addProperty( "type", 0);
                jpushService.pushForAll( message.getContent(), message.getTitle(), jsonObject);
                break;
            case 2://学校消息
                //暂时屏蔽
                break;
            case 3://班级消息
                jpushService.pushForTag( message.getTitle(), message.getContent(), String.valueOf( model.getSchoolClassId()));
                break;
        }*/
    }

    private Specification<AppUserTable> handleConditon( Long schoolClassId){
        Specification< AppUserTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( schoolClassId)){
                Join<AppUserTable, SchoolClassTable> join = root.join("schoolClass", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), schoolClassId));
            }
            return predicate;
        };
        return specification;
    }

    private Specification<MessageTable> handleConditon( MessageModel condition){
        final AdminUserTable loginUser = adminUserDao.getOne( condition.getAdminUserId());
        Specification< MessageTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            AdminRoleTable role = loginUser.getAdminRole();
            //修改一下，就算是超级管理员，也只能查看自己发送的消息
            /*
            if( !role.getCode().equals( PrivageConfig.SUPER_ADMIN_CODE)){

            }*/
            Join<MessageTable, AdminUserTable> join = root.join("adminUser", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( join.get("id"), condition.getAdminUserId()));
            return predicate;
        };
        return specification;
    }
}
