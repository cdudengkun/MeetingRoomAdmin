package com.cjack.meetingroomadmin.dao;


import com.cjack.meetingroomadmin.table.CityTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 全国省市区县列表
 * Created by root on 10/5/19
 */
public interface CityDao extends JpaRepository<CityTable, Long> {

    List<CityTable> findByParentId(Long parentId);
}
