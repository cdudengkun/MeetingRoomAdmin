package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.TestBookTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface TestBookDao extends JpaRepository<TestBookTable, Long>{


}
