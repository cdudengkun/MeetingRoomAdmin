package com.cjack.meetingroomadmin.dao;


import com.cjack.meetingroomadmin.table.CompanyJoinInfoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface CompanyJoinInfoDao extends JpaRepository<CompanyJoinInfoTable, Long>, JpaSpecificationExecutor<CompanyJoinInfoTable> {

}
