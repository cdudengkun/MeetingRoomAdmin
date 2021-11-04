package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.util.Date;

/**
 * 课本单元单词信息
 */
@Data
public class BookUnitWordModel {

    private Long bookId;

    private Long id;
    private Date createTime;
    private Date updateTime;
    private String bookName;
    private String unitName;
    private Long wordId;
    private String aboutWords;
    private String americanPronunciation;
    private String assistantNotation;
    private String englishExample1;
    private String englishExample2;
    private String englishPronunciation;
    private String exampleTranslation1;
    private String exampleTranslation2;
    private String interpretation;
    private String phoneticSymbol;
    private String rootAffixes;
    private String spare1;
    private String spare2;
    private String word;
    private String wordAudioUrl;
    private String syllabification;
    private String usaAudioUrl;
}
