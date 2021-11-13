package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.CooperationShoppingTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface CooperationShoppingDao extends JpaRepository<CooperationShoppingTable, Long>, JpaSpecificationExecutor<CooperationShoppingTable> {


}
