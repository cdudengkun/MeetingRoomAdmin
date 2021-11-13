package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.DefineKeyValueTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 4/20/19.
 */
public interface DefineKeyValueDao extends JpaRepository<DefineKeyValueTable, Long> {

    List<DefineKeyValueTable> findByType(Integer type);

}
