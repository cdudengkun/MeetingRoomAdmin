package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.AdvertisementTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by root on 4/20/19.
 */
public interface AdvertisementDao extends JpaRepository<AdvertisementTable, Long> {

    public List<AdvertisementTable> getByPosition(Integer position);
}
