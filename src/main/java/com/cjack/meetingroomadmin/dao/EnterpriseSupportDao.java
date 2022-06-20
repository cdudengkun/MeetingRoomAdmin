package com.cjack.meetingroomadmin.dao;


import com.cjack.meetingroomadmin.table.EnterpriseSupportTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


public interface EnterpriseSupportDao extends JpaRepository<EnterpriseSupportTable, Long>, JpaSpecificationExecutor<EnterpriseSupportTable> {

    @Modifying
    @Transactional
    @Query( value = "update enterprise_support set type_id=null where type_id=?1", nativeQuery = true)
    void updateType( Long typeId);
}
