package com.cjack.meetingroomadmin.dao;


import com.cjack.meetingroomadmin.table.ConsultInfoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface ConsultInfoDao extends JpaRepository<ConsultInfoTable, Long>, JpaSpecificationExecutor<ConsultInfoTable> {

}
