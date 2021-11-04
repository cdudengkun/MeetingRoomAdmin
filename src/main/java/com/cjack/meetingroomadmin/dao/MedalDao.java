package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.MedalTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by root on 4/20/19.
 */
public interface MedalDao extends JpaRepository<MedalTable, Long> {

    /**
     * 统计用户名下对应类型的奖牌数
     * @return
     */
    @Query( value = "select count(um.id) from app_user_medal um, medal m where um.medal_id=m.id and um.app_user_id=?1 and m.abbr_name=?2", nativeQuery = true)
    Integer countMedals(Long appUserId, String abbrName);

}
