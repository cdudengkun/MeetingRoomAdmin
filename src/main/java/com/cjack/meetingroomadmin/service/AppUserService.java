package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.*;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AppUserModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService {

    @Value("${file.word.engDir}")
    private String engDir;
    @Value("${file.word.usaDir}")
    private String usaDir;
    @Value("${file.upload.baseServerDir}")
    String baseServerDir;
    @Value("${file.upload.baseClientDir}")
    String baseClientDir;
    @Value("${file.upload.spelitor}")
    String spelitor;
    @Value("${file.upload.serverUrl}")
    String serverUrl;
    @Autowired
    private AppUserDao dao;
    @Autowired
    private AdminUserDao adminUserDao;

    public void list( LayPage layPage, AppUserModel model, Long adminUserId){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<AppUserTable> specification = handleConditon( model, adminUserId);
        Page<AppUserTable> pageTable = dao.findAll( specification, pageable);
        List<AppUserModel> datas = new ArrayList<>();
        for( AppUserTable appUser : pageTable.getContent()){
            AppUserModel data = ModelUtils.copySignModel( appUser, AppUserModel.class);
            if( appUser.getProvince() != null){
                data.setProvinceId( appUser.getProvince().getId());
                data.setProvinceName( appUser.getProvince().getName());
            }
            if( appUser.getCity() != null){
                data.setCityId( appUser.getCity().getId());
                data.setCityName( appUser.getCity().getName());
            }
            if( appUser.getCountry() != null){
                data.setCountryId( appUser.getCountry().getId());
                data.setCountryName( appUser.getCountry().getName());
            }
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public void del( String ids){
        List<AppUserTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            AppUserTable table = new AppUserTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.save( tables);
    }


    /**
     * 启用禁用
     * @return
     */
    public void updateStatus( AppUserModel model) throws CommonException {
        AppUserTable userTable = dao.getOne( model.getId());
        userTable.setStatus( model.getStatus());
        dao.save( userTable);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<AppUserTable> handleConditon( AppUserModel model, Long loginUserId){
        final AdminUserTable loginUser = adminUserDao.getOne( loginUserId);
        final AdminRoleTable role = loginUser.getAdminRole();
        Specification< AppUserTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( root.get("name"), "%" + model.getName() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getPhone())){
                predicate.getExpressions().add( cb.like( root.get("phone"), "%" + model.getPhone() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getIsVip())){
                Join<AppUserTable, AppUserAccountTable> join = root.join("appUser", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("isVip"), model.getIsVip()));
            }
            return predicate;
        };
        return specification;
    }
}
