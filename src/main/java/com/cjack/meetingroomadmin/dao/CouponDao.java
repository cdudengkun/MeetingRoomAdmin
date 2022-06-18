package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.CouponTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by root on 4/20/19.
 */
public interface CouponDao extends JpaRepository<CouponTable, Long>, JpaSpecificationExecutor<CouponTable> {

    @Query( value = "select type_id, sum(view_count)view_count from coupon group by type_id", nativeQuery = true)
    List<Object[]>  sumViewCountByType();


}
