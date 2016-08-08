package com.neu.fbchallenge.training;

import java.nio.ByteBuffer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparator;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;

public class TraningRunner {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		JobConf conf = new JobConf(TraningRunner.class);
		conf.setJobName("sum-job");

		conf.setMapperClass(TrainingMapper.class);

		conf.setMapOutputKeyClass(Text.class);
		conf.setMapOutputValueClass(Text.class);

		conf.setReducerClass(TrainingReducer.class);	
		Path outputPath = new Path(args[1]);

		// take the input and output from the command line
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, outputPath);

		try {
			FileSystem dfs = FileSystem.get(outputPath.toUri(), conf);
			if (dfs.exists(outputPath)) {
				dfs.delete(outputPath, true);
			}
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
