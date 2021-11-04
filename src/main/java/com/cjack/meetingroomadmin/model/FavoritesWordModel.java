package com.cjack.meetingroomadmin.model;

import com.cjack.meetingroomadmin.table.DictionaryWordTable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import java.util.Date;

/**
 * App用户收藏的单词
 */
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class FavoritesWordModel {

    private Long id;
    private Date createTime;
    private Date updateTime;

    private DictionaryWordTable word;//关联单词

    private AppUserModel appUser;// 所属用户
}
