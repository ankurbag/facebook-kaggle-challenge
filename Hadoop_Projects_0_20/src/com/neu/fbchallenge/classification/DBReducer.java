package com.neu.fbchallenge.classification;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

import com.neu.fbchallenge.config.MyDBConstants;

/**
 * Counts all of the hits for an ip. Outputs all ip's
 */
public class DBReducer extends MapReduceBase implements Reducer<Text, Text, Text, Text> {
	Logger log = Logger.getLogger(DBReducer.class.getName());

	public void reduce(Text key_x_y, Iterator<Text> values, OutputCollector<Text, Text> output, Reporter reporter)
			throws IOException {

		String temp="";
		DAO dao = new DAO();
		// loop over the count and tally it up
		log.info("**************************Training Reducer**************************");
		//10_6_4	247_6.8635_4.4686_71_2914591934_10
		if (values.hasNext()) {
			temp = values.next().toString();
			log.info("values.next() " + temp);
			String tokens[] = temp.split(",");
			for(String token : tokens){
				String temptokens[] = token.split("_");
				Map <String,Object>docMap = new HashMap<String,Object>();
				docMap.put(MyDBConstants.FIELD_LOC_KEY, key_x_y.toString());
				docMap.put(MyDBConstants.FIELD_LOC_ID, temptokens[0]);
				docMap.put(MyDBConstants.FIELD_X_COORD, temptokens[1]);
				docMap.put(MyDBConstants.FIELD_Y_COORD,temptokens[2]);
				docMap.put(MyDBConstants.FIELD_ROW_ID, temptokens[3]);
				docMap.put(MyDBConstants.FIELD_TIME_STAMP, temptokens[4]);
				docMap.put(MyDBConstants.FIELD_TIME_SLOT, temptokens[5]);
				String message = dao.insertLocationCoordinates(docMap);
				output.collect(key_x_y, new Text(message));
				
			}

		}
	}

}
