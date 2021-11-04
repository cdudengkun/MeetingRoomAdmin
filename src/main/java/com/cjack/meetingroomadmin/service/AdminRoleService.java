package com.cjack.meetingroomadmin.service;

import com.cjack.meetingroomadmin.config.PrivageConfig;
import com.cjack.meetingroomadmin.dao.AdminRoleDao;
import com.cjack.meetingroomadmin.model.AdminRoleModel;
import com.cjack.meetingroomadmin.util.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRoleService {

    @Autowired
    private AdminRoleDao dao;

    public List<AdminRoleModel> list(){
        return ModelUtils.copyListModel( dao.findByCodeIsNot( PrivageConfig.SUPER_ADMIN_CODE), AdminRoleModel.class);
    }
}
