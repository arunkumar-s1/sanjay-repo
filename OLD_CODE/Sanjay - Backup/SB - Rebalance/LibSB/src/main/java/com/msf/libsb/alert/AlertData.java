package com.msf.libsb.alert;

public class AlertData {
	
	String sUserID;
	String sOrderID;
	String sSymbol;
	String sPrice;
	String sAlertPrice;
	String sQuantity;
	String sAlertID;
	String sStatus;
	String alertType;
	String sSide;
	String sProdType;
	String sSegment;
	
	public String getAlertType() {
		return alertType;
	}
	public void setAlertType(String alertType) {
		this.alertType = alertType;
	}
	public String getsUserID() {
		return sUserID;
	}
	public void setsUserID(String sUserID) {
		this.sUserID = sUserID;
	}
	public String getsOrderID() {
		return sOrderID;
	}
	public void setsOrderID(String sOrderID) {
		this.sOrderID = sOrderID;
	}
	public String getsSymbol() {
		return sSymbol;
	}
	public void setsSymbol(String sSymbol) {
		this.sSymbol = sSymbol;
	}
	public String getsPrice() {
		return sPrice;
	}
	public void setsPrice(String sPrice) {
		this.sPrice = sPrice;
	}
	public String getsAlertPrice() {
		return sAlertPrice;
	}
	public void setsAlertPrice(String sAlertPrice) {
		this.sAlertPrice = sAlertPrice;
	}
	public String getsQuantity() {
		return sQuantity;
	}
	public void setsQuantity(String sQuantity) {
		this.sQuantity = sQuantity;
	}
	public String getsAlertID() {
		return sAlertID;
	}
	public void setsAlertID(String sAlertID) {
		this.sAlertID = sAlertID;
	}
	public String getsStatus() {
		return sStatus;
	}
	public void setsStatus(String sStatus) {
		this.sStatus = sStatus;
	}
	
	public void setSide(String side) {
		this.sSide = side;
	}
	
	public String getSide() {
		return this.sSide;
	}
	
	public void setProdType(String type) {
		this.sProdType = type;
	}
	
	public String getProdType() {
		return this.sProdType;
	}
	
	public void setSegment(String segment) {
		this.sSegment = segment;
	}
	
	public String getSegment() {
		return this.sSegment;
	}
}
