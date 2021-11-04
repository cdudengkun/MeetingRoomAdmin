package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.*;
import com.cjack.meetingroomadmin.model.*;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.DateFormatUtil;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.text.Collator;
import java.util.*;

@Service
public class BookService {

    @Autowired
    private BookDao dao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private LearnBookDao learnBookDao;
    @Autowired
    private BookVersionDao bookVersionDao;
    @Autowired
    private DefineSignValueDao defineSignValueDao;

    public void list(LayPage layPage, BookModel model){
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit());
        Specification<BookTable> specification = handleConditon( model);
        Page<BookTable> pageTable = dao.findAll( specification, pageable);
        List< BookModel> datas = new ArrayList<>();
        for( BookTable bookTable : pageTable.getContent()){
            BookModel data = ModelUtils.copySignModel( bookTable, BookModel.class);
            data.setVersionName( bookTable.getVersion().getName());
            data.setVersionId( bookTable.getVersion().getId());
            data.setStageName( bookTable.getVersion().getStageName());
            data.setStageId( bookTable.getVersion().getStageId());
            data.setGradeName( bookTable.getGrade().getDataKey());
            data.setGradeId( bookTable.getGrade().getId());
            datas.add( data);
        }
        //课本的数据需要按照 课本阶段、课本版本、课本年级、课本名称  的优先级排序
        Collections.sort(datas, new Comparator<BookModel>() {
            @Override
            public int compare(BookModel o1, BookModel o2) {
                Collator collator = Collator.getInstance( Locale.CHINA);
                if( o1.getStageName().equals( o2.getStageName())){
                    if( o1.getVersionName().equals( o2.getVersionName())){
                        if( o1.getGradeName().equals( o2.getGradeName())){
                            return collator.compare( o1.getBookName(), o2.getBookName());
                        }else{
                            return collator.compare( o1.getGradeName(), o2.getGradeName());
                        }
                    }else{
                        return collator.compare( o1.getVersionName(), o2.getVersionName());
                    }
                }else{
                    return collator.compare( o1.getStageName(), o2.getStageName());
                }
            }
        });
        layPage.setData( datas);
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    public void save( BookModel model){
        BookTable bookTable;
        if( EmptyUtil.isNotEmpty( model.getId())){
            bookTable = dao.getOne( model.getId());
        }else{
            bookTable = new BookTable();
        }
        ModelUtils.copySignModel( model, bookTable);
        if( EmptyUtil.isNotEmpty( model.getGradeId())){
            bookTable.setGrade( defineSignValueDao.getOne( model.getGradeId()));
        }
        bookTable.setVersion( handleBookVersion( model));
        dao.save( bookTable);
    }

    private BookVersionTable handleBookVersion( BookModel model){
        BookVersionTable versionTable = bookVersionDao.findOne( handleConditon( model.getVersionName(), model.getStageId()));
        if( versionTable == null){//如果version不存在则保存
            versionTable = new BookVersionTable();
            versionTable.setCreateTime( new Date());
            versionTable.setUpdateTime( new Date());
            versionTable.setName( model.getVersionName());
            versionTable.setStageId( model.getStageId());
            versionTable.setStageName( defineSignValueDao.getOne( model.getStageId()).getDataKey());
            bookVersionDao.save( versionTable);
        }
        return versionTable;
    }

    public void del( Long id){
        dao.delete( dao.getOne( id));
    }

