package com.cjack.meetingroomadmin.dao;


import com.cjack.meetingroomadmin.table.EnterpriseSupportTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EnterpriseSupportDao extends JpaRepository<EnterpriseSupportTable, Long>, JpaSpecificationExecutor<EnterpriseSupportTable> {

}
