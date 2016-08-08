package com.neu.fbchallenge.generator;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class GeneratorReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text>{

	@Override
	public void reduce(Text key, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {
		
		String finalKey = key.toString();
		StringBuilder finalValues = new StringBuilder();
		while (values.hasNext()) {
			if (finalValues.length() <= 0) {
				finalValues.append(values.next().toString());
			} else {
				finalValues.append(","+values.next().toString());
			}
		}
		
		output.collect(new Text(finalKey), new Text(finalValues.toString()));
	}

}
