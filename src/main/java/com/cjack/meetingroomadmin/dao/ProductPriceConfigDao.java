package com.cjack.meetingroomadmin.dao;

import com.cjack.meetingroomadmin.table.ProductPriceConfigTable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by root on 4/20/19.
 */
public interface ProductPriceConfigDao extends JpaRepository<ProductPriceConfigTable, Long> {

}
