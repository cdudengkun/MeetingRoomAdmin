package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.SchoolTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface SchoolDao extends JpaRepository<SchoolTable, Long>, JpaSpecificationExecutor<SchoolTable> {


}
