package com.mr.abc_Classification;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import com.pojo.AbcPojo;
import com.util.LoadProperty;
import com.util.NormInv;

public class ABCReducer extends Reducer<Text, Text, Text, Text> {
	
	//FileName
		
		private final String F_NAME = "com/common/properties/abc_Classification.properties";
	
		
		private final double price_cal_value=Double.parseDouble(LoadProperty.getValue("price_cal_value", F_NAME));
		private final double volume_cal_value=Double.parseDouble(LoadProperty.getValue("volume_cal_value", F_NAME));
		private final double spend_cal_value=Double.parseDouble(LoadProperty.getValue("spend_cal_value", F_NAME));
		private final double criticality_cal_value=Double.parseDouble(LoadProperty.getValue("criticality_cal_value", F_NAME));
		private final double valocity_cal_value=Double.parseDouble(LoadProperty.getValue("valocity_cal_value", F_NAME));
		private final int index_cal_value=Integer.parseInt(LoadProperty.getValue("index_cal_value", F_NAME));
		private final double ASlo=Double.parseDouble(LoadProperty.getValue("ASlo", F_NAME));
		private final double BSlo=Double.parseDouble(LoadProperty.getValue("BSlo", F_NAME));
		private final double CSlo=Double.parseDouble(LoadProperty.getValue("CSlo", F_NAME));
		private final double percentage_A=Double.parseDouble(LoadProperty.getValue("percentage_A", F_NAME))/100.0;
		private final double percentage_B=percentage_A+(Double.parseDouble(LoadProperty.getValue("percentage_B", F_NAME))/100.0);
		
		
	
	
	
	
		private Map<String, AbcPojo> map=new ConcurrentHashMap<String,AbcPojo>();
		private double totalindex;
		private String price="NewPrice";
		private String volume="NewVolume";
		private String spend="NewSpend";
		private String criticality="NewCriticality";
		private String valocity="NewValocity";
		private String Query=null;
		private List<String> col=null;
		private List<String> val=null;
		private double temp=0.0;
		private int rank=1;
		private int total=1;
	
		public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		String part=_key.toString();
		double newprice=0.0;
		double newvolume=0.0;
		double newspend=0.0;
		double newcriticality=0.0;
		double newvalocity=0.0;
		double index=0.0;
	
