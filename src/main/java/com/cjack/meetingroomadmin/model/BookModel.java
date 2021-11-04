package com.cjack.meetingroomadmin.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * 课本信息表
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class BookModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private String bookName;//课本名称
    private Integer downLoadNum;//课本下载记录
    private Integer wordsNum;//课本单词数
    private String versionName;
    private Long versionId;
    private String gradeName;
    private Long gradeId;
    private String stageName;
    private Long stageId;

    private String coverImg;//封面图片地址
    private Double price;//课本价格


    private Long dealerId;

    private Long schoolId;
}
