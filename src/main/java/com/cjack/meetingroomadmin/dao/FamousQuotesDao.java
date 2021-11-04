package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.FamousQuotesTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface FamousQuotesDao extends JpaRepository<FamousQuotesTable, Long> {


}
