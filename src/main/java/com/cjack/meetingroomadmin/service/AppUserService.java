package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.*;
import com.cjack.meetingroomadmin.excel.AppUserUidExcel;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.exception.JPushException;
import com.cjack.meetingroomadmin.model.AppUserModel;
import com.cjack.meetingroomadmin.model.BatchAddAppUserModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
    private SchoolDao schoolDao;
    @Autowired
    private SchoolClassDao schoolClassDao;
    @Autowired
    private CityDao cityDao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private AppUserAccountDao appUserAccountDao;
    @Autowired
    private JpushService jpushService;

    public void list( LayPage layPage, AppUserModel model, Long adminUserId){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<AppUserTable> specification = handleConditon( model, adminUserId);
        Page<AppUserTable> pageTable = dao.findAll( specification, pageable);
        List<AppUserModel> datas = new ArrayList<>();
        for( AppUserTable appUser : pageTable.getContent()){
            AppUserModel data = ModelUtils.copySignModel( appUser, AppUserModel.class);
            if( appUser.getSchoolClass() != null){
                data.setSchoolClassName( appUser.getSchoolClass().getName());
                data.setSchoolClassId( appUser.getSchoolClass().getId());
            }
            if( appUser.getSchool() != null){
                data.setSchoolId( appUser.getSchool().getId());
                data.setSchoolName( appUser.getSchool().getName());
            }
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
            data.setBookNumber( appUser.getLearnBooks().size());
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

    public void save( AppUserModel model) throws JPushException {
        AppUserTable appUserTable;
        if( EmptyUtil.isNotEmpty( model.getId())){
            appUserTable = dao.getOne( model.getId());
        }else{
            appUserTable = new AppUserTable();
        }
        ModelUtils.copySignModel( model, appUserTable);
        if( EmptyUtil.isNotEmpty( model.getSchoolId())){
            appUserTable.setSchool( schoolDao.getOne( model.getSchoolId()));
        }
        if( EmptyUtil.isNotEmpty( model.getSchoolId())){
            appUserTable.setSchool( schoolDao.getOne( model.getSchoolId()));
        }
        if( EmptyUtil.isNotEmpty( model.getSchoolClassId())){
            appUserTable.setSchoolClass( schoolClassDao.getOne( model.getSchoolClassId()));
        //    jpushService.addUserToTag( String.valueOf( model.getSchoolClassId()), model.getPhone());
        }
        if( EmptyUtil.isNotEmpty( model.getProvinceId())){
            appUserTable.setProvince( cityDao.getOne( model.getProvinceId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCityId())){
            appUserTable.setCity( cityDao.getOne( model.getCityId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCountryId())){
            appUserTable.setCountry( cityDao.getOne( model.getCountryId()));
        }
        dao.save( appUserTable);
    }

    /**
     * 校长通过uid添加学员，
     * @return
     */
    public void assignSchool( String uid, Long loginUserId) throws CommonException {
        //校长和学校一对一，这里通过查询到校长的学校
        AdminUserTable adminUserTable = adminUserDao.getOne( loginUserId);
        if( adminUserTable.getSchool() == null){
            throw new CommonException( "您名下没有学校，分配学员失败");
        }
        AppUserTable appUserTable = dao.getByUid( uid);
        if( appUserTable.getSchool() != null){
            throw new CommonException( "学员已经加入其它学校");
        }
        appUserTable.setSchool( adminUserTable.getSchool());
        dao.save( appUserTable);
    }

    /**
     * 从学校中删除学员
     * @return
     */
    public void delFromSchool( AppUserModel model) {
        AppUserTable appUserTable = dao.getOne( model.getId());
        appUserTable.setSchool( null);
        dao.save( appUserTable);
    }

    /**
     * 从学校中删除学员
     * @return
     */
    public void delFromClass( AppUserModel model) throws JPushException {
        AppUserTable appUserTable = dao.getOne( model.getId());
        appUserTable.setSchoolClass( null);
        dao.save( appUserTable);
    //    jpushService.delUserToTag( String.valueOf( model.getSchoolClassId()), model.getPhone());
    }

    /**
     * 批量生成学员
     * @return
     */
    public void batchAdd( BatchAddAppUserModel model) {
        SchoolTable school = schoolDao.getOne( model.getSchoolId());
        for( int i = 0; i < model.getNumber(); i++){
            AppUserTable appUserTable = new AppUserTable();
            appUserTable.setSchool( school);
            appUserTable.setCreateTime( new Date());
            appUserTable.setUpdateTime( new Date());
            //生成11位uid
            String uid = CustomerStringUtil.randomNumberStr( 11);
            appUserTable.setUid( uid);
            appUserTable.setName( uid);
            appUserTable.setRegistType( 1);
            appUserTable.setSex( 1);
            appUserTable.setTryOuted( 2);
            appUserTable.setPassword( Md5Util.md5Encode( model.getPassword()));
            dao.save( appUserTable);

            AppUserAccountTable appUserAccountTable = new AppUserAccountTable();
            appUserAccountTable.setCreateTime( new Date());
            appUserAccountTable.setUpdateTime( new Date());
            //新用户默认赠送50个金币
            appUserAccountTable.setGoldCoin( 0d);
            appUserAccountTable.setIntegral( 0);
            appUserAccountTable.setLevel( 1);
            appUserAccountTable.setAppUser( appUserTable);
            appUserAccountDao.save( appUserAccountTable);
        }
    }

    /**
     * 批量导出通过批量生成的学员
     * @return
     */
    public String batchExportUid( String ids) throws IOException {
        List<AppUserUidExcel> excelRows = new ArrayList<>();
        if( EmptyUtil.isNotEmpty( ids)){
            String[] idArr = ids.split( ",");
            //先找到需要导出的学员
            for( String id : idArr){
                AppUserTable appUserTable = dao.getOne( Long.valueOf( id));
                AppUserUidExcel excelRow = new AppUserUidExcel();
                excelRow.setCreateTime( DateFormatUtil.format( appUserTable.getCreateTime(), DateFormatUtil.DATE_RULE_3));
                excelRow.setPassword( appUserTable.getPassword());
                excelRow.setUid( appUserTable.getUid());
                excelRow.setSchoolName( appUserTable.getSchool().getName());
                excelRows.add( excelRow);
            }
        }else{
            List<AppUserTable> appUserTables = dao.getByRegistType(1);
            for( AppUserTable appUserTable : appUserTables){
                AppUserUidExcel excelRow = new AppUserUidExcel();
                excelRow.setCreateTime( DateFormatUtil.format( appUserTable.getCreateTime(), DateFormatUtil.DATE_RULE_3));
                excelRow.setPassword( appUserTable.getPassword());
                excelRow.setUid( appUserTable.getUid());
                excelRow.setSchoolName( appUserTable.getSchool().getName());
                excelRows.add( excelRow);
            }
        }
        String fileName = "exportUid_" + DateFormatUtil.format( new Date(), DateFormatUtil.DATE_RULE_2) + ".xls";
        String dumic =  "exportUid_" + spelitor + FileUtils.getDirByDate();//动态的这一截路径
        //服务器保存文件的目录
        String destDir = baseServerDir + dumic;
        //供浏览器客户端访问的目录
        String clientDir = baseClientDir + dumic;
        //先判断当前的目录是否存在
        FileUtils.handleDir( destDir);
        String serverFilePath = destDir  + spelitor + fileName;
        //导出单词数据到excel
        ExcelUtil.exportExcel( excelRows, "UID", "UID", AppUserUidExcel.class, serverFilePath);
        //客户端访问文件的地址
        String clientFilePath = serverUrl + clientDir  + spelitor + fileName;
        return clientFilePath;
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
            if( EmptyUtil.isNotEmpty( model.getSchoolClassId())){
                Join<AppUserTable, SchoolClassTable> join = root.join("schoolClass", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getSchoolClassId()));
            }
            if( EmptyUtil.isNotEmpty( model.getSchoolId())){
                Join<AppUserTable, SchoolTable> join = root.join("school", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getSchoolId()));
            }
            //如果角色是校长，则能查看本校的学生
            if( role.getCode().equals( PrivageConfig.MASRER_ADMIN_CODE)){
                Join<AppUserTable, SchoolTable> join = root.join("school", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), loginUser.getSchool().getId()));
            }else if( role.getCode().equals( PrivageConfig.TEACHER_ADMIN_CODE)){
                //如果角色是教师，则能查看名下班级的学生
                Join<AppUserTable, SchoolClassTable> join = root.join("schoolClass", JoinType.LEFT);
                CriteriaBuilder.In<Long> in = cb.in( join.get( "id"));
                if( loginUser.getSchoolClasses() != null && loginUser.getSchoolClasses().size() > 0) {
                    for (SchoolClassTable schoolClass : loginUser.getSchoolClasses()) {
                        in.value(schoolClass.getId());
                    }
                }else{
                    in.value( 0l);
                }
                predicate.getExpressions().add( in);
            }
            return predicate;
        };
        return specification;
    }
}
