package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.RechargeCardTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by root on 4/20/19.
 */
public interface RechargeCardDao extends JpaRepository<RechargeCardTable, Long>, JpaSpecificationExecutor<RechargeCardTable> {

    /**
     * 统计总的充值卡数量
     */
    @Query( value = "SELECT count(id) from recharge_card ", nativeQuery = true)
    Integer countTotal();
    /**
     * 未激活总数
     */
    @Query( value = "SELECT count(id) from recharge_card where status=2", nativeQuery = true)
    Integer countNoActiveTotal();
    /**
     * 激活总数
     */
    @Query( value = "SELECT count(id) from recharge_card where status=1", nativeQuery = true)
    Integer countActiveTotal();
    /**
     * 已过期总数
     */
    @Query( value = "SELECT count(id) from recharge_card where TO_DAYS( NOW()) - TO_DAYS( create_time) > validity_day", nativeQuery = true)
    Integer countExpireTotal();
}
