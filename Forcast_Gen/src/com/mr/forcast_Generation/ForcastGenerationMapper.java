package com.mr.forcast_Generation;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class ForcastGenerationMapper  extends Mapper<LongWritable, Text, Text, Text> {

	private String pART;
	private String actual;
	private String fdate;
	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {
		String tempString = ivalue.toString();
		String[] data = tempString.split(",");
		pART=data[1];
		try{
			fdate=convertyymmdd(data[0]);
			actual=data[10];
			context.write(new Text(pART), new Text(fdate+","+actual));
		}catch(ArrayIndexOutOfBoundsException ae){
			System.err.println(ae.getMessage());
		}
	
		
	}

public static String convertyymmdd(String date){
	//SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
			
	//Date fdata = null;
	String dateInString=null;
	String data[] =date.split("/");
	String month=data[0];
	String day=data[1];
	String year=data[2];
	dateInString =year+"/"+month+"/"+day;
		//fdata=formatter.parse(dateInString);
	return dateInString;
	
}
}
