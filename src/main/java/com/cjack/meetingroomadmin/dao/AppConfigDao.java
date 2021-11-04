package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppConfigTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface AppConfigDao extends JpaRepository<AppConfigTable, Long> {


}
