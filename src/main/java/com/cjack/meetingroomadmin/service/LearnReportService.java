package com.cjack.meetingroomadmin.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.*;
import com.cjack.meetingroomadmin.model.AppUserDailyLearnInfoModel;
import com.cjack.meetingroomadmin.model.LearnReportModel;
import com.cjack.meetingroomadmin.table.*;
import com.cjack.meetingroomadmin.util.DateFormatUtil;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LearnReportService {

    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private AppUserDao appUserDao;
    @Autowired
    private AppUserDailyLearnInfoDao appUserDailyLearnInfoDao;
    @Autowired
    private LearnBookService learnBookService;
    @Autowired
    private MedalDao medalDao;
    @Value( "${standard.threshold}")
    private Integer threshold;

    public void list( LayPage layPage, LearnReportModel model, Long loginUserId) {
        Sort sort = new Sort( Sort.Direction.DESC, "createTime");
        Pageable pageable = new PageRequest( layPage.getPage()-1, layPage.getLimit(), sort);
        Specification<AppUserTable> specification = handleConditon( model, loginUserId);
        Page<AppUserTable> pageTable = appUserDao.findAll( specification, pageable);
        //查询今天对应的 to_days值
        Integer currDayNum = appUserDailyLearnInfoDao.getCurrentTimeDays();
        List<LearnReportModel> datas = new ArrayList<>();
        for( AppUserTable appUser : pageTable.getContent()){
            LearnReportModel data = new LearnReportModel();
            data.setName( appUser.getName());
            data.setPhone( appUser.getPhone());
            if( appUser.getSchoolClass() != null){
                data.setClassName( appUser.getSchoolClass().getName());
            }
            data.setLastLoginTime( appUser.getLastLoginTime());
            //今天学习信息
            Specification<AppUserDailyLearnInfoTable> todayLearnSpecification = handleConditon( appUser.getId(), currDayNum);
            AppUserDailyLearnInfoTable learnInfoTable = appUserDailyLearnInfoDao.findOne( todayLearnSpecification);
            if( learnInfoTable != null){
                data.setTodayLearnTime( learnInfoTable.getLearnTime());
                data.setTodayOnlineTime( learnInfoTable.getOnlineTime());
                data.setTodayReviewWords( learnInfoTable.getReviewWordNum());
                data.setTodayNewWords( learnInfoTable.getLearnWordNum());
            }else{
                data.setTodayLearnTime( 0);
                data.setTodayOnlineTime( 0);
                data.setTodayReviewWords( 0);
                data.setTodayNewWords( 0);
            }
            //学习信息汇总
            Object sumData = appUserDailyLearnInfoDao.sumLearnInfo( appUser.getId());
            if( sumData != null){
                Object[] arr = (Object[])sumData;
                data.setTotalLearnTime( arr[0] == null ? 0 : ((BigDecimal)arr[0]).intValue());
                data.setTotalLearnWords( arr[3] == null ? 0 : ((BigDecimal)arr[3]).intValue());
                data.setTotalOnlineTime( arr[1] == null ? 0 : ((BigDecimal)arr[1]).intValue());
            }else {
                data.setTotalLearnTime(0);
                data.setTotalLearnWords(0);
                data.setTotalOnlineTime(0);
            }
            String learnRate = "";
            for( LearnBookTable learnBookTable : appUser.getLearnBooks()){
                if( learnBookTable.getCurrentBook().equals( CommonConfig.YES)){
                    learnRate = learnBookTable.getLearnedNum() + "/" + learnBookTable.getBook().getWordsNum();
                    break;
                }
            }
            data.setAppUserId( appUser.getId());
            data.setLearnRate( learnRate);
            datas.add( data);
        }
        layPage.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
        layPage.setData( datas);
    }

    /**
     * 查询最近30天每天的学习信息
     * @param appUserId
     * @return
     */
    public List<AppUserDailyLearnInfoModel> dailyLearnInfoOfThirty( Long appUserId){
        List<AppUserDailyLearnInfoModel> datas = new ArrayList<>();
        int i = 0;
        Integer currDaysNum = appUserDailyLearnInfoDao.getCurrentTimeDays();
        while( i < 30){
            AppUserDailyLearnInfoModel data = new AppUserDailyLearnInfoModel();
            Object learnDataObject = appUserDailyLearnInfoDao.learnInfoOfDay( appUserId, currDaysNum - i);
            Date date = DateFormatUtil.formatLastNDay( i);
            if( learnDataObject != null){
                Object[] arr = (Object[])learnDataObject;
                data.setLearnTime( arr[0] == null ? 0 : (Integer) arr[0]);
                data.setOnlineTime( arr[1] == null ? 0 : (Integer)arr[1]);
                data.setReviewWordNum( arr[2] == null ? 0 : (Integer)arr[2]);
                data.setLearnWordNum( arr[3] == null ? 0 : (Integer)arr[3]);
                data.setAllLearnWordNum( arr[4] == null ? 0 : (Integer)arr[4]);
                data.setPunchTime( arr[5] != null ? (Date) arr[5] : null);
                data.setCreateTime( date);
            }else{
                data.setLearnTime( 0);
                data.setOnlineTime( 0);
                data.setReviewWordNum( 0);
                data.setLearnWordNum( 0);
                data.setAllLearnWordNum( 0);
                data.setCreateTime( date);
            }
            i++;
            datas.add( data);
        }
        return datas;
    }

    public JSONObject studentLearnDetail( Long appUserId){
        AppUserTable appUserTable = appUserDao.getOne( appUserId);
        JSONObject res = new JSONObject();
        //用户基本资料部分
        JSONObject userInfo = new JSONObject();
        userInfo.put( "avatar", appUserTable.getAvatar());//用户头像
        userInfo.put( "userName", appUserTable.getName());
        if( EmptyUtil.isNotEmpty( appUserTable.getSex())){
            userInfo.put( "sex", appUserTable.getSex().equals( 1) ? "男" : "女");
        }
        if(  appUserTable.getSchool() != null){
            userInfo.put( "schoolName", appUserTable.getSchool().getName());
        }
        if( appUserTable.getSchoolClass() != null){
            userInfo.put( "schoolClassName", appUserTable.getSchoolClass().getName());
            userInfo.put( "schoolClassTeacherName", appUserTable.getSchoolClass().getTeacher().getName());
        }
        userInfo.put( "phone", appUserTable.getPhone());
        //学习信息
        JSONObject learnInfo = new JSONObject();
        learnInfo.put( "bookNum", appUserTable.getLearnBooks().size());//激活课本数量
        learnInfo.put( "goldCoins", appUserTable.getAppUserAccount().getGoldCoin());//当前金币余额
        //当前课本汇总信息
        LearnBookTable currLearnBook = learnBookService.getCurrentBook( appUserId);
        //当前学习课本
        learnInfo.put( "currLearnBookName1", currLearnBook.getBook().getBookName());
        learnInfo.put( "currLearnBookName2", currLearnBook.getBook().getBookName());
        learnInfo.put( "currLearnBookName3", currLearnBook.getBook().getBookName());
        learnInfo.put( "currLearnBookRate", currLearnBook.getLearnedNum() + "/" + currLearnBook.getBook().getWordsNum());//当前学习进度
        learnInfo.put( "versionName", currLearnBook.getBook().getVersion().getName());//课本版本
        learnInfo.put( "currLearnBookState", currLearnBook.getFinish().equals( CommonConfig.YES) ? "已学完" : "正在学");//课本状态
        //测试相关
        Integer beforeLearnScore = 0;
        Integer afterLearnScore = 0;
        Integer increaseScore = 0;
        List<TestBookTable> testBooks = currLearnBook.getTestBooks();
        if( testBooks != null && testBooks.size() > 0){
            //学前测试成绩
            for( TestBookTable testBook : testBooks){
                //是否学前测试
                if( testBook.getPreLearnTest().equals( CommonConfig.YES)){
                    beforeLearnScore = testBook.getScore();
                    continue;
                }
            }
            //已经根据id倒序查询，相当于已经通过测试时候倒序排秀，这里第0条即为最后一条测试时间
            TestBookTable testBook = testBooks.get( 0);
            //学后测试成绩
            afterLearnScore = testBook.getScore();
            increaseScore = afterLearnScore - beforeLearnScore;
        }
        learnInfo.put( "beforeLearnScore", beforeLearnScore);//学前成绩
        learnInfo.put( "afterLearnScore", afterLearnScore);//学后成绩
        learnInfo.put( "increaseScore", increaseScore);//提分 学前成绩-学后成绩
        learnInfo.put( "currLearnBookId", currLearnBook.getId());

        //查询学习信息的统计
        Object sumData = appUserDailyLearnInfoDao.sumLearnInfo( appUserId);
        Object[] sumArr = (Object[])sumData;
        //查询今天的学习信息
        Integer currDaysNum = appUserDailyLearnInfoDao.getCurrentTimeDays();
        Object learnDataObject = appUserDailyLearnInfoDao.learnInfoOfDay( appUserId, currDaysNum);
        Object[] todayArr = (Object[])learnDataObject;

        learnInfo.put( "totalLearnWords", sumArr != null && sumArr[3] != null ? ((BigDecimal)sumArr[3]).intValue() : 0);//单词学习总数
        learnInfo.put( "startLearnTime", DateFormatUtil.format( appUserTable.getCreateTime(), DateFormatUtil.DATE_RULE_3));//开始学习时间
        learnInfo.put( "lastLoginTime", DateFormatUtil.format( appUserTable.getLastLoginTime(), DateFormatUtil.DATE_RULE_3));//最近登录时间
        learnInfo.put( "totalOnlineTime", sumArr != null && sumArr[1] != null ? ((BigDecimal)sumArr[1]).intValue() : 0);//总在线时长
        learnInfo.put( "todayOnlineTime", todayArr != null && todayArr[1] != null ? (Integer)todayArr[1] : 0);//今日在线时长
        learnInfo.put( "totalStandardDays", appUserDailyLearnInfoDao.countAllStandardDay( appUserId, threshold));//总达标天数 -当天学习时间超过一小时为达标
        learnInfo.put( "thirtyStandardDays", appUserDailyLearnInfoDao.countThirtyStandardDay( appUserId, threshold, currDaysNum - 30));//近30天内达标天数
        learnInfo.put( "totalLearnTime", sumArr != null && sumArr[0] != null ? ((BigDecimal)sumArr[0]).intValue() : 0);//总学习时长
        learnInfo.put( "todayLearnTime", todayArr != null && todayArr[0] != null ? (Integer)todayArr[0] : 0);//今日学习时长
        //获得的奖牌信息
        JSONObject medalInfo = new JSONObject();
        medalInfo.put( "GJ", medalDao.countMedals( appUserId, "GJ"));//国金
        medalInfo.put( "SJ", medalDao.countMedals( appUserId, "SJ"));//省金
        medalInfo.put( "XJ", medalDao.countMedals( appUserId, "XJ"));//校金
        medalInfo.put( "BJ", medalDao.countMedals( appUserId, "BJ"));//班金
        medalInfo.put( "GY", medalDao.countMedals( appUserId, "GY"));//国银
        medalInfo.put( "SY", medalDao.countMedals( appUserId, "SY"));//省银
        medalInfo.put( "XY", medalDao.countMedals( appUserId, "XY"));//校银
        medalInfo.put( "BT", medalDao.countMedals( appUserId, "BT"));//班银
        medalInfo.put( "GT", medalDao.countMedals( appUserId, "GT"));//国铜
        medalInfo.put( "ST", medalDao.countMedals( appUserId, "ST"));//省铜
        medalInfo.put( "XT", medalDao.countMedals( appUserId, "XT"));//校铜
        medalInfo.put( "BT", medalDao.countMedals( appUserId, "BT"));//班铜
        //当前课本单元信息列表
        JSONArray currLearnBookUnits = new JSONArray();
        for( LearnUnitTable unit : currLearnBook.getLearnUnits()){
            JSONObject currLearnBookUnit = new JSONObject();
            currLearnBookUnit.put( "name", unit.getUnit().getName());//单元名称
            String reviewInfo;//总的复习次数
            Integer reviewNumber1 = 0;//单元的语音复习次数
            Integer reviewNumber2 = 0;//单元的听音辨意次数
            Integer reviewNumber4 = 0;//单元的智能默写次数
            Integer reviewNumber5 = 0;//单元的选词填空次数
            Integer reviewNumber6 = 0;//单元的句式练习次数
            List<LearnUnitReviewDetailTable> reviewDetails = unit.getLearnUnitReviewDetails();
            if( reviewDetails == null || reviewDetails.size() == 0){
                reviewInfo = "记忆";
            }else{
                reviewInfo = "复习" + reviewDetails.size();
                for( LearnUnitReviewDetailTable reviewDetail : reviewDetails){
                    switch ( reviewDetail.getType()){
                        case 1: reviewNumber1++;break;
                        case 2: reviewNumber2++;break;
                        case 3: reviewNumber4++;break;
                        case 4: reviewNumber5++;break;
                        case 5: reviewNumber6++;break;
                    }
                }
            }
            currLearnBookUnit.put( "reviewInfo", reviewInfo);//记忆复习
            currLearnBookUnit.put( "reviewNumber1", reviewNumber1);//语音复习1
            currLearnBookUnit.put( "reviewNumber2", reviewNumber2);//听音辨意2
            currLearnBookUnit.put( "reviewNumber4", reviewNumber4);//智能默写4
            currLearnBookUnit.put( "reviewNumber5", reviewNumber5);//选词填空5
            currLearnBookUnit.put( "reviewNumber6", reviewNumber6);//句式练习6
            //最后一次测试的信息，由于单元的测试记录已经通过id倒序排序，这里取第一条数据即可当成是最后一条测试记录
            if( unit.getTestUnits() != null && unit.getTestUnits().size() != 0){
                TestUnitTable testUnitTable = unit.getTestUnits().get( 0);
                currLearnBookUnit.put( "testScore", testUnitTable.getScore());//闯关得分
                currLearnBookUnit.put( "useTime", testUnitTable.getUseTime() + "秒");//闯关时间
            }
            currLearnBookUnit.put( "learnUnitId", unit.getId());//提醒点评
            currLearnBookUnits.add( currLearnBookUnit);
        }
        res.put( "userInfo", userInfo);
        res.put( "learnInfo", learnInfo);
        res.put( "medalInfo", medalInfo);
        res.put( "currLearnBookUnits", currLearnBookUnits);
        return res;
    }

    public JSONObject downloadLearnInfo( Long appUserId){
        JSONObject res = new JSONObject();
        AppUserTable appUserTable = appUserDao.getOne( appUserId);
        Integer currDaysNum = appUserDailyLearnInfoDao.getCurrentTimeDays();
        //当前课本汇总信息
        LearnBookTable currLearnBook = learnBookService.getCurrentBook( appUserId);
        JSONObject userInfo = new JSONObject();
        userInfo.put( "avatar", appUserTable.getAvatar());//用户头像
        userInfo.put( "userName", appUserTable.getName());
        userInfo.put( "schoolClassName", appUserTable.getSchoolClass().getName());
        userInfo.put( "schoolClassTeacherName", appUserTable.getSchoolClass().getTeacher().getName());
        userInfo.put( "thirtyStandardDays", appUserDailyLearnInfoDao.countThirtyStandardDay( appUserId, threshold, currDaysNum - 30));//近30天内达标天数
        userInfo.put( "startLearnTime", DateFormatUtil.format( appUserTable.getCreateTime(), DateFormatUtil.DATE_RULE_3));//开始学习时间
        userInfo.put( "punchCardDaysNumber", 1);//总的打卡天数
        Object learnDataObject = appUserDailyLearnInfoDao.learnInfoOfDay( appUserId, currDaysNum);
        //查询学习信息的统计
        Object sumData = appUserDailyLearnInfoDao.sumLearnInfo( appUserId);
        Object[] sumArr = (Object[])sumData;
        //查询今天的学习信息
        Object[] todayArr = (Object[])learnDataObject;
        userInfo.put( "totalLearnTime", sumArr != null && sumArr[0] != null ? ((BigDecimal)sumArr[0]).intValue() : 0);//总学习时长
        userInfo.put( "todayLearnTime", todayArr != null && todayArr[0] != null ? (Integer)todayArr[0] : 0);//今日学习时长
        userInfo.put( "currLearnBookName", currLearnBook.getBook().getBookName());
        userInfo.put( "currLearnBookRate", currLearnBook.getLearnedNum() + "/" + currLearnBook.getBook().getWordsNum());//当前学习进度
        userInfo.put( "totalLearnWords", sumArr != null && sumArr[3] != null ? ((BigDecimal)sumArr[3]).intValue() : 0);//单词学习总数
        Integer beforeLearnScore = 0;
        Integer afterLearnScore = 0;
        List<TestBookTable> testBooks = currLearnBook.getTestBooks();
        if( testBooks != null && testBooks.size() > 0){
            //学前测试成绩
            for( TestBookTable testBook : testBooks){
                //是否学前测试
                if( testBook.getPreLearnTest().equals( CommonConfig.YES)){
                    beforeLearnScore = testBook.getScore();
                    continue;
                }
            }
            //已经根据id倒序查询，相当于已经通过测试时候倒序排秀，这里第0条即为最后一条测试时间
            TestBookTable testBook = testBooks.get( 0);
            //学后测试成绩
            afterLearnScore = testBook.getScore();
        }
        userInfo.put( "beforeLearnScore", beforeLearnScore);//学前成绩
        userInfo.put( "afterLearnScore", afterLearnScore);//学后成绩

        //当前课本单元信息列表
        JSONArray currLearnBookUnits = new JSONArray();
        for( LearnUnitTable unit : currLearnBook.getLearnUnits()){
            JSONObject currLearnBookUnit = new JSONObject();
            currLearnBookUnit.put( "name", unit.getUnit().getName());//单元名称
            Integer reviewNumber1 = 0;//单元的语音复习次数
            Integer reviewNumber4 = 0;//单元的智能默写次数
            Integer reviewNumber5 = 0;//单元的选词填空次数
            Integer reviewNumber6 = 0;//单元的句式练习次数
            List<LearnUnitReviewDetailTable> reviewDetails = unit.getLearnUnitReviewDetails();
            if( reviewDetails != null && reviewDetails.size() != 0){
                for( LearnUnitReviewDetailTable reviewDetail : reviewDetails){
                    switch ( reviewDetail.getType()){
                        case 1: reviewNumber1++;break;
                        case 3: reviewNumber4++;break;
                        case 4: reviewNumber5++;break;
                        case 5: reviewNumber6++;break;
                    }
                }
            }
            currLearnBookUnit.put( "reviewNumber1", reviewNumber1);//语音复习1
            currLearnBookUnit.put( "reviewNumber4", reviewNumber4);//智能默写4
            currLearnBookUnit.put( "reviewNumber5", reviewNumber5);//选词填空5
            currLearnBookUnit.put( "reviewNumber6", reviewNumber6);//句式练习6
            currLearnBookUnits.add( currLearnBookUnit);
        }
        //查询近一周学习、复习单词信息供图表显示
        JSONArray dates = new JSONArray();
        JSONArray reviewWords = new JSONArray();
        JSONArray learnWords = new JSONArray();
        int i = 7;
        while( i > 0){
            Object dailyDataObject = appUserDailyLearnInfoDao.learnInfoOfDay( appUserId, currDaysNum - i);
            Date date = DateFormatUtil.formatLastNDay( i);
            dates.add( DateFormatUtil.format( date, DateFormatUtil.DATE_RULE_8));
            if( dailyDataObject != null){
                Object[] arr = (Object[])dailyDataObject;
                reviewWords.add( (Integer)arr[2]);
                learnWords.add( (Integer)arr[3]);
            }else{
                reviewWords.add( 0);
                learnWords.add( 0);
            }
            i--;
        }
        res.put( "dates", dates);
        res.put( "reviewWords", reviewWords);
        res.put( "learnWords", learnWords);
        res.put( "userInfo", userInfo);
        res.put( "currLearnBookUnits", currLearnBookUnits);
        return res;
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<AppUserTable> handleConditon( LearnReportModel model, Long loginUserId){
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
            //如果角色是校长，则能查看本校的学生
            if( role.getCode().equals( PrivageConfig.MASRER_ADMIN_CODE)){
                Join<AppUserTable, SchoolTable> join = root.join("school", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), loginUser.getSchool().getId()));
            }else if( role.getCode().equals( PrivageConfig.TEACHER_ADMIN_CODE)){
                //如果角色是教师，则能查看名下班级的学生
                Join<AppUserTable, SchoolClassTable> join = root.join("schoolClass", JoinType.LEFT);
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

    /**
     * 组装查询条件
     * @param appUserId
     * @param daysNum 参数传入的日期转化为的日期
     * @return
     */
    private Specification<AppUserDailyLearnInfoTable> handleConditon( Long appUserId, Integer daysNum){
        Specification< AppUserDailyLearnInfoTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            Join<AppUserDailyLearnInfoTable, AppUserTable> join = root.join("appUser", JoinType.LEFT);
            predicate.getExpressions().add( cb.equal( join.get("id"), appUserId));
            predicate.getExpressions().add( cb.equal( root.get("daysNumber"), daysNum));
            return predicate;
        };
        return specification;
    }
}
