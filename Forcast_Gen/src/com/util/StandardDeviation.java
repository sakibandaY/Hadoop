package com.util;

import java.util.ArrayList;
import java.util.List;

public class StandardDeviation {
	
	    public static double getMean(List<Double> data){
	        double sum = 0.0;
	        for(double d:data)
	        	sum = sum + d;
	       double mean = sum / data.size();
		return mean;
	    }

	    public static double getVariance(List<Double> data)
	    {
			double sum = 0;
			for(double d:data)
				sum = sum + ((d - getMean(data)) * (d - getMean(data)));
			double result = sum / (data.size() - 1);
	    	return result;
	    }
	 
	  public static double getStdDev(List<Double> data)
	    {
	        return Math.sqrt(getVariance(data));
	    }
	    public static void main(String[] args) {
			List<Double> data= new ArrayList<Double>();
			data.add(-0.3778426552);
					data.add(-0.5077405322);
					data.add(-0.3941317145);
					data.add(-0.318687291);
					data.add(-0.2921896914);
					data.add(-0.556348083);

/*					data.add(45.0);
					data.add(24.0);
					data.add(-6.0);
					data.add(13.0);
					data.add(9.0);
					data.add(8.0);
*/
					/*data.add(-38.0);
					data.add(-51.0);
					data.add(-39.0);
					data.add(-32.0);
					data.add(-29.0);
					data.add(-56.0);*/

	    	System.out.println("Mean :: "+getMean(data));
	    	System.out.println("variance :: "+getVariance(data));
	    	System.out.println("StdDev :: "+getStdDev(data));
		}
	
}
