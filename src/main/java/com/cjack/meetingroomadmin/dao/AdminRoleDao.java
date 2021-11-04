package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AdminRoleTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 4/20/19.
 */
public interface AdminRoleDao extends JpaRepository<AdminRoleTable, Long> {

    public List<AdminRoleTable> findByCodeIsNot(String superCode);

}
