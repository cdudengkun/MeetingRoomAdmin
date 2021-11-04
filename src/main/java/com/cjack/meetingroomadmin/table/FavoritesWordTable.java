package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * App用户收藏的单词
 */
@Entity
@Table(name="favorites_word")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
public class FavoritesWordTable {
    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    @ManyToOne( cascade={CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "word_id")
    private DictionaryWordTable word;//关联单词

    @OneToOne( cascade={CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户
}
