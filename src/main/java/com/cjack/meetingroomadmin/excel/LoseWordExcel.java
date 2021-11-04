package com.cjack.meetingroomadmin.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

@Data
public class LoseWordExcel {

    @Excel(name = "词库缺失单词", orderNum = "0")
    private String word;
}