		// process values
		for (Text value : values){
			
			String intval=value.toString();
			String [] tokens = intval.split(",");
			price=tokens[0];
			volume=tokens[1];
			spend=tokens[2];
			criticality=tokens[3];
			valocity=tokens[4];

			try{
			  newprice=price_cal_value*(1/Double.parseDouble(price));
			  newvolume=volume_cal_value*(1/Double.parseDouble(volume));
			  newspend=spend_cal_value*(1/Double.parseDouble(spend));
			  newcriticality=criticality_cal_value*(1/Double.parseDouble(criticality));
			  newvalocity=valocity_cal_value*(1/Double.parseDouble(valocity));
			  index=newprice*newvolume*newspend*newcriticality*newvalocity*index_cal_value;
			  map.put(part,new AbcPojo(newprice, newvolume, newspend, newcriticality, newvalocity,index, price, volume,  spend,  criticality, valocity));
			}catch(NumberFormatException ne){
				col=new ArrayList<String>();
				col.add(part);
				col.add(price);
				col.add(volume);
				col.add(spend);
				col.add(criticality);
				col.add(valocity);
				col.add("NewPrice");
				col.add("NewVolume");
				col.add("NewSpend");
				col.add("NewCriticality");
				col.add("NewValocity");
				col.add("Index");
				col.add("HMLABC");
				col.add("Percentile");
				col.add("Rank");
				col.add("ABC");
				col.add("SLO");
				col.add("NORMSINV");
				context.write(new Text(part+","), new Text(price +","+volume+","+spend+","+criticality+","+valocity+","+"NewPrice,NewVolume,NewSpend,NewCriticality,NewValocity,Index,HML/ABC,Percentile,Rank,ABC,SLO,NORMSINV(SLO)"));
				//CreateTable.generateTable(tblName, col);
				//Query=CreateTable.insertStmt(tblName, col);
				System.out.println("IN MR"+Query);
			}
			
			totalindex=totalindex+index;
		}
	}

	
		public void run(Context context) throws IOException, InterruptedException {
	    setup(context);
	    try {
	      while (context.nextKey()) {
	        reduce(context.getCurrentKey(), context.getValues(), context);
	      }
	      double hml=0.0;
	      double per=0.0;
	      double reqp=0.0;
	      double aslo=ASlo;
	      double bslo=BSlo;
	      double cslo=CSlo;
	      
	      int deltaA=0;
	      int deltaB=0;
	      int deltaC=0;
	      Map<String,AbcPojo> sortedmap=sortByComparator(map);
	      int size=sortedmap.size();	

	      for (@SuppressWarnings("rawtypes") Map.Entry entry : sortedmap.entrySet()) {
	    		if(total <=Math.round(size*percentage_A)){
					deltaA++;
				}
				else if(total <=Math.round(size*percentage_B)){
					deltaB++;	
				}
				else{
					deltaC++;	
				}
	    		total++;
	      }
	      total=1;
	      for (@SuppressWarnings("rawtypes") Map.Entry entry : sortedmap.entrySet()) {
				cleanup(context);
				String part1=(String) entry.getKey();
				AbcPojo l=(AbcPojo) entry.getValue(); 
				hml=l.getNewindex()/totalindex;
				per=hml*100;
				reqp=temp+per;
				val=new ArrayList<String>();
				val.add(part1);
				val.add(l.getPrice());
				val.add(l.getVolume());
				val.add(l.getSpend());
				val.add(l.getCriticality());
				val.add(l.getValocity());
				val.add(Double.toString(l.getNewprice()));
				val.add(Double.toString(l.getNewvolume()));
				val.add(Double.toString(l.getNewspend()));
				val.add(Double.toString(l.getNewcriticality()));
				val.add(Double.toString(l.getNewvalocity()));
				val.add(Double.toString(l.getNewindex()));
				val.add(Double.toString(hml));
				val.add(Double.toString(reqp));
				val.add(Double.toString(rank));
				if(total <=Math.round(size*0.2)){
				double temp=aslo;
					val.add("A");
					val.add(Double.toString(aslo));
					val.add(Double.toString(NormInv.compute((temp/100), 100, 10)));
					insertData(context, part1, l, "A", hml, reqp,aslo,NormInv.compute((temp/100), 100, 10));
					val.clear();
					aslo=temp-((double)5/deltaA);
				}
				else if(total <=Math.round(size*0.8)){
					double temp=bslo;
					val.add("B");
					val.add(Double.toString(bslo));
					val.add(Double.toString(NormInv.compute((temp/100), 100, 10)));
					insertData(context, part1, l, "B", hml, reqp,bslo,NormInv.compute((temp/100), 100, 10));
					val.clear();
					bslo=temp-((double)7/deltaB);
				}
				else{
					double temp=cslo;
					val.add("C");
					val.add(Double.toString(cslo));
					val.add(Double.toString(NormInv.compute((temp/100), 100, 10)));
					insertData(context, part1, l, "C", hml, reqp,cslo,NormInv.compute((temp/100), 100, 10));
					val.clear();
					cslo=temp-((double)7/deltaC);
				}
				if(!(reqp==temp)){
					rank++;
					}
				temp=reqp;
				total++;
			}
	    } finally {
	      cleanup(context);
	    }
	  }
	
	
	private void insertData(Context context,String part1,AbcPojo l,String grade,double hml,double reqp,double slo,double nsv) throws IOException, InterruptedException{
		context.write(new Text(part1+","), new Text(l.getPrice() +","+l.getVolume()+","+l.getSpend()+","+l.getCriticality()+","+l.getValocity()+","+ l.getNewprice()+","+l.getNewvolume()+","+l.getNewspend()+","+l.getNewcriticality()+","+l.getNewvalocity()+","+l.getNewindex()+","+hml+","+reqp+"%,"+rank+","+grade+","+slo+","+nsv));
		//InsertData.insertData(Query, val); 
	}
	
	private static Map<String, AbcPojo> sortByComparator(Map<String, AbcPojo> unsortMap) {

		// Convert Map to List
		List<Map.Entry<String, AbcPojo>> list = 
			new LinkedList<Map.Entry<String, AbcPojo>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, AbcPojo>>() {
			public int compare(Map.Entry<String, AbcPojo> o1,
                                           Map.Entry<String, AbcPojo> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// Convert sorted map back to a Map
		Map<String, AbcPojo> sortedMap = new LinkedHashMap<String, AbcPojo>();
		for (Iterator<Map.Entry<String, AbcPojo>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, AbcPojo> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
	
	public static void main(String[] args) {
		ABCReducer r=new ABCReducer();
		r.col=new ArrayList<String>();
		r.col.add("part");
		r.col.add("price");
		r.col.add("volume");
		r.col.add("spend");
		r.col.add("criticality");
		r.col.add("valocity");
		r.col.add("NewPrice");
		r.col.add("NewVolume");
		r.col.add("NewSpend");
		r.col.add("NewCriticality");
		r.col.add("NewValocity");
		r.col.add("Index");
		r.col.add("HMLABC");
		r.col.add("Percentile");
		r.col.add("Rank");
		r.col.add("ABC");
		r.col.add("SLO");
		r.col.add("NORMSINV");
		for(String val:r.col){
			System.out.println(val);
		}
	}
}
