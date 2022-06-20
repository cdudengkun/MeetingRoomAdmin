package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.PolicyInterpretationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface PolicyInterpretationDao extends JpaRepository<PolicyInterpretationTable, Long>, JpaSpecificationExecutor<PolicyInterpretationTable> {

    @Modifying
    @Transactional
    @Query( value = "update policy_interpretation set type_id=null where type_id=?1", nativeQuery = true)
    void updateType( Long typeId);

}
