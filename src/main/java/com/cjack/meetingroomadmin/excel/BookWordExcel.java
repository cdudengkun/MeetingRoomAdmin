package com.cjack.meetingroomadmin.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

/**
 * 课本的单词excel类
 *
 **/
@Data
public class BookWordExcel {

    @Excel(name = "序号", orderNum = "0")
    private String orderNum;

    @Excel(name = "阶段",orderNum = "1")
    private String stage ;

    @Excel(name="版本",orderNum = "2")
    private String verison ;

    @Excel(name = "课本名称",orderNum = "3")
    private String bookName ;

    @Excel(name="单元名称",orderNum = "4")
    private String unitName ;

    @Excel(name="单词",orderNum = "5")
    private String word ;

    /*释义*/
    @Excel(name="释义",orderNum = "6")
    String interpretation ;

    @Excel(name="英文例句1",orderNum = "7")
    String englishExample1 ;

    @Excel(name = "例句翻译1",orderNum = "8")
    String exampleTranslation1 ;


    @Excel(name="英文例句2",orderNum = "9")
    String englishExample2 ;

    @Excel(name = "例句翻译2",orderNum = "10")
    String exampleTranslation2 ;

    /**
     * 助记法
     */
    @Excel(name = "助记法",orderNum = "11")
    String assistantNotation ;

    /**
     * 词根词缀
     */
    @Excel(name = "词根词缀",orderNum = "12")
    String rootAffixes ;


    @Excel(name="相关词",orderNum = "13")
    String aboutWords ;
    /**
     * 备用1
     */
    @Excel(name="备用1",orderNum = "14")
    String spare1;
    /**
     * 备用2
     */
    @Excel(name="备用2",orderNum = "15")
    String spare2 ;
}
