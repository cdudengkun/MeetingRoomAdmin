package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.CouponTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface CouponDao extends JpaRepository<CouponTable, Long>, JpaSpecificationExecutor<CouponTable> {


}
