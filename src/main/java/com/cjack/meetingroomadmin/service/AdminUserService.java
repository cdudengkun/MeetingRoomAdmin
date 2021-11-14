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
import java.util.List;
import static com.cjack.meetingroomadmin.util.CustomerStringUtil.*;

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
        if( !user.getPassWord().equals( Md5Util.stringToMD5( passWord))){
            throw new AdminUserPassErrorException();
        }
        user.setLastLoginTime( System.currentTimeMillis());
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
            model.setProvinceId( user.getProvince().getAreaCode());
            model.setProvinceName( user.getProvince().getName());
        }
        if( user.getCity() != null){
            model.setCityId( user.getCity().getAreaCode());
            model.setCityName( user.getCity().getName());
        }
        if( user.getCounty() != null){
            model.setCountyId( user.getCounty().getAreaCode());
            model.setCountyName( user.getCounty().getName());
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
        dao.save( table);
    }

    public List<AdminUserModel> list( AdminUserModel condition){
        List< Sort.Order> orders = new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Specification<AdminUserTable> specification = handleConditon( condition);
        List<AdminUserTable> userTables = dao.findAll( specification);
        List<AdminUserModel> models = new ArrayList<>();
        for( AdminUserTable userTable : userTables){
            AdminUserModel model = ModelUtils.copySignModel( userTable, AdminUserModel.class);
            //超管无法被编辑
            if( model.getId().equals( 1l)){
                continue;
            }
            AdminRoleTable adminRole = userTable.getAdminRole();
            model.setRoleId( adminRole.getId());
            model.setRoleName( adminRole.getRoleName());
            model.setRoleContent( adminRole.getContent());
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

    public void save( AdminUserModel model){
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
            table.setProvince( cityDao.getByAreaCode( model.getProvinceId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCityId())){
            table.setCity( cityDao.getByAreaCode( model.getCityId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCountyId())){
            table.setCounty( cityDao.getByAreaCode( model.getCountyId()));
        }
        if( EmptyUtil.isNotEmpty( model.getPassWord())){
            table.setPassWord( Md5Util.stringToMD5( model.getPassWord()));
        }

        dao.save( table);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<AdminUserTable> handleConditon( AdminUserModel model){
        Specification< AdminUserTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getRoleId())){
                Join<AdminUserTable, AdminRoleTable> join = root.join("adminRole", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getRoleId()));
            }
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( root.get("name"), toLikeStr( model.getName())));
            }
            if( EmptyUtil.isNotEmpty( model.getPhone())){
                predicate.getExpressions().add( cb.like( root.get("phone"), toLikeStr( model.getPhone())));
            }
            if( EmptyUtil.isNotEmpty( model.getEmail())){
                predicate.getExpressions().add( cb.like( root.get("email"), toLikeStr( model.getEmail())));
            }
            return predicate;
        };
        return specification;
    }
}
