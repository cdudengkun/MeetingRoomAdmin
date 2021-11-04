package com.cjack.meetingroomadmin.table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 收获地址信息
 */
@Entity
@Table(name="receiver_address")
@Data
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class ReceiverAddressTable {

    @Id
    @GeneratedValue( strategy= GenerationType.AUTO)
    private Long id;
    private Date createTime;
    private Date updateTime;

    private String phone;//收件人电话
    private String receiverName;//收件人姓名

    @ManyToOne( cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn( name = "app_user_id")
    private AppUserTable appUser;// 所属用户

    @ManyToOne
    @JoinColumn(name="province_id", columnDefinition="int(20)")
    private CityTable province;
    @ManyToOne
    @JoinColumn(name="city_id", columnDefinition="int(20)")
    private CityTable city;
    @ManyToOne
    @JoinColumn(name="country_id", columnDefinition="int(20)")
    private CityTable country;
    private String address;
}
