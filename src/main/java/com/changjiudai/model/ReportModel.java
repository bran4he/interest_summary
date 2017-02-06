package com.changjiudai.model;

import java.io.Serializable;

public class ReportModel implements Serializable{

	private static final long serialVersionUID = 2420573502553342851L;
	
	private String date;
	private Long total;
	private Long capital;
	private Long interest;
	
	public ReportModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	public ReportModel(String date, Long total, Long capital, Long interest) {
		super();
		this.date = date;
		this.total = total;
		this.capital = capital;
		this.interest = interest;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Long getTotal() {
		return total;
	}
	public void setTotal(Long total) {
		this.total = total;
	}
	public Long getCapital() {
		return capital;
	}
	public void setCapital(Long capital) {
		this.capital = capital;
	}
	public Long getInterest() {
		return interest;
	}
	public void setInterest(Long interest) {
		this.interest = interest;
	}
	
	
}
