package com.mr.forcast_Correction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import com.pojo.ForcastCorrectionPojo;
import com.util.ListTransform;
import com.util.LoadProperty;

public class ForcastCorrectionReducer extends Reducer<Text, Text, Text, Text> {


	private final String F_NAME = "com/common/properties/forcast_correction.properties";
	
	// Forecast check values
	private final int overforcast_Check_Value = Integer.parseInt(LoadProperty.getValue("overforcast_Check_Value",F_NAME));
	private final int under_forcast_Check_Value = Integer.parseInt(LoadProperty.getValue("under_forcast_Check_Value",F_NAME));

	// Forecast Qualifiers
	private final int overforcast_qualifier = Integer.parseInt(LoadProperty.getValue("overforcast_qualifier",F_NAME));
	private final int random_qualifier = Integer.parseInt(LoadProperty.getValue("random_qualifier",F_NAME));
	private final int underforcast_qualifier = Integer.parseInt(LoadProperty.getValue("underforcast_qualifier",F_NAME));

	// for correction factor first and last percentage values
	private final double first = Double.parseDouble(LoadProperty.getValue("first",F_NAME));
	private final double last = Double.parseDouble(LoadProperty.getValue("last",F_NAME));

	// Lists
	private List<ForcastCorrectionPojo> list = null;

	private List<String> vals = null;
	private List<List<String>> listOfList = null;

	private List<List<String>> tempListOfList = new ArrayList<List<String>>();

	private List<String> errorList = null;
	private List<List<String>> errorLists = null;

	private List<String> dignosis;
	private List<List<String>> dignosisList = new ArrayList<List<String>>();
	private List<List<String>> transformedDignosisList = new ArrayList<List<String>>();

	private List<Double> reduc_Inf_Factor = null;

	boolean isfirst = true;
	private String pART;

	public void reduce(Text _key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		pART = _key.toString();
		// process values
		for (Text val : values) {
			String tempString = val.toString();
			String[] data = tempString.split(",");
			try {
				list.add(new ForcastCorrectionPojo(pART, data[0], data[1], data[2], data[3], data[4], data[5], data[6], data[7],
						data[8], data[9]));
			} catch (Exception ae) {
				System.out.println(ae.getStackTrace() + "****" + ae.getMessage() + "*****" + ae.getLocalizedMessage());
			}
		}
	}

	@Override
	public void run(Context context) throws IOException, InterruptedException {
		setup(context);
		try {
			System.out.println("overforcast_Check_Value :: " + overforcast_Check_Value);
			System.out.println("under_forcast_Check_Value :: " + under_forcast_Check_Value);
			System.out.println("overforcast_qualifier :: " + overforcast_qualifier);
			System.out.println("random_qualifier :: " + random_qualifier);
			System.out.println("underforcast_qualifier :: " + underforcast_qualifier);
			System.out.println("first :: " + first);
			System.out.println("last :: " + last);

			while (context.nextKey()) {

				listOfList = new ArrayList<List<String>>();
				list = new ArrayList<ForcastCorrectionPojo>();
				reduce(context.getCurrentKey(), context.getValues(), context);

				if (!pART.equalsIgnoreCase("part")) {
					if (!isfirst) {
						context.write(new Text(), new Text());
					}
					isfirst = false;
					files_WE(listOfList, list, "hello", context);
					context.write(new Text("WaterFall"), new Text());
					context.write(new Text("Part,"), new Text("FNO,Actual,w0,w1,w2,w3,w4,w5,w6,w7"));
					files_WE(listOfList, list, "waterfall", context);
					context.write(new Text(), new Text());
					context.write(new Text("Error"), new Text());
					context.write(new Text("Part,"), new Text("FNO,Actual,w0,w1,w2,w3,w4,w5,w6,w7"));
					files_WE(listOfList, list, "error", context);
					context.write(new Text(), new Text());
					context.write(new Text("Dignosis of " + pART), new Text());
					writeDignosisData(context);
					setData(tempListOfList, list, "part");
					correctedData();
					tempListOfList = new ArrayList<List<String>>();
				}

			}

		} finally {
			cleanup(context);
		}
	}

