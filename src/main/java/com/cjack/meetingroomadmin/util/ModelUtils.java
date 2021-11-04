package com.cjack.meetingroomadmin.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.cjack.meetingroomadmin.exception.CommonException;
import com.cjack.meetingroomadmin.model.AdminUserModel;
import com.cjack.meetingroomadmin.table.AdminUserTable;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将db对应的table实体转化为供前端页面使用的model对象
 * Created by root on 6/23/19.
 */
public final class ModelUtils {

    public static <T> T copySignModel( Object source, Class< T> destClass) {
        T dest = null;
        try {
            dest = destClass.newInstance();
            BeanUtils.copyProperties( dest, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return dest;
    }

    public static void copySignModel( Object source, Object dest) {
        BeanUtil.copyProperties( source, dest, true, CopyOptions.create().setIgnoreNullValue( true).setIgnoreError( true));
    }

    public static <T> List<T> copyListModel( List sources, Class< T> destClass) {
        List< T> dests = new ArrayList<>( sources.size());
        try {
            for( Object source : sources){
                T dest = destClass.newInstance();
                BeanUtils.copyProperties( dest, source);
                dests.add( dest);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return dests;
    }

    public static void main(String[] args) throws CommonException {
        AdminUserTable table = new AdminUserTable();
        table.setPassWord( "123456");

        AdminUserModel model = ModelUtils.copySignModel( table, AdminUserModel.class);
        System.out.println( model.getName() + "," + model.getPassWord());
    }
}
