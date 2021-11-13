package com.cjack.meetingroomadmin.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class UpdatePassWordModel  extends BaseModel{

    private String newPassword;
    private String oldPassword;
}
