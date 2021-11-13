package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.AppUserOrderDao;
import com.cjack.meetingroomadmin.model.AppUserOrderModel;
import com.cjack.meetingroomadmin.table.AppUserOrderTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private AppUserOrderDao dao;

    public void list( LayPage page, AppUserOrderModel model){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<AppUserOrderTable> specification = handleConditon( model);
        Page<AppUserOrderTable> pageTable = dao.findAll( specification, pageable);
        List<AppUserOrderModel> datas = new ArrayList<>();
        for( AppUserOrderTable table : pageTable.getContent()){
            AppUserOrderModel data = ModelUtils.copySignModel( table, AppUserOrderModel.class);

            datas.add( data);
        }
        page.setData( datas);
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }


    public void updateOrderStatus( AppUserOrderModel model){
        AppUserOrderTable order = dao.getOne( model.getId());
        order.setStatus( model.getStatus());
        dao.save( order);
    }

    /**
     * 组装查询条件
     * @param model
     * @return
     */
    private Specification<AppUserOrderTable> handleConditon( AppUserOrderModel model ){
        Specification< AppUserOrderTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getStatus())){
                predicate.getExpressions().add( cb.equal( root.get("status"), model.getStatus()));
            }
            return predicate;
        };
        return specification;
    }
}
