package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.PolicyInterpretationVideoTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface PolicyInterpretationVedioDao extends JpaRepository<PolicyInterpretationVideoTable, Long>, JpaSpecificationExecutor<PolicyInterpretationVideoTable> {

}
