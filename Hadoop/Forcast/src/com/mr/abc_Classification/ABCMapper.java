package com.mr.abc_Classification;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class ABCMapper extends Mapper<LongWritable, Text, Text, Text> {
//private AbcPojo objdata=null;
	public void map(LongWritable ikey, Text ivalue, Context context) throws IOException, InterruptedException {

		String tempString = ivalue.toString();
		String[] data = tempString.split(";"); 
		String part=data[0];
		String price=data[1];
		String volume=data[2];
		String spend=data[3];
		String criticality=data[4];
		String valocity=data[5];
		context.write(new Text(part), new Text( price+","+volume+","+spend+","+criticality+","+valocity));
	}

}