	// Transform List in Diagonal format and write the data to Files
	public void files_WE(List<List<String>> listOfList, List<ForcastCorrectionPojo> list, String file, Context context) {

		Collections.sort(list);
		if (file.equalsIgnoreCase("waterfall")) {
			try {
				setData(listOfList, list, "dontgetpart");

				Collections.sort(listOfList, new Comparator<List<String>>() {
					@Override
					public int compare(List<String> o1, List<String> o2) {
						return o1.get(0).compareTo(o2.get(0));
					}
				});

				for (int i = listOfList.size() - 1; i > -1; i--) {
					List<String> list1 = listOfList.get(i);
					int k = 1;
					for (int j = 3; j < list1.size(); j++) {
						try {
							list1.set(j, listOfList.get(i - k).get(j));
						} catch (Exception ex) {
							list1.set(j, null);
						}
						k++;
					}

				}
			} catch (Exception e) {
				e.getLocalizedMessage();
			}
		}
		try {

			errorLists = new ArrayList<List<String>>();

			for (int i = 0; i < listOfList.size(); i++) {
				errorList = new ArrayList<String>();
				context.write(new Text(pART + ","), new Text(convertLTS(listOfList.get(i), file)));
				if (!errorList.isEmpty())
					errorLists.add(errorList);
			}
			dignosis(errorLists);
			correctionFactor(errorLists);

		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}

	}

	// For converting list of values in comma seperated values in string
	public String convertLTS(List<String> lis, String file) {
		StringBuilder str = new StringBuilder();
		boolean isfirst = true;
		boolean issecond = false;
		int actualConsumption = 0;
		for (String val : lis) {
			if (issecond) {
				try {
					actualConsumption = Integer.parseInt(val);
				} catch (NumberFormatException ne) {
					System.out.println("IN CLTS" + ne.getLocalizedMessage());
				}
				str.append("," + val);
				issecond = false;
			} else if (isfirst) {
				str.append(val);
				isfirst = false;
				issecond = true;
			} else {

				if (file.equalsIgnoreCase("waterfall")) {
					if (val != null) {
						str.append("," + val);
					}
				}
				if (file.equalsIgnoreCase("error")) {
					String error = aC(val, actualConsumption);
					if (error != null) {
						errorList.add(error);
						str.append("," + error + "%");
					}
				}
			}
		}
		return str.toString();
	}

	// For Error Calculation
	public String aC(String wval, int actual) {
		try {
			double val = Double.parseDouble(wval);
			if (actual != 0) {
				int temp = (int) Math.round(((val - actual) / actual) * 100);
				return String.valueOf(temp);
			} else
				return "0";
		} catch (Exception e) {
			return null;
		}
	}

	// Error DIGNOSIS values and to find correction factor
	public void dignosis(List<List<String>> listOfList) {
		System.out.println("ERROR");
		int totalSize = listOfList.size();

		for (int i = 0; i < listOfList.size(); i++) {
			dignosis = new ArrayList<String>();
			int overForcast = 0;
			int underForcast = 0;
			int random = 0;
			for (int j = 0; j < listOfList.size(); j++) {
				List<String> list = listOfList.get(j);
				try {
					int val = Integer.parseInt(list.get(i));
					// System.out.println(val);
					if (val > overforcast_Check_Value) {
						overForcast++;
					} else if (val < under_forcast_Check_Value) {
						underForcast++;
					} else {
						random++;
					}
				} catch (Exception e) {
					System.out.println(e.getStackTrace());
				}
			}
			double oForcast = ((double) overForcast / totalSize) * 100;
			double uForcast = ((double) underForcast / totalSize) * 100;
			double rdm = ((double) random / totalSize) * 100;
			dignosis.add(String.valueOf(oForcast));
			dignosis.add(String.valueOf(rdm));
			dignosis.add(String.valueOf(uForcast));
			dignosisList.add(dignosis);
			totalSize--;
		}
	}

