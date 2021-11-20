package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.AppUserDao;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AppUserAccountModel;
import com.cjack.meetingroomadmin.model.AppUserModel;
import com.cjack.meetingroomadmin.table.AdminRoleTable;
import com.cjack.meetingroomadmin.table.AdminUserTable;
import com.cjack.meetingroomadmin.table.AppUserAccountTable;
import com.cjack.meetingroomadmin.table.AppUserTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import static com.cjack.meetingroomadmin.util.CustomerStringUtil.toLikeStr;

@Service
public class AppUserService {

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
            if( appUser.getCounty() != null){
                data.setCountyId( appUser.getCounty().getId());
                data.setCountyName( appUser.getCounty().getName());
            }
            data.setAccountModel( ModelUtils.copySignModel( appUser.getAppUserAccount(), AppUserAccountModel.class));
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
    public void update( AppUserModel model) throws CommonException {
        AppUserTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, AppUserTable.class);
        }
        dao.save( table);
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
                predicate.getExpressions().add( cb.like( root.get("name"), toLikeStr(  model.getName())));
            }
            if( EmptyUtil.isNotEmpty( model.getPhone())){
                predicate.getExpressions().add( cb.like( root.get("phone"), toLikeStr(  model.getPhone())));
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
