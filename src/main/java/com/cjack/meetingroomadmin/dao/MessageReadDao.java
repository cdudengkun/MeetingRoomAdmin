package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.MessageReadTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface MessageReadDao extends JpaRepository<MessageReadTable, Long>, JpaSpecificationExecutor<MessageReadTable> {


}
