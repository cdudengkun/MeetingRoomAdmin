package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.MeetingZoneTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface MeetingZoneDao extends JpaRepository<MeetingZoneTable, Long>, JpaSpecificationExecutor<MeetingZoneTable> {


}
