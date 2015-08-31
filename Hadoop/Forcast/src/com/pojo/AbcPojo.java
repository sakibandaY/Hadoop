package com.pojo;


public class AbcPojo implements Comparable<AbcPojo> {
	private double newprice;
	private double newvolume;
	private double newspend;
	private double newcriticality;
	private double newvalocity;
	private double newindex;
	private double hml;
	
	private String price;
	private String volume;
	private String spend;
	private String criticality;
	private String valocity;
	
	public AbcPojo() {
	}
	
	public AbcPojo(double newprice, double newvolume, double newspend, double newcriticality, double newvalocity,
			double newindex, String price, String volume, String spend, String criticality,
			String valocity) {
		super();
		this.newprice = newprice;
		this.newvolume = newvolume;
		this.newspend = newspend;
		this.newcriticality = newcriticality;
		this.newvalocity = newvalocity;
		this.newindex = newindex;
		this.price = price;
		this.volume = volume;
		this.spend = spend;
		this.criticality = criticality;
		this.valocity = valocity;
	}

	public double getNewprice() {
		return newprice;
	}
	public void setNewprice(double newprice) {
		this.newprice = newprice;
	}
	public double getNewvolume() {
		return newvolume;
	}
	public void setNewvolume(double newvolume) {
		this.newvolume = newvolume;
	}
	public double getNewspend() {
		return newspend;
	}
	public void setNewspend(double newspend) {
		this.newspend = newspend;
	}
	public double getNewcriticality() {
		return newcriticality;
	}
	public void setNewcriticality(double newcriticality) {
		this.newcriticality = newcriticality;
	}
	public double getNewvalocity() {
		return newvalocity;
	}
	public void setNewvalocity(double newvalocity) {
		this.newvalocity = newvalocity;
	}
	public double getNewindex() {
		return newindex;
	}
	public void setNewindex(double newindex) {
		this.newindex = newindex;
	}
	public double getHml() {
		return hml;
	}
	public void setHml(double hml) {
		this.hml = hml;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public String getSpend() {
		return spend;
	}
	public void setSpend(String spend) {
		this.spend = spend;
	}
	public String getCriticality() {
		return criticality;
	}
	public void setCriticality(String criticality) {
		this.criticality = criticality;
	}
	public String getValocity() {
		return valocity;
	}
	public void setValocity(String valocity) {
		this.valocity = valocity;
	}

	public int compareTo(AbcPojo value) {
		return this.getNewindex() < value.getNewindex()? 1 : (this.getNewindex() > value.getNewindex() ? -1 : 0);
	}

	
}
