package com.cjack.meetingroomadmin;


import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by root on 4/4/19.
 */
@SpringBootApplication
@EnableTransactionManagement
public class MeetingRoomAdminApplication {

    private static final Logger logger = Logger.getLogger(MeetingRoomAdminApplication.class);

    public static void main(String[] args) {
        new SpringApplicationBuilder( MeetingRoomAdminApplication.class).web(true).run(args);
        logger.info("Application started");
    }

}
