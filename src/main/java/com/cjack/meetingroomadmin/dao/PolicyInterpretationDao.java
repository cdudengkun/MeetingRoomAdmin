package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.PolicyInterpretationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PolicyInterpretationDao extends JpaRepository<PolicyInterpretationTable, Long>, JpaSpecificationExecutor<PolicyInterpretationTable> {

}
