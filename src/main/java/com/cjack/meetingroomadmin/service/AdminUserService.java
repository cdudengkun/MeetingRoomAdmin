package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.AdminRoleDao;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.CityDao;
import com.cjack.meetingroomadmin.exception.AdminUserNotFoundException;
import com.cjack.meetingroomadmin.exception.AdminUserPassErrorException;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AdminUserModel;
import com.cjack.meetingroomadmin.model.UpdatePassWordModel;
import com.cjack.meetingroomadmin.table.AdminRoleTable;
import com.cjack.meetingroomadmin.table.AdminUserTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.Md5Util;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AdminUserService {

    @Autowired
    private AdminUserDao dao;
    @Autowired
    private AdminRoleDao adminRoleDao;
    @Autowired
    private CityDao cityDao;

    public AdminUserModel login( String userName, String passWord) throws AdminUserNotFoundException, AdminUserPassErrorException, CommonException {
        AdminUserTable user = dao.findOneByLoginName( userName);
        if( user == null){
            throw new AdminUserNotFoundException();
        }
        if( !user.getPassWord().equals( passWord)){
            throw new AdminUserPassErrorException();
        }
        user.setLastLoginTime( new Date());
        user.setUpdateTime( new Date());
        dao.save( user);
        return ModelUtils.copySignModel( user, AdminUserModel.class);
    }

    public AdminUserModel getUserInfo( Long id) throws AdminUserNotFoundException {
        AdminUserTable user = dao.findOne( id);
        if( user == null){
            throw new AdminUserNotFoundException();
        }
        AdminUserModel model = ModelUtils.copySignModel( user, AdminUserModel.class);
        model.setLastLoginTime( user.getLastLoginTime());
        if( user.getProvince() != null){
            model.setProvinceId( user.getProvince().getId());
            model.setProvinceName( user.getProvince().getName());
        }
        if( user.getCity() != null){
            model.setCityId( user.getCity().getId());
            model.setCityName( user.getCity().getName());
        }
        if( user.getCountry() != null){
            model.setCountryId( user.getCountry().getId());
            model.setCountryName( user.getCountry().getName());
        }
        if( user.getAdminRole() != null){
            model.setRoleId( user.getAdminRole().getId());
            model.setRoleName( user.getAdminRole().getRoleName());
            model.setRoleContent( user.getAdminRole().getContent());
        }
        return model;
    }

    public void updatePassword( UpdatePassWordModel model) throws Exception {
        AdminUserTable table = dao.getOne( model.getId());
        if( !Md5Util.stringToMD5( model.getOldPassword()).equals( table.getPassWord())){
            throw new Exception();
        }
        table.setPassWord( Md5Util.stringToMD5( model.getNewPassword()));
        table.setUpdateTime( new Date());
        dao.save( table);
    }

    public List<AdminUserModel> list( Long loginUserId, AdminUserModel condition){
        List< Sort.Order> orders = new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<AdminUserTable> specification = handleConditon( loginUserId, condition);
        List<AdminUserTable> userTables = dao.findAll( specification);
        List<AdminUserModel> models = new ArrayList<>();
        for( AdminUserTable userTable : userTables){
            AdminUserModel model = ModelUtils.copySignModel( userTable, AdminUserModel.class);
            models.add( model);
        }
        return models;
    }

    public void del( String ids){
        List<AdminUserTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AdminUserTable table = new AdminUserTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( Long loginUserId, AdminUserModel model){
        AdminUserTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.getOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else {
            table = ModelUtils.copySignModel( model, AdminUserTable.class);
        }
        if( EmptyUtil.isNotEmpty( model.getRoleId())){
            table.setAdminRole( adminRoleDao.getOne( model.getRoleId()));
        }
        if( EmptyUtil.isNotEmpty( model.getProvinceId())){
            table.setProvince( cityDao.getOne( model.getProvinceId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCityId())){
            table.setCity( cityDao.getOne( model.getCityId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCountryId())){
            table.setCountry( cityDao.getOne( model.getCountryId()));
        }
        if( EmptyUtil.isNotEmpty( model.getPassWord())){
            table.setPassWord( Md5Util.stringToMD5( model.getPassWord()));
        }
        AdminUserTable loginUser = dao.getOne( loginUserId);
        dao.save( table);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<AdminUserTable> handleConditon( Long loginUserId, AdminUserModel model){
        Specification< AdminUserTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getRoleId())){
                Join<AdminUserTable, AdminRoleTable> join = root.join("adminRole", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getRoleId()));
            }
            if( EmptyUtil.isNotEmpty( loginUserId)){
                AdminUserTable loginUser = dao.getOne( loginUserId);

            }
            return predicate;
        };
        return specification;
    }
}
