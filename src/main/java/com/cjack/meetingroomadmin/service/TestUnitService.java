package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.*;
import com.cjack.meetingroomadmin.model.LearnUnitCommentModel;
import com.cjack.meetingroomadmin.model.TestUnitModel;
import com.cjack.meetingroomadmin.table.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TestUnitService {

    @Autowired
    private TestUnitDao dao;
    @Autowired
    protected LearnBookDao learnBookDao;
    @Autowired
    private LearnUnitDao learnUnitDao;
    @Autowired
    private AdminUserDao adminUserDao;
    @Autowired
    private LearnUnitCommentDao learnUnitCommentDao;

    public List<TestUnitModel> list( Long learnBookId) {
        LearnBookTable learnBookTable = learnBookDao.getOne( learnBookId);
        List<LearnUnitTable> learnUnits = learnBookTable.getLearnUnits();
        List<TestUnitModel> datas = new ArrayList<>();
        for( LearnUnitTable learnUnit : learnUnits){
            TestUnitModel data = new TestUnitModel();
            data.setLearnUnitId( learnUnit.getId());
            data.setUnitName( learnUnit.getUnit().getName());
            //最后一次测试的信息，由于单元的测试记录已经通过id倒序排序，这里取第一条数据即可当成是最后一条测试记录
            if( learnUnit.getTestUnits() != null && learnUnit.getTestUnits().size() != 0){
                TestUnitTable testUnitTable = learnUnit.getTestUnits().get( 0);
                data.setScore( testUnitTable.getScore());
                data.setUseTime( testUnitTable.getUseTime());
                data.setTestTime( testUnitTable.getCreateTime());
            }
            //统计该单元的复习信息
            Integer reviewNumber1 = 0;//单元的语音复习次数
            Integer reviewNumber4 = 0;//单元的智能听写次数
            Integer reviewNumber5 = 0;//单元的句子填空次数
            Integer reviewNumber6 = 0;//单元的句式练习次数
            List<LearnUnitReviewDetailTable> reviewDetails = learnUnit.getLearnUnitReviewDetails();
            for( LearnUnitReviewDetailTable reviewDetail : reviewDetails){
                switch ( reviewDetail.getType()){
                    case 1: reviewNumber1++;break;
                    case 4: reviewNumber4++;break;
                    case 5: reviewNumber5++;break;
                    case 6: reviewNumber6++;break;
                }
            }
            data.setReviewNumber1( reviewNumber1);
            data.setReviewNumber4( reviewNumber4);
            data.setReviewNumber5( reviewNumber5);
            data.setReviewNumber6( reviewNumber6);
            datas.add( data);
        }
        return datas;
    }

    public void addComment( LearnUnitCommentModel model){
        LearnUnitTable learnUnitTable = learnUnitDao.getOne( model.getLearnUnitId());
        AdminUserTable adminUserTable = adminUserDao.getOne( model.getAdminUserId());
        LearnUnitCommentTable learnUnitCommentTable = new LearnUnitCommentTable();
        learnUnitCommentTable.setLearnUnit( learnUnitTable);
        learnUnitCommentTable.setAdminUser( adminUserTable);
        learnUnitCommentTable.setCreateTime( new Date());
        learnUnitCommentTable.setUpdateTime( new Date());
        learnUnitCommentTable.setContent( model.getContent());
        learnUnitCommentDao.save( learnUnitCommentTable);
    }
}
