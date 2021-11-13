package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.VerificationCodeTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface VerificationCodeDao extends JpaRepository<VerificationCodeTable, Long> {

    VerificationCodeTable getFirstByPhoneOrderByCreateTimeDesc(String phone);
}
