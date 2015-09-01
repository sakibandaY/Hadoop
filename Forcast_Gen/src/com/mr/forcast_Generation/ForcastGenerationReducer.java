package com.mr.forcast_Generation;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;

import com.pojo.ForcastGenerationPojo;
import com.pojo.ListTransform;


public class ForcastGenerationReducer extends Reducer<Text, Text, Text, Text> {
	
	
	private final double percentage=64;
	private final int sIndex=1;
	private final int eIndex=9;
	
	
	
	private List<ForcastGenerationPojo> list = null;
	private String pART;
	private List<List<String>> movingAvgLists=null;
	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		pART = _key.toString();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/mm/dd");
		// process values
		for (Text val : values) {
			String tempString = val.toString();
			String[] data = tempString.split(",");
			try {
				
				list.add(new ForcastGenerationPojo(pART,data[0], data[1]));
			} catch (Exception ae) {
				System.out.println(ae.getStackTrace() + "****" + ae.getMessage() + "*****" + ae.getLocalizedMessage());
			}
		}
	}
	@Override
	 public void run(Context context) throws IOException, InterruptedException {
		         setup(context);
		         try {
		          while (context.nextKey()) {
		        	  list =new ArrayList<ForcastGenerationPojo>();
		        	  movingAvgLists=new ArrayList<List<String>>();
		        	  List<List<String>> trns=new ArrayList<List<String>>();
		             reduce(context.getCurrentKey(), context.getValues(), context);

		            // Collections.sort(list);
		             
		             Collections.sort(list, new Comparator<ForcastGenerationPojo>() {
		            	   
		            	 @Override
							public int compare(ForcastGenerationPojo o1, ForcastGenerationPojo o2) {
								// TODO Auto-generated method stub
								return o1.getDate().compareTo(o2.getDate());
							}
		            	});
		             
		             System.out.println("************"+pART+"****************");

		             movingAvgLists.add(addDatesFromForcastGen(list));
		             movingAvgLists.add(movingAverage(0, list));
		             for(int i=sIndex;i<=eIndex;i++){
		            	 movingAvgLists.add(movingAverage(i, list));
		             }
		             trns.addAll(ListTransform.transform_MN_To_NM(movingAvgLists));
		             for(List<String> lis:trns){
		            	 System.out.println(lis);
		             }
					}

		             
		             
		          } finally {
		          cleanup(context);
		         }
		    }

	
	
	public List<String> addDatesFromForcastGen(List<ForcastGenerationPojo> list){
		List<String> dates=new ArrayList<String>();
		for(ForcastGenerationPojo fp:list){
			//System.out.println("List::\t"+fp.getPart()+"\t"+fp.getDate()+"\t"+fp.getActual());

			dates.add(fp.getDate());	
		}
	/*System.out.println("***********************************");
	for(int i=0;i<dates.size();i++){
		System.out.println("List::"+i+"\t"+dates.get(i));
	}*/
	return dates;
}
	
	
	public List<String> movingAverage(int month, List<ForcastGenerationPojo> list) {
	List<String> avgdata=new ArrayList<String>();
	int endIndex=(int) Math.round(list.size()*(percentage/100));
	int sI=month;
	
	if(month!=0){
	for(int i=0;i<list.size();i++){
		try{
			//System.out.println(/*list.get(i).getPart()+"\t"+list.get(i).getDate()+"\t"+*/"List::"+i+"\t"+list.get(i).getActual());
			if(i<endIndex){
				if(i<month){
					avgdata.add("null");
				}
				else{
				double temp=0;
				for(int j=1;j<=month;j++){
					temp=temp+Double.parseDouble(list.get(sI-j).getActual());
				}
			
				avgdata.add(String.valueOf(temp/month));
				sI++;
				}
			}
			}
			catch(Exception e){
				System.out.println(e.getStackTrace());
			}
		}
	}
	else{
		for(ForcastGenerationPojo fp:list){
			//System.out.println("List::\t"+fp.getPart()+"\t"+fp.getDate()+"\t"+fp.getActual());

		avgdata.add(fp.getActual());	
		}
	}
	/*System.out.println("***********************************");
	for(int i=0;i<avgdata.size();i++){
		System.out.println("List::"+i+"\t"+avgdata.get(i));
	}*/
	/*
		for(int i=0;i<actual.size();i++){
			if(i<endIndex){
				if(i<k){
					
				}
			}*/
			
			
		
		return avgdata;
		
	}
}