package com.cjack.meetingroomadmin.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class AppUserUidExcel {

    @Excel(name = "UID", orderNum = "0")
    private String uid;

    @Excel(name="密码",orderNum = "1")
    private String password;

    @Excel(name="使用学校",orderNum = "2")
    private String schoolName;

    @Excel(name="生成时间",orderNum = "3" )
    private String createTime;
}
