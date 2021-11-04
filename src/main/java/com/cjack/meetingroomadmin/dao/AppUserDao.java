package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AppUserTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * Created by root on 4/20/19.
 */
public interface AppUserDao extends JpaRepository<AppUserTable, Long>, JpaSpecificationExecutor<AppUserTable> {

    public AppUserTable getByUid(String uid);

    public List<AppUserTable> getByRegistType(Integer registType);
}
