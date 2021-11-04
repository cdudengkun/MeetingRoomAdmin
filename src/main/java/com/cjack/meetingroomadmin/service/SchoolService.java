package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.AppUserDailyLearnInfoDao;
import com.cjack.meetingroomadmin.dao.CityDao;
import com.cjack.meetingroomadmin.dao.SchoolDao;
import com.cjack.meetingroomadmin.model.SchoolModel;
import com.cjack.meetingroomadmin.table.AdminUserTable;
import com.cjack.meetingroomadmin.table.AppUserTable;
import com.cjack.meetingroomadmin.table.CityTable;
import com.cjack.meetingroomadmin.table.SchoolTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolService {

    @Autowired
    private SchoolDao dao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private AppUserDailyLearnInfoDao appUserDailyLearnInfoDao;
    @Autowired
    private CityDao cityDao;

    public List<SchoolModel> list( SchoolModel model){
        Specification<SchoolTable> specification = handleConditon( model);
        List<SchoolTable> tables = dao.findAll( specification);
        List<SchoolModel> datas = new ArrayList<>();
        //查询今天的days
        Integer currDays = appUserDailyLearnInfoDao.getCurrentTimeDays();
        for( SchoolTable table : tables){
            SchoolModel data = ModelUtils.copySignModel( table, SchoolModel.class);
            data.setProvinceId( table.getProvince().getId());
            data.setProvinceName( table.getProvince().getName());
            data.setCityId( table.getCity().getId());
            data.setCityName( table.getCity().getName());
            data.setCountryId( table.getCountry().getId());
            data.setCountryName( table.getCountry().getName());
            AdminUserTable master = getMaster( table);
            if( master != null){
                data.setMasterId( master.getId());
                data.setMasterName( master.getName());
            }
            if( table.getAgent() != null){
                data.setAgentId( table.getAgent().getId());
                data.setAgentName( table.getAgent().getName());
            }
            List<AppUserTable> students = table.getStudents();
            Integer learnsOfSevenDay = 0;//最近七天学习人数
            Integer learnsOfThrityDay = 0;//最近30天学习人数
            for( AppUserTable student : students){
                Long appUserId = student.getId();
                //判断该学生最近7天是否学习过
                if( appUserDailyLearnInfoDao.lastDayIsLearn( appUserId, currDays - 7) > 0){
                    learnsOfSevenDay++;
                }
                //判断该学生最近30天是否学习过
                if( appUserDailyLearnInfoDao.lastDayIsLearn( appUserId, currDays - 30) > 0){
                    learnsOfThrityDay++;
                }
            }
            data.setStudentNumber( students.size());
            data.setClassNumber( table.getSchoolClasses().size());
            data.setLearnsOfSevenDay( learnsOfSevenDay);
            data.setLearnsOfThrityDay( learnsOfThrityDay);
            datas.add( data);
        }
        return datas;
    }

    public List<SchoolModel> listForAddMaster( Long editUserId){
        List<SchoolTable> tables = dao.findAll();
        List<SchoolModel> datas = new ArrayList<>();
        for( SchoolTable table : tables){
            AdminUserTable master = getMaster( table);
            //学校已经绑定校长，并且不是校长不是当前用户，则不返回
            if( master != null && !master.getId().equals( editUserId)){
                continue;
            }
            SchoolModel data = ModelUtils.copySignModel( table, SchoolModel.class);
            data.setProvinceId( table.getProvince().getId());
            data.setProvinceName( table.getProvince().getName());
            data.setCityId( table.getCity().getId());
            data.setCityName( table.getCity().getName());
            data.setCountryId( table.getCountry().getId());
            data.setCountryName( table.getCountry().getName());
            if( master != null){
                data.setMasterId( master.getId());
                data.setMasterName( master.getName());
            }
            if( table.getAgent() != null){
                data.setAgentId( table.getAgent().getId());
                data.setAgentName( table.getAgent().getName());
            }
            datas.add( data);
        }
        return datas;
    }

    public List<SchoolModel> listForAddTeacher( Long adminUserId){
        AdminUserTable loginUser = adminUserDao.getOne( adminUserId);
        List<SchoolTable> tables = dao.findAll();
        List<SchoolModel> datas = new ArrayList<>();
        //如果不是超级管理员（不是超级管理员，肯定是校长列），则只返回自己当前的学校
        if( !loginUser.getAdminRole().getCode().equals( PrivageConfig.SUPER_ADMIN_CODE) ){
            datas.add( ModelUtils.copySignModel( loginUser.getSchool(), SchoolModel.class));
            return datas;
        }
        for( SchoolTable table : tables){
            SchoolModel data = ModelUtils.copySignModel( table, SchoolModel.class);
            data.setProvinceId( table.getProvince().getId());
            data.setProvinceName( table.getProvince().getName());
            data.setCityId( table.getCity().getId());
            data.setCityName( table.getCity().getName());
            data.setCountryId( table.getCountry().getId());
            data.setCountryName( table.getCountry().getName());
            AdminUserTable master = getMaster( table);
            if( master != null){
                data.setMasterId( master.getId());
                data.setMasterName( master.getName());
            }
            if( table.getAgent() != null){
                data.setAgentId( table.getAgent().getId());
                data.setAgentName( table.getAgent().getName());
            }
            datas.add( data);
        }
        return datas;
    }

    public List<SchoolModel> listForDealer( Long adminUserId, SchoolModel model){
        AdminUserTable loginUser = adminUserDao.getOne( adminUserId);
        Specification<SchoolTable> specification = handleConditon( model);
        List<SchoolTable> tables = dao.findAll( specification);
        List<SchoolModel> datas = new ArrayList<>();
        for( SchoolTable table : tables){
            //不是超级管理员，只展示自己名下代理的学校
            if( !loginUser.getAdminRole().getCode().equals( PrivageConfig.SUPER_ADMIN_CODE)){
                if( table.getAgent()== null || !table.getAgent().getId().equals( adminUserId)){
                    continue;
                }
            }
            SchoolModel data = ModelUtils.copySignModel( table, SchoolModel.class);
            data.setProvinceId( table.getProvince().getId());
            data.setProvinceName( table.getProvince().getName());
            data.setCityId( table.getCity().getId());
            data.setCityName( table.getCity().getName());
            data.setCountryId( table.getCountry().getId());
            data.setCountryName( table.getCountry().getName());
            AdminUserTable master = getMaster( table);
            if( master != null){
                data.setMasterId( master.getId());
                data.setMasterName( master.getName());
            }
            if( table.getAgent() != null){
                data.setAgentId( table.getAgent().getId());
                data.setAgentName( table.getAgent().getName());
            }
            datas.add( data);
        }
        return datas;
    }

    public void del( String ids){
        List<SchoolTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            SchoolTable table = new SchoolTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( SchoolModel model){
        SchoolTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, SchoolTable.class);
        }
        if( EmptyUtil.isNotEmpty( model.getAgentId())){
            table.setAgent( adminUserDao.getOne( model.getAgentId()));
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
        dao.save( table);
    }


    /**
     * 找到学校的教师列表里面的校长
     * @param school
     * @return
     */
    private AdminUserTable getMaster( SchoolTable school){
        List<AdminUserTable> teachers = school.getTeachers();
        if( teachers == null || teachers.size() == 0){
            return null;
        }
        for( AdminUserTable teacher : teachers){
            if( teacher.getAdminRole().getCode().equals( PrivageConfig.MASRER_ADMIN_CODE)){
                return teacher;
            }
        }
        return null;
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<SchoolTable> handleConditon( SchoolModel model){
        Specification< SchoolTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( root.get("name"), "%" + model.getName() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getProvinceId())){
                Join<SchoolTable, CityTable> versionJoin = root.join("province", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( versionJoin.get("id"), model.getProvinceId()));
            }
            if( EmptyUtil.isNotEmpty( model.getCityId())){
                Join<SchoolTable, CityTable> join = root.join("city", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getCityId()));
            }
            return predicate;
        };
        return specification;
    }
}
