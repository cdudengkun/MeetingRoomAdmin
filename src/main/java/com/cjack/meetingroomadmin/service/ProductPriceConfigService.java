package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.ProductPriceConfigDao;
import com.cjack.meetingroomadmin.dao.ProductPriceTypeDao;
import com.cjack.meetingroomadmin.model.ProductPriceConfigModel;
import com.cjack.meetingroomadmin.model.ProductPriceTypeModel;
import com.cjack.meetingroomadmin.table.ProductPriceConfigTable;
import com.cjack.meetingroomadmin.table.ProductPriceTypeTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductPriceConfigService {

    @Autowired
    private ProductPriceConfigDao dao;
    @Autowired
    private ProductPriceTypeDao typeDao;

    public List<ProductPriceConfigModel> list( ProductPriceConfigModel model){
        Specification<ProductPriceConfigTable> specification = handleConditon( model);
        List<ProductPriceConfigTable> tables = dao.findAll(specification);
        List<ProductPriceConfigModel> models = new ArrayList<>();
        for( ProductPriceConfigTable t : tables){
            ProductPriceConfigModel m = ModelUtils.copySignModel( t, ProductPriceConfigModel.class);
            ProductPriceTypeTable typeTable = t.getType();
            m.setTypeId( typeTable.getId());
            m.setTypeName( typeTable.getName());
            models.add( m);
        }
        return models;
    }

    public void del( String ids){
        List<ProductPriceConfigTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ProductPriceConfigTable table = new ProductPriceConfigTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( ProductPriceConfigModel model){
        ProductPriceConfigTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, ProductPriceConfigTable.class);
        }
        dao.save( table);
    }


    public List<ProductPriceTypeModel> typeList(){
        return ModelUtils.copyListModel( typeDao.findAll(), ProductPriceTypeModel.class);
    }

    public void delType( String ids){
        List<ProductPriceTypeTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            ProductPriceTypeTable table = new ProductPriceTypeTable();
            table.setId( Long.valueOf( id));
            tables.add( table);
        }
        typeDao.deleteInBatch( tables);
    }

    public void saveType( ProductPriceTypeModel model){
        ProductPriceTypeTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = typeDao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, ProductPriceTypeTable.class);
        }
        typeDao.save( table);
    }

    private Specification<ProductPriceConfigTable> handleConditon(ProductPriceConfigModel model){
        Specification<ProductPriceConfigTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getTypeId())){
                Join<ProductPriceConfigTable, ProductPriceTypeTable> join = root.join("type", JoinType.LEFT);
                predicate.getExpressions().add( cb.equal( join.get("id"), model.getTypeId()));
            }
            return predicate;
        };
        return specification;
    }

}
