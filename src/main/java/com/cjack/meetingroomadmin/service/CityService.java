package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.dao.CityDao;
import com.cjack.meetingroomadmin.model.CityModel;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {

    @Autowired
    private CityDao dao;

    public List< CityModel> list( Long parentId){
        return ModelUtils.copyListModel( dao.findByParentId( parentId), CityModel.class);
    }
}
