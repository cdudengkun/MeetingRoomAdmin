package com.cjack.meetingroomadmin.table;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 阅读精选
 * @author sww
 * @create 2020-01-13 17:18
 */
@Entity
@Table(name="article")
@Data
public class ArticleTable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id ;
    private Date createTime;
    private Date updateTime;

    //作者
    private String author;

    //用户名下的收货地址列表
    @OneToMany( cascade = {CascadeType.REFRESH, CascadeType.MERGE},mappedBy = "article" , fetch = FetchType.LAZY)
    @OrderBy("id DESC")
    private List<ArticleParagraphTable> paragraphs;

    private String img;

    //标题
    private String title;

    //简介
    private String introduction;

    //类型,1:小学,2:初中,3:高中
    private Integer type;

}
