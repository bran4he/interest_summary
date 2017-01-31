package com.changjiudai.bean;

import java.util.ArrayList;
import java.util.List;

public class ReportData {
	List<String> datelst = new ArrayList<String>();
	List<Long> totallst = new ArrayList<Long>();
	List<Long> capitallst = new ArrayList<Long>();
	List<Long> interestlst = new ArrayList<Long>();
	public List<String> getDatelst() {
		return datelst;
	}
	public void setDatelst(List<String> datelst) {
		this.datelst = datelst;
	}
	public List<Long> getTotallst() {
		return totallst;
	}
	public void setTotallst(List<Long> totallst) {
		this.totallst = totallst;
	}
	public List<Long> getCapitallst() {
		return capitallst;
	}
	public void setCapitallst(List<Long> capitallst) {
		this.capitallst = capitallst;
	}
	public List<Long> getInterestlst() {
		return interestlst;
	}
	public void setInterestlst(List<Long> interestlst) {
		this.interestlst = interestlst;
	}
	public ReportData(List<String> datelst, List<Long> totallst,
			List<Long> capitallst, List<Long> interestlst) {
		super();
		this.datelst = datelst;
		this.totallst = totallst;
		this.capitallst = capitallst;
		this.interestlst = interestlst;
	}
	public ReportData() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	
}
