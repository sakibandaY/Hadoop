package com.drivers;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.mr.abc_Classification.ABCMapper;
import com.mr.abc_Classification.ABCReducer;

public class ABCDriver {

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "JobName");
		job.setJarByClass(com.drivers.ABCDriver.class);
		// TODO: specify a mapper
		job.setMapperClass(ABCMapper.class);
		// TODO: specify a reducer
		job.setReducerClass(ABCReducer.class);

		// TODO: specify output types
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// TODO: specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(job,new Path("hdfs://localhost:9000/input/sample.csv"));
		FileOutputFormat.setOutputPath(job, new Path("hdfs://localhost:9000/output"));

		if (!job.waitForCompletion(true))
			return;
	}

}