	// For writing dignosis Data and correction factor data
	public void writeDignosisData(Context context) {

		transformedDignosisList.addAll(ListTransform.transform_MN_To_NM(dignosisList));
		boolean isfirst = true;
		boolean issecond = false;
		boolean isthird = false;

		for (List<String> ls : transformedDignosisList) {
			try {
				if (isfirst) {
					context.write(new Text("Over Forcast,"), new Text("," + convertDiag(ls, "overforcast")));
					isfirst = false;
					issecond = true;
				} else if (issecond) {
					context.write(new Text("Random,"), new Text("," + convertDiag(ls, "random")));
					issecond = false;
					isthird = true;
				} else if (isthird) {
					context.write(new Text("Under Forcast,"), new Text("," + convertDiag(ls, "underforcast")));
					isthird = false;
				}

			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				System.out.println("Error In::\t" + e.getStackTrace());
			}
		}

		StringBuilder str = new StringBuilder();
		for (double values : reduc_Inf_Factor) {
			str.append("," + values);
		}
		try {
			context.write(new Text(), new Text());
			context.write(new Text("REDUCINF FACTOR of " + pART), new Text());
			context.write(new Text(), new Text("," + str));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dignosisList = new ArrayList<List<String>>();
		transformedDignosisList.clear();
	}

	// method for calculating correction factor
	public void correctionFactor(List<List<String>> listOfList) {
		int totalSize = listOfList.size();
		reduc_Inf_Factor = new ArrayList<Double>();
		for (int i = 0; i < listOfList.size(); i++) {

			double avg = 0.0;
			int startindex = (int) Math.round((totalSize)*(first/100));
			int endindex = (int) Math.round((totalSize)*(last/100));
			int no_of_values_in_btw_SI_EI = (totalSize - endindex) - startindex;

			for (int j = startindex + i; j < listOfList.size() - endindex; j++) {
				try {
					List<String> list = listOfList.get(j);
					double temp = Double.parseDouble(list.get(i));
					avg = avg + temp;
				} catch (Exception e) {
					System.out.println("in correction factor::" + avg);
				}
			}
			double mean = avg / no_of_values_in_btw_SI_EI;
			reduc_Inf_Factor.add(mean);
			totalSize--;
		}
	}

	// to check Dignosis with its qualifier values
	public String convertDiag(List<String> lis, String file) {

		StringBuilder str = new StringBuilder();

		for (String val : lis) {

			if (file.equalsIgnoreCase("overforcast")) {
				if (Double.parseDouble(val) < overforcast_qualifier) {
					str.append("," + val + "%   FAIL");
				}
				if (Double.parseDouble(val) > overforcast_qualifier) {
					str.append("," + val + "%   PASS");
				}
			}
			if (file.equalsIgnoreCase("random")) {
				if (Double.parseDouble(val) < random_qualifier) {
					str.append("," + val + "%");
				}
				if (Double.parseDouble(val) > random_qualifier) {
					str.append("," + val + "%");
				}
			}
			if (file.equalsIgnoreCase("underforcast")) {
				if (Double.parseDouble(val) < underforcast_qualifier) {
					str.append("," + val + "%   FAIL");
				}
				if (Double.parseDouble(val) > underforcast_qualifier) {
					str.append("," + val + "%   PASS");
				}
			}

		}
		System.out.println(str);
		return str.toString();
	}

	

	// to set the data to list of list
	public void setData(List<List<String>> listOfList, List<ForcastCorrectionPojo> list, String dlist) {
		for (ForcastCorrectionPojo str : list) {
			vals = new ArrayList<String>();
			if (dlist.equalsIgnoreCase("part")) {
				vals.add(str.getpART());
			}
			vals.add(str.getFno());
			vals.add(str.getActual());
			vals.add(str.getW0());
			vals.add(str.getW1());
			vals.add(str.getW2());
			vals.add(str.getW3());
			vals.add(str.getW4());
			vals.add(str.getW5());
			vals.add(str.getW6());
			vals.add(str.getW7());
			listOfList.add(vals);

		}
	}

	public void correctedData() {
		List<String> newError = null;
		List<String> orginalError = null;
		List<String> absolute_New_Error = null;
		List<String> absolute_Original_Error = null;
		List<List<String>> absolute_New_Errors = new ArrayList<List<String>>();
		List<List<String>> absolute_Original_Errors = new ArrayList<List<String>>();
		List<String> avg_of_absolute_original_error = new ArrayList<String>();
		List<String> avg_of_absolute_new_error = new ArrayList<String>();
		List<String> Acceptace_Factor = null;

		int actual = 0;

		System.out.println("***********ORGINAL ERROR VALUES***********");
		// Original Values in List
		for (int i = 0; i < tempListOfList.size(); i++) {
			List<String> ls = tempListOfList.get(i);
			orginalError = new ArrayList<String>();
			for (int j = 0; j < ls.size(); j++) {
				orginalError.add(ls.get(j));
			}
			System.out.println(orginalError);
		}
		System.out.println("***********ABSOLUTE ORGINAL ERROR VALUES**************");

		// Absolute Original values in List
		for (int i = 0; i < tempListOfList.size(); i++) {
			List<String> ls = tempListOfList.get(i);
			absolute_Original_Error = new ArrayList<String>();
			// double avg = 0.0;

			for (int j = 0; j < ls.size(); j++) {
				if (j < 3) {

					actual = Integer.parseInt(ls.get(2));
					absolute_Original_Error.add(ls.get(j));

				} else {
					double d = Double.parseDouble(ls.get(j));
					absolute_Original_Error.add(aConsumption(d, actual));
					// double temp = Double.parseDouble(aConsumption(d,actual));
					// avg=avg+temp;
				}
			}
			// System.out.println(ls);
			// double absoluteNewError=avg/tempListOfList.size();
			System.out.println(absolute_Original_Error);
			absolute_Original_Errors.add(absolute_Original_Error);
		}
		System.out.println("***************NEW ERROR VALUES******************");

		// New Error Values in List
		for (int i = 0; i < tempListOfList.size(); i++) {
			List<String> ls = tempListOfList.get(i);
			newError = new ArrayList<String>();
			int k = 3;
			for (int j = 0; j < ls.size(); j++) {
				if (j < 3) {
					newError.add(ls.get(j));
				} else {
					double d = Double.parseDouble(ls.get(j));
					double f = reduc_Inf_Factor.get(j - k);
					double res = d * f;
					newError.add(String.valueOf(res));
				}
			}
			System.out.println(newError);

		}
		System.out.println("************ABSOLUTE NEW ERROR VALUES**************");

		// Absolute New Error values in List
		for (int i = 0; i < tempListOfList.size(); i++) {
			List<String> ls = tempListOfList.get(i);
			absolute_New_Error = new ArrayList<String>();
			int k = 3;

			for (int j = 0; j < ls.size(); j++) {
				if (j < 3) {

					actual = Integer.parseInt(ls.get(2));
					absolute_New_Error.add(ls.get(j));
				} else {
					double d = Double.parseDouble(ls.get(j));
					double f = reduc_Inf_Factor.get(j - k);
					double res = d * f;
					absolute_New_Error.add(aConsumption(res, actual));

				}
			}
			System.out.println(absolute_New_Error);
			absolute_New_Errors.add(absolute_New_Error);
		}
		System.out.println("************Average Of Absolute Original Values*************");

		// Average Of Absolute Original Values in List
		int size = 0;
		for (List ls : absolute_Original_Errors) {
			size = ls.size();
		}
		System.out.println(size);
		for (int i = 3; i < size; i++) {

			double avg = 0.0;

			for (int j = 0; j < absolute_Original_Errors.size(); j++) {
				try {
					List<String> list = absolute_Original_Errors.get(j);
					double temp = Double.parseDouble(list.get(i));
					avg = avg + temp;
				} catch (Exception e) {
					System.out.println("in correction factor::" + avg);
				}
			}
			double mean = avg / absolute_Original_Errors.size();
			if (mean < 0) {
				mean = mean * -1;
			}
			avg_of_absolute_original_error.add(String.valueOf(Math.round(mean * 100)));
		}
		System.out.println(avg_of_absolute_original_error);
		System.out.println("************Average Of Absolute New Values*************");

		// Average Of Absolute New Values in List
		for (List ls : absolute_New_Errors) {
			size = ls.size();
		}
		System.out.println(size);
		for (int i = 3; i < size; i++) {

			double avg = 0.0;

			for (int j = 0; j < absolute_New_Errors.size(); j++) {
				try {
					List<String> list = absolute_New_Errors.get(j);
					double temp = Double.parseDouble(list.get(i));
					avg = avg + temp;
				} catch (Exception e) {
					System.out.println("in correction factor::" + avg);
				}
			}
			double mean = avg / absolute_Original_Errors.size();
			if (mean < 0) {
				mean = mean * -1;
			}
			avg_of_absolute_new_error.add(String.valueOf(Math.round(mean * 100)));
		}
		System.out.println(avg_of_absolute_new_error);

		System.out.println("***************Acceptace_Factor******************");

		if (avg_of_absolute_new_error.size() == avg_of_absolute_original_error.size()) {
			size = avg_of_absolute_new_error.size();
			Acceptace_Factor = new ArrayList<String>();
			for (int i = 0; i < size; i++) {
				int aane = Integer.parseInt(avg_of_absolute_new_error.get(i));
				int aaoe = Integer.parseInt(avg_of_absolute_original_error.get(i));
				Acceptace_Factor.add(String.valueOf(aane - aaoe));
			}

		}

		System.out.println(Acceptace_Factor);

		System.out.println("***************END******************");

		/*
		 * System.out.println("**********************");
		 * 
		 * for(int i=0;i<tempListOfList.size();i++){ List<String> ls=tempListOfList.get(i);
		 * samplelist=new ArrayList<String>(); int k=3;
		 * 
		 * for(int j=0;j<ls.size();j++){ if(j<3){ samplelist.add(ls.get(j)); }
		 * else{ double d=Double.parseDouble(ls.get(j)); double
		 * f=reduc_Inf_Factor.get(j-k); double res=d*f;
		 * samplelist.add(String.valueOf(res)); } }
		 * System.out.println(samplelist); }
		 */

	}

	// For Error Calculation
	public String aConsumption(double wval, int actual) {
		try {
			if (actual != 0) {
				double temp = (wval - actual) / actual;
				return String.valueOf(temp);
			} else
				return "0";
		} catch (Exception e) {
			return null;
		}
	}
}
