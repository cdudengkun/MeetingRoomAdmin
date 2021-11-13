package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.EnterpriseServiceTypeTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EnterpriseServiceTypeDao extends JpaRepository<EnterpriseServiceTypeTable, Long>, JpaSpecificationExecutor<EnterpriseServiceTypeTable> {


}
