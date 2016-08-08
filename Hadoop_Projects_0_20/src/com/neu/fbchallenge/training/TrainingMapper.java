package com.neu.fbchallenge.training;

import java.awt.Polygon;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

/**
 * Mapper that takes a line from an Apache access log and emits the IP with a
 * count of 1. This can be used to count the number of times that a host has hit
 * a website.
 */
public class TrainingMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	Logger log = Logger.getLogger(TrainingMapper.class.getName());

	@Override
	public void map(LongWritable fileOffset, Text lineContents, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		log.info("**************************Training Mapper**************************");
		String tokens[] = lineContents.toString().split(",");
		String location_id = tokens[5];
		String x_coord = tokens[1];
		String y_coord = tokens[2];
		String accuracy = tokens[3];
		String timestamp = tokens[4];
		
		output.collect(new Text(location_id), new Text(x_coord + "_" + y_coord + "_" + accuracy + "_" + timestamp));

	}

}
