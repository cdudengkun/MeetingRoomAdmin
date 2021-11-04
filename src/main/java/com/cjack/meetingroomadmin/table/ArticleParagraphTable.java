package com.cjack.meetingroomadmin.table;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 阅读精选-段落（包含中文与译文）
 * @author sww
 * @create 2020-01-13 17:18
 */
@Entity
@Table(name="article_paragraph")
@Data
public class ArticleParagraphTable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id ;
    private Date createTime;
    private Date updateTime;

    @ManyToOne
    @JoinColumn(name="`article_id`")
    private ArticleTable article;

    private String original;//原文
    private String translation;//翻译

}
