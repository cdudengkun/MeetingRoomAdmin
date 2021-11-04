package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.RechargeCardDao;
import com.cjack.meetingroomadmin.model.BatchAddRechargeCardModel;
import com.cjack.meetingroomadmin.model.RechargeCardCount;
import com.cjack.meetingroomadmin.model.RechargeCardModel;
import com.cjack.meetingroomadmin.table.AdminUserTable;
import com.cjack.meetingroomadmin.table.RechargeCardTable;
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

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class RechargeCardService {

    @Autowired
    private RechargeCardDao dao;
    @Autowired
    private AdminUserDao adminUserDao;

    public void list( LayPage layPage, RechargeCardModel model){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<RechargeCardTable> specification = handleConditon( model);
        Page<RechargeCardTable> pageTable = dao.findAll( specification, pageable);
        List<RechargeCardModel> datas = new ArrayList<>();
        for( RechargeCardTable card : pageTable.getContent()){
            RechargeCardModel data = ModelUtils.copySignModel( card, RechargeCardModel.class);
            if( card.getAppUser() != null){
                data.setAppUserId( card.getAppUser().getId());
                data.setAppUserName( card.getAppUser().getName());
            }
            if( card.getSchool() != null){
                data.setSchoolId( card.getSchool().getId());
                data.setSchoolName( card.getSchool().getName());
            }
            if( card.getAdminUser() != null){
                data.setAdminUserId( card.getAdminUser().getId());
                data.setAdminUserName( card.getAdminUser().getName());
            }
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<RechargeCardTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            RechargeCardTable table = new RechargeCardTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public RechargeCardCount showCount(){
        RechargeCardCount model = new RechargeCardCount();
        model.setTotal( dao.countTotal());
        model.setActiveTotal( dao.countActiveTotal());
        model.setExpireTotal( dao.countActiveTotal());
        model.setNoActiveTotal( dao.countNoActiveTotal());
        return model;
    }

    public void batchAdd( BatchAddRechargeCardModel model){
        AdminUserTable adminUserTable = adminUserDao.getOne( model.getAdminUserId());
        List<RechargeCardTable> tables = new ArrayList<>( model.getNumber());
        int i = 0;
        while( i++ < model.getNumber()){
            RechargeCardTable table = new RechargeCardTable();
            table.setCreateTime( new Date());
            table.setUpdateTime( new Date());
            table.setAmount( model.getAmount());
            table.setCode( CustomerStringUtil.randomStrOnlyUpperChar( 11));
            table.setDescription( model.getDescription());
            table.setStatus( CommonConfig.NO);
            table.setValidityDay( model.getValidityDay());
            table.setAdminUser( adminUserTable);
            tables.add( table);
        }
        dao.save( tables);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<RechargeCardTable> handleConditon( RechargeCardModel model){
        Specification< RechargeCardTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getStatus())){
                predicate.getExpressions().add( cb.equal( root.get("status"), model.getStatus()));
            }
            if( model.getStartActiveTime() != null){
                predicate.getExpressions().add( cb.greaterThan( root.get( "activeTime").as( Date.class), model.getStartActiveTime()));
            }
            if( model.getEndActiveTime() != null){
                predicate.getExpressions().add( cb.lessThan( root.get( "activeTime").as( Date.class), model.getEndActiveTime()));
            }
            return predicate;
        };
        return specification;
    }
}
