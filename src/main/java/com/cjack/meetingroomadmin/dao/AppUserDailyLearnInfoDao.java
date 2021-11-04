package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppUserDailyLearnInfoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


/**
 * Created by root on 4/20/19.
 */
public interface AppUserDailyLearnInfoDao extends JpaRepository<AppUserDailyLearnInfoTable, Long>, JpaSpecificationExecutor<AppUserDailyLearnInfoTable> {

    /**
     * 判断该用户从days对应这天之后是否有学习记录
     */
    @Query( value = "SELECT count(id) from app_user_daily_learn_info where app_user_id=?1 and days_number > ?2", nativeQuery = true)
    Integer lastDayIsLearn(Long appUserId, Integer days);
    /**
     * 调用数据的to_days函数，查询当前时间对应公元时间的天数
     * @return
     */
    @Query( value = "select to_days(NOW())", nativeQuery = true)
    Integer getCurrentTimeDays();

    @Query( value = "select sum( learn_time) total_learn_time, sum( online_time) total_online_time, sum( review_word_num) total_review_word_num,sum( learn_word_num)total_learn_word_num, sum( all_learn_word_num)total_all_learn_word_num from app_user_daily_learn_info where app_user_id=?1", nativeQuery = true)
    Object sumLearnInfo(Long appUserId);

    @Query( value = "select learn_time, online_time, review_word_num, learn_word_num, all_learn_word_num, punch_time from app_user_daily_learn_info where app_user_id=?1 and days_number=?2", nativeQuery = true)
    Object learnInfoOfDay(Long appUserId, Integer days);

    /**
     * 统计30天内达标的天数
     * @return
     */
    @Query( value = "select count(id) from app_user_daily_learn_info where app_user_id=?1 and days_number > ?3 and learn_time > ?2", nativeQuery = true)
    Integer countThirtyStandardDay(Long appUserId, Integer threshold, Integer daysNumber);

    /**
     * 统计总的达标天数
     * @return
     */
    @Query( value = "select count(id) from app_user_daily_learn_info where app_user_id=?1 and learn_time > ?2", nativeQuery = true)
    Integer countAllStandardDay(Long appUserId, Integer threshold);
}
