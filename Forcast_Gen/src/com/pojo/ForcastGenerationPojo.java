package com.pojo;

import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ForcastGenerationPojo implements Comparable<ForcastGenerationPojo>{
	private String part;
	private String date;
	private String actual;

	public ForcastGenerationPojo(String part, String date, String actual) {
		this.part = part;
		this.date = date;
		this.actual = actual;
	}

	public String getPart() {
		return part;
	}

	public String getDate() {
		return date;
	}

	public String getActual() {
		return actual;
	}

	public void setPart(String part) {
		this.part = part;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}
	
	@Override
	  public int compareTo(ForcastGenerationPojo fgp){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
		Date thisDate=null;
		Date incomingDate=null;
		if(getDate().equalsIgnoreCase("date")){
			return 1;
		}
		try{
		thisDate=formatter.parse(getDate());
		incomingDate=formatter.parse(fgp.getDate());
		}catch(Exception e){
			try {
				throw new Exception("Error in Mapper");
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return thisDate.compareTo(incomingDate);
	  }

	@Override
	public String toString() {
		return "ForcastGenerationPojo [part=" + part + ", date=" + date + ", actual=" + actual + "]";
	}
	
	
	
	
	
	
	/*@Override
	public int compareTo(ForcastGenerationPojo fgp) {
		try{
		// TODO Auto-generated method stub
		return Integer.parseInt(this.date)-Integer.parseInt(fgp.getDate());
		}catch(Exception ne){
			System.out.println(ne.getLocalizedMessage());
		}
		return 0;
	}*/
}
