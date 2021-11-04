package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.AppUserDao;
import com.cjack.meetingroomadmin.dao.SchoolBindApplyDao;
import com.cjack.meetingroomadmin.model.SchoolBindApplyModel;
import com.cjack.meetingroomadmin.table.AppUserTable;
import com.cjack.meetingroomadmin.table.SchoolBindApplyTable;
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
import java.util.Date;
import java.util.List;

@Service
public class SchoolBindApplyService {

    @Autowired
    private SchoolBindApplyDao dao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private AppUserDao appUserDao;

    public void list( LayPage layPage, SchoolBindApplyModel model){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<SchoolBindApplyTable> specification = handleConditon( model);
        Page<SchoolBindApplyTable> pageTable = dao.findAll( specification, pageable);
        List<SchoolBindApplyModel> datas = new ArrayList<>();
        for( SchoolBindApplyTable bindApply : pageTable.getContent()){
            SchoolBindApplyModel data = ModelUtils.copySignModel( bindApply, SchoolBindApplyModel.class);
            data.setAppUserId( bindApply.getAppUser().getId());
            data.setAppUserUid( bindApply.getAppUser().getUid());
            data.setAppUserName( bindApply.getAppUser().getName());
            data.setAppUserPhone( bindApply.getAppUser().getPhone());
            data.setSchoolId( bindApply.getSchool().getId());
            data.setSchoolName( bindApply.getSchool().getName());
            if( bindApply.getAdminUser() != null){
                data.setAdminUserId( bindApply.getAdminUser().getId());
                data.setAdminUserName( bindApply.getAdminUser().getName());
            }
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    public void del( String ids){
        List<SchoolBindApplyTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            SchoolBindApplyTable table = new SchoolBindApplyTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void agree( SchoolBindApplyModel model){
        //修改申请状态
        SchoolBindApplyTable table = dao.getOne( model.getId());
        table.setState( CommonConfig.YES);
        table.setAgreeTime( new Date());
        table.setUpdateTime( new Date());
        table.setAdminUser( adminUserDao.getOne( model.getAdminUserId()));
        //将app用户申请的学校设置到他的绑定学校里面去
        AppUserTable appUserTable = table.getAppUser();
        appUserTable.setSchool( table.getSchool());
        appUserDao.save( appUserTable);
        dao.save( table);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<SchoolBindApplyTable> handleConditon( SchoolBindApplyModel model){
        Specification< SchoolBindApplyTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<SchoolBindApplyTable, AppUserTable> join = root.join("appUser", JoinType.LEFT);
            if( EmptyUtil.isNotEmpty( model.getAppUserName())){
                predicate.getExpressions().add( cb.equal( join.get("name"), "%" + model.getAppUserName() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getAppUserPhone())){
                predicate.getExpressions().add( cb.like( join.get("phone"), "%" + model.getAppUserPhone() + "%"));
            }
            return predicate;
        };
        return specification;
    }
}
