package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 词库单词信息
 */
@Data
public class DictionaryWordModel {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private String aboutWords;//相关词
    private String americanPronunciation;//美式发音
    private String assistantNotation;//助记法
    private String englishExample1;//英文例句1
    private String englishExample2;//英文例句2
    private String englishPronunciation;//英式发音
    private String exampleTranslation1;//例句翻译1
    private String exampleTranslation2;//例句翻译2
    private String interpretation;//释义
    private String phoneticSymbol;//音标
    private String rootAffixes;//词根词缀
    private String spare1;//备用1
    private String spare2;//备用2
    private String word;//单词
    private String wordAudioUrl;//
    private String syllabification;//单词划分
    private String usaAudioUrl;//
}