    //统计校长名下的学员激活各个阶段的数量
    public BookForMasterViewModel listForMasterView( Long adminUserId){
        BookForMasterViewModel model = new BookForMasterViewModel();
        Integer activeNumber_29 = 0;//小学课本
        Integer activeNumber_30 = 0;//初中课本
        Integer activeNumber_31 = 0;//高中课本
        Integer activeNumber_32 = 0;//大学课本
        Integer activeNumber_33 = 0;//专业英语
        Integer activeNumber_34 = 0;//其他课本
        Integer activeNumber_total = 0;//总计
        AdminUserTable schoolMaster = adminUserDao.getOne( adminUserId);
        SchoolTable school = schoolMaster.getSchool();
        if( school != null){
            List<AppUserTable> students = school.getStudents();
            if( students != null && students.size() != 0){
                for( AppUserTable student : students){
                    List<LearnBookTable> learnBooks = student.getLearnBooks();
                    if( learnBooks != null && learnBooks.size() != 0){
                        for( LearnBookTable learnBook : learnBooks) {
                            if( learnBook.getBuy().equals( CommonConfig.YES)){
                                int stageId = learnBook.getBook().getVersion().getId().intValue();
                                switch ( stageId) {
                                    case 29: //小学课本
                                        activeNumber_29++;
                                        break;
                                    case 30://初中课本
                                        activeNumber_30++;
                                        break;
                                    case 31://高中课本
                                        activeNumber_31++;
                                        break;
                                    case 32://大学课本
                                        activeNumber_32++;
                                        break;
                                    case 33://专业英语
                                        activeNumber_33++;
                                        break;
                                    case 34://其他课本
                                        activeNumber_total++;
                                        break;
                                }
                                activeNumber_total++;
                            }
                        }
                    }
                }
            }
        }
        model.setActiveNumber_29( activeNumber_29);
        model.setActiveNumber_30( activeNumber_30);
        model.setActiveNumber_31( activeNumber_31);
        model.setActiveNumber_32( activeNumber_32);
        model.setActiveNumber_33( activeNumber_33);
        model.setActiveNumber_34( activeNumber_34);
        model.setActiveNumber_total( activeNumber_total);
        return model;
    }

    /**
     * 获取系统所有的学习阶段
     * @return
     */
    public List<BookStageModel> stageList() {
        DefineSignKeyTable condition = new DefineSignKeyTable();
        condition.setType( 2);
        Example< DefineSignKeyTable> example = Example.of( condition);
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        List<DefineSignKeyTable> stageTables = defineSignValueDao.findAll( example, sort);
        List<BookStageModel> stages = new ArrayList<>();
        for( DefineSignKeyTable stageTable : stageTables){
            stages.add( new BookStageModel( stageTable.getId(), stageTable.getDataKey()));
        }
        return stages;
    }

    /**
     * 获取系统所有的课本版本
     * @return
     */
    public List<BookVersionModel> versionList() {
        List<BookVersionTable> versionTables = bookVersionDao.findAll();
        List<BookVersionModel> versions = new ArrayList<>();
        for( BookVersionTable versionTable : versionTables){
            versions.add( new BookVersionModel( versionTable.getId(), versionTable.getName()));
        }
        return versions;
    }

    /**
     * 获取系统所有的年级
     * @return
     */
    public List<BookGradeModel> gradeList() {
        DefineSignKeyTable condition = new DefineSignKeyTable();
        condition.setType( 3);
        Example< DefineSignKeyTable> example = Example.of( condition);
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        List<DefineSignKeyTable> stageTables = defineSignValueDao.findAll( example, sort);
        List<BookGradeModel> stages = new ArrayList<>();
        for( DefineSignKeyTable stageTable : stageTables){
            stages.add( new BookGradeModel( stageTable.getId(), stageTable.getDataKey()));
        }
        return stages;
    }

    //统计校长名下的学员激活各个阶段的数量
    public LastSellBookInfoModel lastSellBookInfo( Long adminUserId){
        LastSellBookInfoModel model = new LastSellBookInfoModel();
        AdminUserTable schoolMaster = adminUserDao.getOne( adminUserId);
        SchoolTable school = schoolMaster.getSchool();
        if( school != null){
            Specification<LearnBookTable> specification = handleConditon( school.getId(), null);
            List<LearnBookTable> datas = learnBookDao.findAll( specification);
            //激活总数
            if( datas != null){
                model.setSell_total( datas.size());
            }
            //三十天激活数量
            if( datas != null){
                specification = handleConditon( school.getId(), DateFormatUtil.formatLastNDay( 30));
                datas = learnBookDao.findAll( specification);
                model.setSell_thirty( datas.size());
            }
        }

        return model;
    }

