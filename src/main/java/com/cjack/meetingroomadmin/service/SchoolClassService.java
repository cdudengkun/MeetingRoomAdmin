package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.AppUserDailyLearnInfoDao;
import com.cjack.meetingroomadmin.dao.SchoolClassDao;
import com.cjack.meetingroomadmin.dao.SchoolDao;
import com.cjack.meetingroomadmin.model.SchoolClassModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolClassService {

    @Autowired
    private SchoolClassDao dao;
    @Autowired
    private AppUserDailyLearnInfoDao appUserDailyLearnInfoDao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private SchoolDao schoolDao;

    public void list( LayPage layPage, SchoolClassModel model, Long loginUserId){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<SchoolClassTable> specification = handleConditon( model, loginUserId);
        Page<SchoolClassTable> pageTable = dao.findAll( specification, pageable);
        List< SchoolClassModel> datas = new ArrayList<>();
        //查询今天的days
        Integer currDays = appUserDailyLearnInfoDao.getCurrentTimeDays();
        for( SchoolClassTable schoolClass : pageTable.getContent()){
            List<AppUserTable> students = schoolClass.getStudents();
            SchoolClassModel data = ModelUtils.copySignModel( schoolClass, SchoolClassModel.class);
            if( schoolClass.getTeacher() != null){
                data.setTeacherName( schoolClass.getTeacher().getName());
                data.setTeacherId( schoolClass.getTeacher().getId());
            }
            data.setSchoolId( schoolClass.getSchool().getId());
            data.setSchoolName( schoolClass.getSchool().getName());
            data.setStudentNumber( students.size());
            //最近两天学习人数
            Integer learnsOfTwoDay = 0;
            //最近七天学习人数
            Integer learnsOfSevenDay = 0;
            for( AppUserTable student : students){
                Long appUserId = student.getId();
                //判断该学生最近两天是否学习过
                if( appUserDailyLearnInfoDao.lastDayIsLearn( appUserId, currDays - 2) > 0){
                    learnsOfTwoDay++;
                }
                //判断该学生最近7天是否学习过
                if( appUserDailyLearnInfoDao.lastDayIsLearn( appUserId, currDays - 7) > 0){
                    learnsOfSevenDay++;
                }
            }
            data.setLearnsOfTwoDay( learnsOfTwoDay);
            data.setLearnsOfSevenDay( learnsOfSevenDay);
            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( (Long.valueOf( pageTable.getTotalElements())).intValue());
    }

    //查询当前用户所属学校下的所有班级
    public List< SchoolClassModel> list( Long loginUserId){
        AdminUserTable loginUser = adminUserDao.getOne( loginUserId);
        if( loginUser.getSchool() == null){
            return null;
        }
        List<SchoolClassTable> schoolClassTables = loginUser.getSchool().getSchoolClasses();
        return ModelUtils.copyListModel( schoolClassTables, SchoolClassModel.class);
    }

    public void del( String ids){
        List<SchoolClassTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            SchoolClassTable table = new SchoolClassTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( SchoolClassModel model){
        AdminUserTable adminUserTable = adminUserDao.getOne( model.getAdminUserId());
        SchoolClassTable schoolClass;
        if( EmptyUtil.isNotEmpty( model.getId())){
            schoolClass = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, schoolClass);
        }else{
            schoolClass = ModelUtils.copySignModel( model, SchoolClassTable.class);
        }
        if( EmptyUtil.isNotEmpty( model.getSchoolId())){
            schoolClass.setSchool( schoolDao.getOne( model.getSchoolId()));
        }else{
            //如果是老师，或者校长添加的班级，那么班级所属学校直接设置为他们的学校
            if( adminUserTable.getAdminRole().getCode().equals( PrivageConfig.MASRER_ADMIN_CODE)){
                //校长
                schoolClass.setSchool( adminUserTable.getSchool());
            }else if( adminUserTable.getAdminRole().getCode().equals( PrivageConfig.TEACHER_ADMIN_CODE)){
                //教师
                schoolClass.setSchool( adminUserTable.getSchool());
            }
        }
        schoolClass.setTeacher( adminUserDao.getOne( model.getTeacherId()));
        schoolClass.setAdminUser( adminUserTable);
        dao.save( schoolClass);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<SchoolClassTable> handleConditon( SchoolClassModel model, Long loginUserId){
        final AdminUserTable loginUser = adminUserDao.getOne( loginUserId);
        final AdminRoleTable role = loginUser.getAdminRole();
        Specification< SchoolClassTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            //如果角色是校长，则能查看本校的班级
            if( role.getCode().equals( PrivageConfig.MASRER_ADMIN_CODE)){
                Join< SchoolClassTable, SchoolTable> join = root.join("school", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), loginUser.getSchool().getId()));
            }else if( role.getCode().equals( PrivageConfig.TEACHER_ADMIN_CODE)){
                //如果角色是教师，则能查看名下的班级
                CriteriaBuilder.In<Long> in = cb.in( root.get( "id"));
                if( loginUser.getSchoolClasses() != null && loginUser.getSchoolClasses().size() > 0){
                    for( SchoolClassTable schoolClass : loginUser.getSchoolClasses()){
                        in.value( schoolClass.getId());
                    }
                }else{
                    in.value( 0l);
                }
                predicate.getExpressions().add( in);
            }
            if( EmptyUtil.isNotEmpty( model.getSchoolId())){
                Join< SchoolClassTable, SchoolTable> join = root.join("school", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getSchoolId()));
            }
            return predicate;
        };
        return specification;
    }
}
