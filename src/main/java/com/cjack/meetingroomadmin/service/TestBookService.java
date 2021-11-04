package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.AdminUserDao;
import com.cjack.meetingroomadmin.dao.LearnBookDao;
import com.cjack.meetingroomadmin.dao.TestBookDao;
import com.cjack.meetingroomadmin.model.SeachBookTestModel;
import com.cjack.meetingroomadmin.model.TestBookModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.EmptyUtil;
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
public class TestBookService {

    @Autowired
    private TestBookDao dao;
    @Autowired
    private LearnBookDao learnBookDao;
    @Autowired
    private AdminUserDao adminUserDao;

    public void list( LayPage layPage, SeachBookTestModel model, Long adminUserId){
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<LearnBookTable> specification = handleConditon( model, adminUserId);
        Page<LearnBookTable> pageTable = learnBookDao.findAll( specification, pageable);
        List<TestBookModel> datas = new ArrayList<>();
        for( LearnBookTable learnBook : pageTable.getContent()){
            TestBookModel data = new TestBookModel();
            //学员姓名
            data.setAppUserId( learnBook.getAppUser().getId());
            data.setAppUserName( learnBook.getAppUser().getName());
            //课本名称
            data.setBookName( learnBook.getBook().getBookName());
            data.setLearnBookId( learnBook.getId());
            //测试相关
            List<TestBookTable> testBooks = learnBook.getTestBooks();
            if( testBooks != null && testBooks.size() > 0){
                //学前测试成绩
                for( TestBookTable testBook : testBooks){
                    //是否学前测试
                    if( testBook.getPreLearnTest().equals( CommonConfig.YES)){
                        data.setBeforeLearnTestScore( testBook.getScore());
                        break;
                    }
                }
                //已经根据id倒序查询，相当于已经通过测试时候倒序排秀，这里第0条即为最后一条测试时间
                TestBookTable testBook = testBooks.get( 0);
                //课本学习完成才展示学后测试
                if( learnBook.getFinish().equals( CommonConfig.YES)){
                    //学后测试成绩
                    data.setAfterLearnTestScore( testBook.getScore());
                    //最后测试时间
                    data.setLastTestTime( testBook.getCreateTime());
                }
            }

            datas.add( data);
        }
        layPage.setData( datas);
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    /**
     * 组装查询条件，先把课本和学生的信息查询出来
     * @param model
     * @return
     */
    private Specification<LearnBookTable> handleConditon( SeachBookTestModel model, Long loginUserId){
        final AdminUserTable loginUser = adminUserDao.getOne( loginUserId);
        final AdminRoleTable role = loginUser.getAdminRole();
        Specification< LearnBookTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            //课本跟学生 内联查询
            Join<AppUserTable, LearnBookTable> bookJoinAppUser = root.join("appUser", JoinType.INNER);
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.like( bookJoinAppUser.get("name"), "%" + model.getName() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getPhone())){
                predicate.getExpressions().add( cb.like( bookJoinAppUser.get("phone"), "%" + model.getPhone() + "%"));
            }
            if( EmptyUtil.isNotEmpty( model.getSchoolClassId())){
                Join<AppUserTable, SchoolClassTable> join = bookJoinAppUser.join("schoolClass", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getSchoolClassId()));
            }
            //如果角色是校长，则能查看本校的学生
            if( role.getCode().equals( PrivageConfig.MASRER_ADMIN_CODE)){
                Join<AppUserTable, SchoolTable> join = bookJoinAppUser.join("school", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), loginUser.getSchool().getId()));
            }else if( role.getCode().equals( PrivageConfig.TEACHER_ADMIN_CODE)){
                //如果角色是教师，则能查看名下班级的学生
                Join<AppUserTable, SchoolClassTable> join = bookJoinAppUser.join("schoolClass", JoinType.LEFT);
                CriteriaBuilder.In<Long> in = cb.in( join.get( "id"));
                if( loginUser.getSchoolClasses() != null && loginUser.getSchoolClasses().size() > 0){
                    for( SchoolClassTable schoolClass : loginUser.getSchoolClasses()){
                        in.value( schoolClass.getId());
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
