package com.mr.forcast_Correction;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ForcastCorrectionMapper extends Mapper<LongWritable, Text, Text, Text> {

	private String pART;
	private String w5;
	private String w1;
	private String w7;
	private String w2;
	private String w6;
	private String w0;
	private String w3;
	private String w4;
	private String actual;
	private String fdate;
	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		String tempString = ivalue.toString();
		String[] data = tempString.split(",");
		pART=data[1];
		try{
			fdate=convertyymmdd(data[0]);
			w0=data[2];
			w1=data[3];
			w2=data[4];
			w3=data[5];
			w4=data[6];
			w5=data[7];
			w6=data[8];
			w7=data[9];
			actual=data[10];
			context.write(new Text(pART), new Text(fdate+","+actual+","+dynamicVariables(data)));
		}catch(ArrayIndexOutOfBoundsException ae){
			System.err.println(ae.getMessage());
		}
	
		
	}

	public void  run(Context context) throws IOException, InterruptedException {
		  setup(context);
		   while (context.nextKeyValue()) {
		   
			   map(context.getCurrentKey(), context.getCurrentValue(), context);
		    }
		   cleanup(context);
	  }

public static String convertyymmdd(String date){
	String fdata = null;
	try{
	String data[] =date.split("/");
	String month=data[0];
	String day=data[1];
	String year=data[2];
	fdata=year+month+day;
	}
	catch(ArrayIndexOutOfBoundsException ae){
		System.out.println("in mapper convert()"+ae.getLocalizedMessage());
	}
	
	return fdata;
	
}

public static String dynamicVariables(String[] data){
	StringBuilder str=new StringBuilder();
	boolean isfirst=true; 
	for(int i=2;i<data.length-1;i++){
		if(isfirst){
			str.append(data[i]);
			isfirst=false;
		}
		else
		str.append(","+data[i]);
	}
	return str.toString();
	}

	
	
}
