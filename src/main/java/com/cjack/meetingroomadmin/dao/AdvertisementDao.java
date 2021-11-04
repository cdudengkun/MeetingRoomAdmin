package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AdvertisementTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface AdvertisementDao extends JpaRepository<AdvertisementTable, Long> {


}
