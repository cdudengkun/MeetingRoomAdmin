package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.BookTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface BookDao extends JpaRepository<BookTable, Long>, JpaSpecificationExecutor<BookTable> {


}
