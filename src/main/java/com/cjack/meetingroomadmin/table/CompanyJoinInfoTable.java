package com.cjack.meetingroomadmin.table;

import lombok.Data;

import javax.persistence.*;

/**
 * 公司加入申请表
 */
@Entity
@Table(name="company_join_info", catalog = "meeting_room")
@Data
public class CompanyJoinInfoTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Long createTime;
    private Long updateTime;

    @ManyToOne
    @JoinColumn(name="province_id")
    private CityTable province;
    @ManyToOne
    @JoinColumn(name="city_id")
    private CityTable city;
    @ManyToOne
    @JoinColumn(name="county_id")
    private CityTable county;
    private String detail;


    private String companyName;//公司名称
    private String name;//联系人
    private String phone;//联系电话
    private String licence;//营业执照列表，多个英文逗号分割
}
