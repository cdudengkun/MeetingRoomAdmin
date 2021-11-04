package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePassWordModel implements Serializable {

    private Long id;

    private String newPassword;
    private String oldPassword;
}
