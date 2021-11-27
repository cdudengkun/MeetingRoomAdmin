package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppUserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by root on 4/20/19.
 */
public interface AppUserDao extends JpaRepository<AppUserTable, Long>, JpaSpecificationExecutor<AppUserTable> {

    public AppUserTable getByLoginName(String loginName);

    public AppUserTable getByWechatOpenId(String wechatOpenId);

    @Query( value = "select count(1) from app_user where DATE_FORMAT(FROM_UNIXTIME(last_login_time/1000),'%Y-%m-%d')=DATE_FORMAT(FROM_UNIXTIME(?1/1000),'%Y-%m-%d')", nativeQuery = true)
    Integer countLoginUserByDay( Long timeStamps);

}