    //统计经销商月度课本销售信息
    public int dealerMonthSellBookInfo( Long adminUserId){
        AdminUserTable dealer = adminUserDao.getOne( adminUserId);
        List<SchoolTable> agentSchools = dealer.getAgentSchools();
        if( agentSchools == null || agentSchools.size() == 0){
            return 0;
        }
        int total = 0;
        for( SchoolTable agentSchool : agentSchools){
            Specification<LearnBookTable> specification = handleConditon( agentSchool.getId(), DateFormatUtil.getThisMonthFirstDay());
            List<LearnBookTable> datas = learnBookDao.findAll( specification);
            if( datas != null && datas.size() > 0){
                total += datas.size();
            }
        }

        return total;
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<BookTable> handleConditon( BookModel model){
        Specification< BookTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getBookName())){
                predicate.getExpressions().add( cb.equal( root.get("bookName"), model.getBookName()));
            }
            if( EmptyUtil.isNotEmpty( model.getStageId())){
                Join<BookTable, BookVersionTable> versionJoin = root.join("version", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( versionJoin.get("stageId"), model.getStageId()));
            }
            if( EmptyUtil.isNotEmpty( model.getVersionId())){
                Join<BookTable, BookVersionTable> join = root.join("version", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getVersionId()));
            }
            if( EmptyUtil.isNotEmpty( model.getGradeId())){
                Join<BookTable, DefineSignKeyTable> join = root.join("grade", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getGradeId()));
            }

            if( EmptyUtil.isNotEmpty( model.getDealerId())){
                Join<BookTable, LearnBookTable> learnBookJoin = root.join("learnBooks", JoinType.LEFT);
                Join<LearnBookTable, AppUserTable> appUserJoin = learnBookJoin.join("appUser", JoinType.LEFT);
                Join<AppUserTable, SchoolTable> schoolJoin = appUserJoin.join("school", JoinType.LEFT);
                AdminUserTable adminUserTable = adminUserDao.getOne( model.getDealerId());
                if( EmptyUtil.isNotEmpty( model.getSchoolId())){
                    predicate.getExpressions().add( cb.equal( schoolJoin.get("id"), model.getSchoolId()));
                }else{
                    //超级管理员也是使用的经销商的课本统计页面，这里返回所有学校
                    if( !adminUserTable.getAdminRole().getCode().equals( PrivageConfig.SUPER_ADMIN_CODE)){
                        Join<SchoolTable, AdminUserTable> dealerJoin = schoolJoin.join("agent", JoinType.LEFT);
                        predicate.getExpressions().add( cb.equal( dealerJoin.get("id"), model.getDealerId()));
                    }
                }
            }
            return predicate;
        };
        return specification;
    }

    /**
     * 组装查询条件，查询学校下激活课本的数量
     * @param schoolId
     * @return
     */
    private Specification<LearnBookTable> handleConditon( Long schoolId, Date queryTime){
        Specification< LearnBookTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<LearnBookTable, AppUserTable> appUserJofin = root.join("appUser", JoinType.LEFT);
            Join<AppUserTable, SchoolTable> schoolJoin = appUserJofin.join("school", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( schoolJoin.get("id"), schoolId));
            if( queryTime != null){
                predicate.getExpressions().add( cb.greaterThan( root.get("createTime"), queryTime));
            }
            predicate.getExpressions().add( cb.equal( root.get("buy"), CommonConfig.YES));
            return predicate;
        };
        return specification;
    }

    /**
     * 根据版本名称和年级id，查询 版本table
     * @return
     */
    private Specification<BookVersionTable> handleConditon( String versionName, Long stageId){
        Specification< BookVersionTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            predicate.getExpressions().add( cb.equal( root.get("name"), versionName));
            predicate.getExpressions().add( cb.equal( root.get("stageId"), stageId));
            return predicate;
        };
        return specification;
    }
}
