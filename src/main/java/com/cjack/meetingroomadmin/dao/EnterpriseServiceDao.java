package com.cjack.meetingroomadmin.dao;


import com.cjack.meetingroomadmin.table.EnterpriseServiceTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface EnterpriseServiceDao extends JpaRepository<EnterpriseServiceTable, Long>, JpaSpecificationExecutor<EnterpriseServiceTable> {

}
