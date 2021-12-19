package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppUserLoginTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by root on 4/20/19.
 */
public interface AppUserLoginDao extends JpaRepository<AppUserLoginTable, Long> {

    @Query( value = "select count(1) from app_user_login where DATE_FORMAT(FROM_UNIXTIME(login_time/1000),'%Y-%m-%d')=DATE_FORMAT(FROM_UNIXTIME(?1/1000),'%Y-%m-%d')", nativeQuery = true)
    Integer countLoginUserByDay(Long timeStamps);
}
