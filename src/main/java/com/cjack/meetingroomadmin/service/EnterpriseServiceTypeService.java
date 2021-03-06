package com.cjack.meetingroomadmin.service;


import com.cjack.meetingroomadmin.dao.EnterpriseServiceTypeDao;
import com.cjack.meetingroomadmin.model.EnterpriseServiceTypeModel;
import com.cjack.meetingroomadmin.table.EnterpriseServiceTypeTable;
import com.cjack.meetingroomadmin.util.EmptyUtil;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
public class EnterpriseServiceTypeService {

    @Autowired
    private EnterpriseServiceTypeDao dao;
    @Autowired
    private CouponService couponService;
    @Autowired
    private EnterpriseServiceService enterpriseServiceService;
    @Autowired
    private EnterpriseSupportService enterpriseSupportService;
    @Autowired
    private PolicyInterpretationService policyInterpretationService;

    public List<EnterpriseServiceTypeModel> list( EnterpriseServiceTypeModel model){
        List< Sort.Order> orders=new ArrayList<>();
        Sort sort = new Sort( "type");
        Specification<EnterpriseServiceTypeTable> specification = handleConditon( model);
        List<EnterpriseServiceTypeTable> tables = dao.findAll( specification, sort);
        List<EnterpriseServiceTypeModel> datas = new ArrayList<>();
        for( EnterpriseServiceTypeTable table : tables){
            EnterpriseServiceTypeModel data = ModelUtils.copySignModel( table, EnterpriseServiceTypeModel.class);
            datas.add( data);
        }
        return datas;
    }

    public void del( String ids){
        List<EnterpriseServiceTypeTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            Long typeId = Long.valueOf( id);
            EnterpriseServiceTypeTable table = new EnterpriseServiceTypeTable();
            table.setId( typeId);
            tables.add( table);
            //?????????????????????type???????????????
            couponService.delType( typeId);
            enterpriseServiceService.delType( typeId);
            enterpriseSupportService.delType( typeId);
            policyInterpretationService.delType( typeId);
        }
        dao.deleteInBatch( tables);
    }

    public void save( EnterpriseServiceTypeModel model){
        EnterpriseServiceTypeTable table;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, EnterpriseServiceTypeTable.class);
        }
        dao.save( table);
    }

    private Specification<EnterpriseServiceTypeTable> handleConditon( EnterpriseServiceTypeModel model){
        Specification< EnterpriseServiceTypeTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getName())){
                predicate.getExpressions().add( cb.equal( root.get("name"), model.getName()));
            }
            if( EmptyUtil.isNotEmpty( model.getType())){
                predicate.getExpressions().add( cb.equal( root.get("type"), model.getType()));
            }
            if( EmptyUtil.isNotEmpty( model.getLevel())){
                predicate.getExpressions().add( cb.equal( root.get("level"), model.getLevel()));
            }
            return predicate;
        };
        return specification;
    }

}
