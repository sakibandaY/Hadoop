package com.driver;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.mr.forcast_Generation.ForcastGenerationMapper;
import com.mr.forcast_Generation.ForcastGenerationReducer;

public class ForcastGenerationDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(com.driver.ForcastGenerationDriver.class);
		// TODO: specify a mapper
		job.setMapperClass(ForcastGenerationMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(ForcastGenerationReducer.class);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		System.out.println("in main");

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job,new Path("hdfs://localhost:9000/Forcast/SampleData2"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/forcastGeneration"));
		if (!job.waitForCompletion(true))
			return;
	}

}
