package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.PolicyInterpretationFileTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PolicyInterpretationFileDao extends JpaRepository<PolicyInterpretationFileTable, Long>, JpaSpecificationExecutor<PolicyInterpretationFileTable> {

}
