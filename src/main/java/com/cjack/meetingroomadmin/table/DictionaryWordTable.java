package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 词典单词信息表
 */
@Entity
@Table(name="dictionary_word")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class DictionaryWordTable {

    public DictionaryWordTable(){

    }

    public DictionaryWordTable( Long id){
        this.id = id;
    }
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "unit_id")
    private BookUnitTable unit;

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
