package com.neu.fbchallenge.generator;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class GeneratorMapper extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {

	@Override
	public void map(LongWritable fileOffset, Text lineContents, OutputCollector<Text, Text> output, Reporter reporter) throws IOException {

		String tokens[] = lineContents.toString().split("\\s+");
		/*if(tokens != null && tokens.length > 0){
			String locationId = tokens[0];
			double meanX = tokens.length > 1? Double.parseDouble(tokens[1]):0;
			double meanY = tokens.length > 2? Double.parseDouble(tokens[2]):0;
			int checkInCount =  tokens.length > 3? Integer.parseInt(tokens[3]):0;
			int s1Count =  tokens.length > 4? Integer.parseInt(tokens[4]):0;
			int s2Count =  tokens.length > 5? Integer.parseInt(tokens[5]):0;
			int s3Count =  tokens.length > 6? Integer.parseInt(tokens[6]):0;
			
			int keyX = (int)meanX;
			int keyY = (int)meanY;
			
			output.collect(new Text(keyX+"_"+keyY), new Text(locationId + "_" + meanX + "_"
			+ meanY + "_" +checkInCount+"_"+ s1Count+"_"+s2Count+"_"+s3Count));
			
		}*/
		if(tokens != null && tokens.length > 0){
			String timeSlot = tokens[0];
			String value = tokens[1];
			System.out.println("Generator Mapper timeSlot "+timeSlot);
			System.out.println("Generator Mapper value "+value);
			if(value != null && value.length() > 0){
				String[]values = value.split("\\|");
				if(values != null && values.length > 0){
					for(String entry : values){
						System.out.println("Generator Mapper Entry "+entry);
						String[] entries = entry.split(",");
						double x = entries.length > 0? Double.parseDouble(entries[0]):0;
						double y = entries.length > 1? Double.parseDouble(entries[1]):0;
						String rowId = entries.length > 2? entries[2]:" ";
						long timeStamp = entries.length > 3? Long.parseLong(entries[3]):0;
						long locationId = entries.length > 4? Long.parseLong(entries[4]):0;
						
						int keyX = (int)x;
						int keyY = (int)y;
						
						output.collect(new Text(timeSlot+"_"+keyX+"_"+keyY), new Text(locationId + "_" + x + "_"
								+ y + "_"+rowId+"_"+timeStamp+"_"+timeSlot));

					}
				}
			}
		}
		
		
	}

}
