package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.BookUnitWordTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface BookUnitWordDao extends JpaRepository<BookUnitWordTable, Long>, JpaSpecificationExecutor<BookUnitWordTable> {


}
