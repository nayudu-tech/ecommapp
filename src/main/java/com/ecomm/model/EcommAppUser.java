package com.ecomm.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ecomm_user")
public class EcommAppUser {

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "eca_id")
	private Integer ecaId;
	@Column(name = "eca_email")
	private String userEmail;
	@Column(name = "eca_first_name")
	private String firstName;
	@Column(name = "eca_last_name")
	private String lastName;
	@Column(name = "eca_password")
	private String password;
	@Column(name = "eca_mobile_no")
	private String mobileNo;
	@Column(name = "eca_last_login_date")
	private Timestamp lastLoginDate;
	@Column(name = "eca_activation_date")
	private Timestamp activationDate;
	
	public Integer getEcaId() {
		return ecaId;
	}
	public void setEcaId(Integer ecaId) {
		this.ecaId = ecaId;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Timestamp getActivationDate() {
		return activationDate;
	}
	public void setActivationDate(Timestamp activationDate) {
		this.activationDate = activationDate;
	}
	public Timestamp getLastLoginDate() {
		return lastLoginDate;
	}
	public void setLastLoginDate(Timestamp lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
}
