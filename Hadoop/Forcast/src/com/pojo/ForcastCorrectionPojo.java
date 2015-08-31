package com.pojo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ForcastCorrectionPojo implements Comparable<ForcastCorrectionPojo>{

	private String pART;
	private String fno;
	private String actual;
	private String w0;
	private String w1;
	private String w2;
	private String w3;
	private String w4;
	private String w5;
	private String w6;
	private String w7;

	public ForcastCorrectionPojo(String pART, String fno, String actual, String w0, String w1, String w2, String w3, String w4,
			String w5, String w6, String w7) {
		this.pART = pART;
		this.fno = fno;
		this.actual = actual;
		this.w0 = w0;
		this.w1 = w1;
		this.w2 = w2;
		this.w3 = w3;
		this.w4 = w4;
		this.w5 = w5;
		this.w6 = w6;
		this.w7 = w7;
	}

	public String getpART() {
		return pART;
	}

	public void setpART(String pART) {
		this.pART = pART;
	}

	public String getFno() {
		return fno;
	}

	public void setFno(String fno) {
		this.fno = fno;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getW0() {
		return w0;
	}

	public void setW0(String w0) {
		this.w0 = w0;
	}

	public String getW1() {
		return w1;
	}

	public void setW1(String w1) {
		this.w1 = w1;
	}

	public String getW2() {
		return w2;
	}

	public void setW2(String w2) {
		this.w2 = w2;
	}

	public String getW3() {
		return w3;
	}

	public void setW3(String w3) {
		this.w3 = w3;
	}

	public String getW4() {
		return w4;
	}

	public void setW4(String w4) {
		this.w4 = w4;
	}

	public String getW5() {
		return w5;
	}

	public void setW5(String w5) {
		this.w5 = w5;
	}

	public String getW6() {
		return w6;
	}

	public void setW6(String w6) {
		this.w6 = w6;
	}

	public String getW7() {
		return w7;
	}

	public void setW7(String w7) {
		this.w7 = w7;
	}

	@Override
	public int compareTo(ForcastCorrectionPojo fp) {
		try{
		// TODO Auto-generated method stub
		return Integer.parseInt(this.fno)-Integer.parseInt(fp.getFno());
		}catch(Exception ne){
			System.out.println(ne.getLocalizedMessage());
		}
		return 0;
	}

	@Override
	public String toString() {
		return "ForcastPojo [pART=" + pART + ", fno=" + fno + ", actual=" + actual + ", w0=" + w0 + ", w1=" + w1
				+ ", w2=" + w2 + ", w3=" + w3 + ", w4=" + w4 + ", w5=" + w5 + ", w6=" + w6 + ", w7=" + w7 + "]";
	}

}
