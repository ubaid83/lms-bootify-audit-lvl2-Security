package com.spts.lms.beans.StudentService;

import java.io.Serializable;

import com.spts.lms.beans.BaseBean;

public class StudentServiceBean extends BaseBean implements Serializable{
	
	private String name;
	private String payment;
	private String amount;
	private String level1;
	private String level2;
	private String level3;
	private String level4;
	private String makeAvail;
	private String duration;
	private String totalSeats;
	private String location;
	public String getTotalSeats() {
		return totalSeats;
	}


	public void setTotalSeats(String totalSeats) {
		this.totalSeats = totalSeats;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}
	BonafideForm bonafide;
	RailwayForm railway;
	HostelForm hostel;
	public HostelForm getHostel() {
		return hostel;
	}


	public void setHostel(HostelForm hostel) {
		this.hostel = hostel;
	}


	public RailwayForm getRailway() {
		return railway;
	}


	public void setRailway(RailwayForm railway) {
		this.railway = railway;
	}
	private Long serviceId;
	public Long getServiceId() {
		return serviceId;
	}


	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}


	public BonafideForm getBonafide() {
		return bonafide;
	}


	public void setBonafide(BonafideForm bonafide) {
		this.bonafide = bonafide;
	}


	public String getDuration() {
		return duration;
	}


	public void setDuration(String duration) {
		this.duration = duration;
	}


	public String getMakeAvail() {
		return makeAvail;
	}


	public void setMakeAvail(String makeAvail) {
		this.makeAvail = makeAvail;
	}


	public String getLevel4() {
		return level4;
	}


	public void setLevel4(String level4) {
		this.level4 = level4;
	}
	private String active;
	private String mapping;
	
	@Override
	public String toString() {
		return "StudentServiceBean [name=" + name + ", payment=" + payment
				+ ", amount=" + amount + ", level1=" + level1 + ", level2="
				+ level2 + ", level3=" + level3 + ", level4=" + level4
				+ ", makeAvail=" + makeAvail + ", duration=" + duration
				+ ", totalSeats=" + totalSeats + ", location=" + location
				+ ", bonafide=" + bonafide + ", railway=" + railway
				+ ", hostel=" + hostel + ", serviceId=" + serviceId
				+ ", active=" + active + ", mapping=" + mapping + "]";
	}
	
	
	public String getMapping() {
		return mapping;
	}


	public void setMapping(String mapping) {
		this.mapping = mapping;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPayment() {
		return payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getLevel1() {
		return level1;
	}
	public void setLevel1(String level1) {
		this.level1 = level1;
	}
	public String getLevel2() {
		return level2;
	}
	public void setLevel2(String level2) {
		this.level2 = level2;
	}
	public String getLevel3() {
		return level3;
	}
	public void setLevel3(String level3) {
		this.level3 = level3;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}

}
