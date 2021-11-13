package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.MeetingRoomReservationTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by root on 4/20/19.
 */
public interface MeetingRoomReservationDao extends JpaRepository<MeetingRoomReservationTable, Long>, JpaSpecificationExecutor<MeetingRoomReservationTable> {


}
