package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.FamousTeacherTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface FamousTeacherDao extends JpaRepository<FamousTeacherTable, Long> {


}
