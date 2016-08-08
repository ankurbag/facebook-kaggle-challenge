package com.neu.fbchallenge.training;

import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.neu.fbchallenge.config.FbConfig;

import javafx.scene.shape.Polygon;

/**
 * Counts all of the hits for an ip. Outputs all ip's
 */
public class TrainingReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
	Logger log = Logger.getLogger(TrainingReducer.class.getName());

	public void reduce(Text location_id, Iterator<Text> counts, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {

		int checkins = 0;
		int s1Count = 0;
		int s2Count = 0;
		int s3Count = 0;
		
		double totalX = 0;
		double totalY = 0;
		
		while (counts.hasNext()) {
			String value = counts.next().toString();
			StringTokenizer stringTokenizer = new StringTokenizer(value,"_");
			String x = stringTokenizer.nextToken();
			String y = stringTokenizer.nextToken();
			String accuracy = stringTokenizer.nextToken();
			String timeStamp = stringTokenizer.nextToken();
			
			if((accuracy != null?Double.parseDouble(accuracy):0 ) <= FbConfig.ACCURACY_LIMIT ){
				checkins++;
				if (x != null && x.length() > 0) {
					totalX += Double.parseDouble(x);
				}

				if (y != null && y.length() > 0) {
					totalY += Double.parseDouble(y);
				}
				
				String timeKey = FbConfig.getTimeKey(timeStamp);
				if(timeKey != null && timeKey.length() > 0){
					if(timeKey.equals(FbConfig.KEY_SHIFT_1)){
						s1Count++;
					}else if(timeKey.equals(FbConfig.KEY_SHIFT_2)){
						s2Count++;
					}else if(timeKey.equals(FbConfig.KEY_SHIFT_3)){
						s3Count++;
					}
				}
				
			}
		}
		double meanX = totalX/checkins;
		double meanY = totalY/checkins;
		// 101 2.3 3.4 4 2 1 1
		// locationId meanX meanY checkins shift1Count shift2Count shift3Count
		if(meanX >=0 && meanY >=0){
		output.collect(location_id, 
				new Text(meanX+" "+meanY+" "+checkins+" "+s1Count+" "+s2Count+" "+s3Count));
		}
	}

}
