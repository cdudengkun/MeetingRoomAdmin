package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.SysConfigTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface SysConfigDao extends JpaRepository<SysConfigTable, Long> {

}
