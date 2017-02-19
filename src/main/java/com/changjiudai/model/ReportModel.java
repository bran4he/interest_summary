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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((capital == null) ? 0 : capital.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((interest == null) ? 0 : interest.hashCode());
		result = prime * result + ((total == null) ? 0 : total.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportModel other = (ReportModel) obj;
		if (capital == null) {
			if (other.capital != null)
				return false;
		} else if (!capital.equals(other.capital))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (interest == null) {
			if (other.interest != null)
				return false;
		} else if (!interest.equals(other.interest))
			return false;
		if (total == null) {
			if (other.total != null)
				return false;
		} else if (!total.equals(other.total))
			return false;
		return true;
	}
	
	
}
