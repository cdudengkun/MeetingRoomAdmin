package com.cjack.meetingroomadmin.service;

import cn.jiguang.common.resp.DefaultResult;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import com.alibaba.fastjson.JSON;
import com.cjack.meetingroomadmin.config.CommonConfig;
import com.cjack.meetingroomadmin.exception.JPushException;
import com.google.gson.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class JpushService{

    private final static Logger LOG = LogManager.getLogger( JpushService.class);

    @Value("${app_key}")
    private String APP_KEY ;

    @Value("${master_secret}")
    private String MASTER_SECRET ;



    private JPushClient createJpushClient(){
        JPushClient jPushClient = new JPushClient(MASTER_SECRET, APP_KEY);
        return  jPushClient ;
    }

    public void pushForAll( String msg, String title, JsonObject jsonExtras) throws JPushException {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform( Platform.android_ios())
                .setAudience( Audience.all())
                .setNotification( Notification.newBuilder()
                        .setAlert( msg)
                        .addPlatformNotification( AndroidNotification.newBuilder()
                                .setTitle(title)
                                .addExtra( CommonConfig.JPUSH_JSON_EXTRAS_KEY, jsonExtras)
                                .build())
                        .addPlatformNotification( IosNotification.newBuilder()
                                .incrBadge( 1)
                                .addExtra( CommonConfig.JPUSH_JSON_EXTRAS_KEY, jsonExtras)
                                .build())
                        .build())
                .build();
        PushResult pushResult;
        try {
            pushResult = createJpushClient().sendPush(payload);
        } catch (Exception e) {
            LOG.error( e.getMessage(), e);
            throw new JPushException();
        }
        LOG.info( "--------------pushForAll RES:" + JSON.toJSON( pushResult));
        if( pushResult.getResponseCode() != 200){
            throw new JPushException();
        }
    }

    /**
     * 添加学员到班级标签
     * @param schoolClassId
     * @param user
     * @throws JPushException
     */
    public void addUserToTag( String schoolClassId, String user) throws JPushException {
        DefaultResult result;
        try {
            Set<String> addUsers = new HashSet<>();
            addUsers.add( user);
            Set<String> delUsers = new HashSet<>();
            result = createJpushClient().addRemoveDevicesFromTag( schoolClassId, addUsers, delUsers);
        } catch ( Exception e) {
            LOG.error( e.getMessage(), e);
            throw new JPushException();
        }
        LOG.info( "--------------addUserToTag RES:" + JSON.toJSON( result));
        if( result.getResponseCode() != 200){
            throw new JPushException();
        }
    }

    /**
     * 将学员从班级标签中移除
     * @param schoolClassId
     * @param user
     * @throws JPushException
     */
    public void delUserToTag( String schoolClassId, String user) throws JPushException {
        DefaultResult result;
        try {
            Set<String> addUsers = new HashSet<>();
            Set<String> delUsers = new HashSet<>();
            delUsers.add( user);
            result = createJpushClient().addRemoveDevicesFromTag( schoolClassId, addUsers, delUsers);
        } catch ( Exception e) {
            LOG.error( e.getMessage(), e);
            throw new JPushException();
        }
        LOG.info( "--------------addUserToTag RES:" + JSON.toJSON( result));
        if( result.getResponseCode() != 200){
            throw new JPushException();
        }
    }

    public void pushForTag( String title, String content, String tag) throws JPushException {
        PushPayload payload = PushPayload.newBuilder()
                .setPlatform( Platform.android_ios())
                .setAudience( Audience.tag( tag))
                .setNotification( Notification.newBuilder()
                        .setAlert( content)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle( title)
                                .build())
                        .addPlatformNotification( IosNotification.newBuilder()
                                .incrBadge( 1)
                                .build())
                        .build())
                .build();
        PushResult pushResult;
        try {
            pushResult = createJpushClient().sendPush(payload);
        } catch ( Exception e) {
            LOG.error( e.getMessage(), e);
            throw new JPushException();
        }
        LOG.info( "--------------pushForTag RES:" + JSON.toJSON( pushResult));
        if( pushResult.getResponseCode() != 200){
            throw new JPushException();
        }
    }
}
