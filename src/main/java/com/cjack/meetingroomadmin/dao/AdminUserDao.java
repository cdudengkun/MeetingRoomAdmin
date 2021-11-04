package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AdminUserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface AdminUserDao extends JpaRepository< AdminUserTable, Long>, JpaSpecificationExecutor<AdminUserTable> {

    AdminUserTable findOneByLoginName(String loginName);
}
