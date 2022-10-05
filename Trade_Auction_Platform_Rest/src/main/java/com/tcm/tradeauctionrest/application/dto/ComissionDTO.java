package com.tcm.tradeauctionrest.application.dto;

import java.util.Date;

public class ComissionDTO {

	private String type;
    private Date dateP;
    private float euros;
    private float comission;

    public ComissionDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDateP() {
        return dateP;
    }

    public void setDateP(Date dateP) {
        this.dateP = dateP;
    }

    public float getEuros() {
        return euros;
    }

    public void setEuros(float euros) {
        this.euros = euros;
    }

    public float getComission() {
        return comission;
    }

    public void setComission(float comission) {
        this.comission = comission;
    }
}
