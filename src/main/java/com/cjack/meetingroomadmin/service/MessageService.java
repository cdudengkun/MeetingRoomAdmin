package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AppUserDao;
import com.cjack.meetingroomadmin.dao.MessageDao;
import com.cjack.meetingroomadmin.dao.MessageReadDao;
import com.cjack.meetingroomadmin.model.MessageModel;
import com.cjack.meetingroomadmin.table.AppUserTable;
import com.cjack.meetingroomadmin.table.MessageReadTable;
import com.cjack.meetingroomadmin.table.MessageTable;
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

import static com.cjack.meetingroomadmin.util.CustomerStringUtil.toLikeStr;

@Service
public class MessageService {

    @Autowired
    private MessageDao dao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private MessageReadDao messageReadDao;

    public void list( LayPage layPage, MessageModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        //只返回手动发送的消息
        model.setType( 1);
        Specification<MessageTable> specification = handleConditon( model);
        Pageable pageable = new PageRequest( layPage.getPage() - 1, layPage.getLimit(), new Sort( orders));

        Page<MessageTable> pageTable = dao.findAll( specification, pageable);
        List<MessageModel> datas = new ArrayList<>();
        for( MessageTable table : pageTable.getContent()){
            MessageModel data = ModelUtils.copySignModel( table, MessageModel.class);
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

    public void sendMessage( MessageModel model){
        MessageTable message = ModelUtils.copySignModel( model, MessageTable.class);
        dao.save( message);
        List<AppUserTable> appUserTables = appUserDao.findAll( );
        //保存到message_read表
        List<MessageReadTable> reads = new ArrayList<>();
        for( AppUserTable appUser : appUserTables){
            MessageReadTable read = new MessageReadTable();
            read.setAppUser( appUser);
            read.setCreateTime( System.currentTimeMillis());
            read.setUpdateTime( System.currentTimeMillis());
            read.setMessage( message);
            read.setState( CommonConfig.NO);
            reads.add( read);
        }
        messageReadDao.save( reads);
    }

    private Specification<MessageTable> handleConditon( MessageModel model){
        Specification< MessageTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTitle())){
                predicate.getExpressions().add( cb.like( root.get("title"), toLikeStr( model.getTitle())));
            }
            if( EmptyUtil.isNotEmpty( model.getType())){
                predicate.getExpressions().add( cb.equal( root.get("type"), model.getType()));
            }
            return predicate;
        };
        return specification;
    }

}
