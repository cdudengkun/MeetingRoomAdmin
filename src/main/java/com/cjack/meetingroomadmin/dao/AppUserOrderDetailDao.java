package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppUserOrderDetailTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface AppUserOrderDetailDao extends JpaRepository<AppUserOrderDetailTable, Long> {


}
