package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.LayPage;
import com.cjack.meetingroomadmin.dao.CityDao;
import com.cjack.meetingroomadmin.dao.CompanyJoinInfoDao;
import com.cjack.meetingroomadmin.model.CompanyJoinInfoModel;
import com.cjack.meetingroomadmin.table.CompanyJoinInfoTable;
import com.cjack.meetingroomadmin.util.CustomerStringUtil;
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
public class CompanyJoinInfoService {

    @Autowired
    private CompanyJoinInfoDao dao;
    @Autowired
    private CityDao cityDao;

    public void list(LayPage page, CompanyJoinInfoModel condition){
        List< Sort.Order> orders=new ArrayList<>();
        orders.add( new Sort.Order( Sort.Direction.DESC, "updateTime"));
        Pageable pageable = new PageRequest( page.getPage()-1, page.getLimit(), new Sort( orders));
        Specification<CompanyJoinInfoTable> specification = handleConditon( condition);
        Page<CompanyJoinInfoTable> pageTable = dao.findAll( specification, pageable);
        List<CompanyJoinInfoModel> models = new ArrayList<>();
        for( CompanyJoinInfoTable table : pageTable.getContent()){
            CompanyJoinInfoModel model = ModelUtils.copySignModel( table, CompanyJoinInfoModel.class);
            if( table.getProvince() != null){
                model.setProvinceId( table.getProvince().getAreaCode());
                model.setProvinceName( table.getProvince().getName());
            }
            if( table.getCity() != null){
                model.setCityId( table.getCity().getAreaCode());
                model.setCityName( table.getCity().getName());
            }
            if( table.getCounty() != null){
                model.setCountyId( table.getCounty().getAreaCode());
                model.setCountyName( table.getCounty().getName());
            }
            models.add( model);
        }
        page.setData( models);
        page.setCount( Long.valueOf( pageTable.getTotalElements()).intValue());
    }

    //先删除文章段落，再删除文章
    public void del( String ids){
        List<CompanyJoinInfoTable> tables = new ArrayList<>();
        String[] idArr = ids.split( ",");
        for( String id : idArr){
            CompanyJoinInfoTable table = dao.getOne( Long.valueOf( id));
            tables.add( table);
        }
        dao.deleteInBatch( tables);
    }

    public void save( CompanyJoinInfoModel model){
        CompanyJoinInfoTable table = null;
        if( EmptyUtil.isNotEmpty( model.getId())){
            table = dao.findOne( model.getId());
            ModelUtils.copySignModel( model, table);
        }else{
            table = ModelUtils.copySignModel( model, CompanyJoinInfoTable.class);
        }
        if( EmptyUtil.isNotEmpty( model.getProvinceId())){
            table.setProvince( cityDao.getByAreaCode( model.getProvinceId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCityId())){
            table.setCity( cityDao.getByAreaCode( model.getCityId()));
        }
        if( EmptyUtil.isNotEmpty( model.getCountyId())){
            table.setCounty( cityDao.getByAreaCode( model.getCountyId()));
        }
        dao.save( table);
    }

    public void uploadImg( CompanyJoinInfoModel model){
        CompanyJoinInfoTable table = dao.findOne( model.getId());
        if( EmptyUtil.isNotEmpty( table.getLicence())){
            if( !table.getLicence().contains( model.getLicence())){
                table.setLicence( table.getLicence() + "," + model.getLicence());
            }
        }else{
            table.setLicence( model.getLicence());
        }

        dao.save( table);
    }

    public void delImg( CompanyJoinInfoModel model){
        CompanyJoinInfoTable table = dao.findOne( model.getId());
        if( EmptyUtil.isNotEmpty( table.getLicence()) && EmptyUtil.isNotEmpty( model.getLicence())){
            for( String img : model.getLicence().split( ",")){
                table.setLicence( table.getLicence().replace( img + ",", ""));
            }
        }
        dao.save( table);
    }

    private Specification<CompanyJoinInfoTable> handleConditon(CompanyJoinInfoModel model){
        Specification< CompanyJoinInfoTable> specification = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if( EmptyUtil.isNotEmpty( model.getCompanyName())){
                predicate.getExpressions().add( cb.like( root.get("companyName"), CustomerStringUtil.toLikeStr( model.getCompanyName())));
            }
            if( EmptyUtil.isNotEmpty( model.getPhone())){
                predicate.getExpressions().add( cb.like( root.get("phone"), CustomerStringUtil.toLikeStr( model.getPhone())));
            }
            return predicate;
        };
        return specification;
    }
}
