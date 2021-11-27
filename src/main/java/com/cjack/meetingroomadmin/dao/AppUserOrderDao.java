package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppUserOrderTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by root on 4/20/19.
 */
public interface AppUserOrderDao extends JpaRepository<AppUserOrderTable, Long>, JpaSpecificationExecutor<AppUserOrderTable> {

    @Query( value = "select sum(amount) from app_user_order where DATE_FORMAT(FROM_UNIXTIME(pay_time/1000),'%Y-%m-%d')=DATE_FORMAT(FROM_UNIXTIME(?1/1000),'%Y-%m-%d')", nativeQuery = true)
    Integer queryTradeMount( Long timeStamps);

}
