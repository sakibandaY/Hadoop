package test;

import java.util.Date;

/**
 * @author yadagirinarasimha.sa
 *
 */
public class RecipientUtill {

	private String channelCode;
	private String copyType;
	private String id;
	private String name;
	private String fax;
	private String phone;
	private String email;
	private String reportingLanguage;
	private String country;
	private String addressline1;
	private String addressline2;
	private String addressline3;
	private String addressline4;
	private String addressline5;
	private String addressline6;
	private String addressline7;
	private Date expirydate;
	public String getChannelCode() {
		return channelCode;
	}
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}
	
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCopyType() {
		return copyType;
	}
	public void setCopyType(String copyType) {
		this.copyType = copyType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getReportingLanguage() {
		return reportingLanguage;
	}
	public void setReportingLanguage(String reportingLanguage) {
		this.reportingLanguage = reportingLanguage;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getAddressline1() {
		return addressline1;
	}
	public void setAddressline1(String addressline1) {
		this.addressline1 = addressline1;
	}
	public String getAddressline2() {
		return addressline2;
	}
	public void setAddressline2(String addressline2) {
		this.addressline2 = addressline2;
	}
	public String getAddressline3() {
		return addressline3;
	}
	public void setAddressline3(String addressline3) {
		this.addressline3 = addressline3;
	}
	public String getAddressline4() {
		return addressline4;
	}
	public void setAddressline4(String addressline4) {
		this.addressline4 = addressline4;
	}
	public String getAddressline5() {
		return addressline5;
	}
	public void setAddressline5(String addressline5) {
		this.addressline5 = addressline5;
	}
	public String getAddressline6() {
		return addressline6;
	}
	public void setAddressline6(String addressline6) {
		this.addressline6 = addressline6;
	}
	public String getAddressline7() {
		return addressline7;
	}
	public void setAddressline7(String addressline7) {
		this.addressline7 = addressline7;
	}
	public Date getExpirydate() {
		return expirydate;
	}
	public void setExpirydate(Date expirydate) {
		this.expirydate = expirydate;
	}
	@Override
	public String toString() {
		return "RecipientUtill [channelCode=" + channelCode + ", copyType=" + copyType + ", id=" + id + ", name=" + name
				+ ", fax=" + fax + ", phone=" + phone + ", email=" + email + ", reportingLanguage=" + reportingLanguage
				+ ", country=" + country + ", addressline1=" + addressline1 + ", addressline2=" + addressline2
				+ ", addressline3=" + addressline3 + ", addressline4=" + addressline4 + ", addressline5=" + addressline5
				+ ", addressline6=" + addressline6 + ", addressline7=" + addressline7 + ", expirydate=" + expirydate
				+ "]";
	}	
	
}
