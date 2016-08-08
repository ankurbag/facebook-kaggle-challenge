package com.neu.knn;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class KnnPattern
{
	
	// WritableComparable class for a paired Double and String (distance and model)
	// This is a custom class for MapReduce to pass a double and a String through context
	// as one serializable object.
	// This example only implements the minimum required methods to make this job run. To be
	// deployed robustly is should include ToString(), hashCode(), WritableComparable interface
	// if this object was intended to be used as a key etc.
		public static class DoubleString implements WritableComparable<DoubleString>
		{
			private Double distance = 0.0;
			private String placeId = null;

			public void set(Double lhs, String rhs)
			{
				distance = lhs;
				placeId = rhs;
			}
			
			public Double getDistance()
			{
				return distance;
			}
			
			public String getPlaceId()
			{
				return placeId;
			}
			
			@Override
			public void readFields(DataInput in) throws IOException
			{
				distance = in.readDouble();
				placeId = in.readUTF();
			}
			
			@Override
			public void write(DataOutput out) throws IOException
			{
				out.writeDouble(distance);
				out.writeUTF(placeId);
			}
			
			@Override
			public int compareTo(DoubleString o)
			{
				return (this.placeId).compareTo(o.placeId);
			}
		}
	
	// The mapper class accepts an object and text (row identifier and row contents) and outputs
	// two MapReduce Writable classes, NullWritable and DoubleString (defined earlier)
	public static class KnnMapper extends Mapper<Object, Text, NullWritable, DoubleString>
	{
		DoubleString distanceAndPlaceId = new DoubleString();
		TreeMap<Double, String> KnnMap = new TreeMap<Double, String>();
		
		// Declaring some variables which will be used throughout the mapper
		int K;
	    
		double normalisedSXCoord;
		double normalisedSYCoord;
		String sStatus;
		String sGender;
		double normalisedSChildren;
		
		// The known ranges of the dataset, which can be hardcoded in for the purposes of this example
		double minXCoord = 0;
		double maxXCoord = 10;
		double minYCoord = 0;
		double maxYCoord = 10;
		
		double minChildren = 0;
		double maxChildren = 5;
			
		// Takes a string and two double values. Converts string to a double and normalises it to
		// a value in the range supplied to reurn a double between 0.0 and 1.0 
		private double normalisedDouble(String n1, double minValue, double maxValue)
		{
			return (Double.parseDouble(n1) - minValue) / (maxValue - minValue);
		}
		
		// Takes two strings and simply compares then to return a double of 0.0 (non-identical) or 1.0 (identical).
		// This provides a way of evaluating a numerical distance between two nominal values.
		private double nominalDistance(String t1, String t2)
		{
			if (t1.equals(t2))
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		
		// Takes a double and returns its squared value.
		private double squaredDistance(double n1)
		{
			return Math.pow(n1,2);
		}
		

		// Takes ten pairs of values (three pairs of doubles and two of strings), finds the difference between the members
		// of each pair (using nominalDistance() for strings) and returns the sum of the squared differences as a double.
		private double totalSquaredDistance(double R1, double R2, double S1,double S2)
		{	
			double xCoordDifference = S1 - R1;
			double yCoordDifference = S2 - R2;
			
			
			// The sum of squared distances is used rather than the euclidean distance
			// because taking the square root would not change the order.
			// Status and gender are not squared because they are always 0 or 1.
			return squaredDistance(xCoordDifference) + squaredDistance(yCoordDifference);
		}

		// The @Override annotation causes the compiler to check if a method is actually being overridden
		// (a warning would be produced in case of a typo or incorrectly matched parameters)
		@Override
		// The setup() method is run once at the start of the mapper and is supplied with MapReduce's
		// context object
		protected void setup(Context context) throws IOException, InterruptedException
		{
			if (context != null )
			{
				//&& context.getCacheFiles().length > 0
				// Read parameter file using alias established in main()
				String knnParams = FileUtils.readFileToString(new File("./knnParamFile"));
				StringTokenizer st = new StringTokenizer(knnParams, ",");
		    	
		    	// Using the variables declared earlier, values are assigned to K and to the test dataset, S.
		    	// These values will remain unchanged throughout the mapper
				//10,0.999,1.0591,62,907285
				K = Integer.parseInt(st.nextToken());
				normalisedSXCoord = normalisedDouble(st.nextToken(), minXCoord, maxXCoord);
				normalisedSYCoord = normalisedDouble(st.nextToken(), minYCoord, maxYCoord);
				
				//sStatus = st.nextToken();
				//sGender = st.nextToken();
				//normalisedSChildren = normalisedDouble(st.nextToken(), minChildren, maxChildren);
			}
		}
				
		@Override
		// The map() method is run by MapReduce once for each row supplied as the input data
		public void map(Object key, Text value, Context context) throws IOException, InterruptedException
		{
			// Tokenize the input line (presented as 'value' by MapReduce) from the csv file
			// This is the training dataset, R
			//0	0.7941	9.0809	54	470702	8523065625

			String rLine = value.toString();
			StringTokenizer st = new StringTokenizer(rLine, ",");
			st.nextToken();
			double normalisedRXCoord = normalisedDouble(st.nextToken(), minXCoord, maxXCoord);
			double normalisedRYCoord = normalisedDouble(st.nextToken(), minYCoord, maxYCoord);
			st.nextToken();//time 
			st.nextToken();//accuracy
			/*String rStatus = st.nextToken();
			String rGender = st.nextToken();
			double normalisedRChildren = normalisedDouble(st.nextToken(), minChildren, maxChildren);
			*/
			//st.nextToken();//timestamp
			String rPlaceId = st.nextToken();
			
			// Using these row specific values and the unchanging S dataset values, calculate a total squared
			// distance between each pair of corresponding values.
			double tDist = totalSquaredDistance(normalisedRXCoord, normalisedRYCoord, normalisedSXCoord, normalisedSYCoord);		
			
			// Add the total distance and corresponding car model for this row into the TreeMap with distance
			// as key and model as value.
			KnnMap.put(tDist, rPlaceId);
			// Only K distances are required, so if the TreeMap contains over K entries, remove the last one
			// which will be the highest distance number.
			if (KnnMap.size() > K)
			{
				KnnMap.remove(KnnMap.lastKey());
			}
		}

		@Override
		// The cleanup() method is run once after map() has run for every row
		protected void cleanup(Context context) throws IOException, InterruptedException
		{
			// Loop through the K key:values in the TreeMap
			for(Map.Entry<Double, String> entry : KnnMap.entrySet())
			{
				  Double knnDist = entry.getKey();
				  String knnPlaceId = entry.getValue();
				  // distanceAndModel is the instance of DoubleString declared aerlier
				  distanceAndPlaceId.set(knnDist, knnPlaceId);
				  // Write to context a NullWritable as key and distanceAndModel as value
				  context.write(NullWritable.get(), distanceAndPlaceId);
			}
		}
	}

	// The reducer class accepts the NullWritable and DoubleString objects just supplied to context and
	// outputs a NullWritable and a Text object for the final classification.
	public static class KnnReducer extends Reducer<NullWritable, DoubleString, NullWritable, Text>
	{
		TreeMap<Double, String> KnnMap = new TreeMap<Double, String>();
		int K;
		
		@Override
		// setup() again is run before the main reduce() method
		protected void setup(Context context) throws IOException, InterruptedException
		{
			if (context!= null )
			{//&& context.getCacheFiles().length > 0
				// Read parameter file using alias established in main()
				String knnParams = FileUtils.readFileToString(new File("./knnParamFile"));
				StringTokenizer st = new StringTokenizer(knnParams, ",");
				// Only K is needed from the parameter file by the reducer
				K = Integer.parseInt(st.nextToken());
			}
		}
		
		@Override
		// The reduce() method accepts the objects the mapper wrote to context: a NullWritable and a DoubleString
		public void reduce(NullWritable key, Iterable<DoubleString> values, Context context) throws IOException, InterruptedException
		{
			// values are the K DoubleString objects which the mapper wrote to context
			// Loop through these
			for (DoubleString val : values)
			{
				String rPlaceId = val.getPlaceId();
				double tDist = val.getDistance();
				
				// Populate another TreeMap with the distance and model information extracted from the
				// DoubleString objects and trim it to size K as before.
				KnnMap.put(tDist, rPlaceId);
				if (KnnMap.size() > K)
				{
					KnnMap.remove(KnnMap.lastKey());
				}
			}	

				// This section determines which of the K values (models) in the TreeMap occurs most frequently
				// by means of constructing an intermediate ArrayList and HashMap.

				// A List of all the values in the TreeMap.
				List<String> knnList = new ArrayList<String>(KnnMap.values());

				Map<String, Integer> freqMap = new HashMap<String, Integer>();
			    
			    // Add the members of the list to the HashMap as keys and the number of times each occurs
			    // (frequency) as values
			    for(int i=0; i< knnList.size(); i++)
			    {  
			        Integer frequency = freqMap.get(knnList.get(i));
			        if(frequency == null)
			        {
			            freqMap.put(knnList.get(i), 1);
			        } else
			        {
			            freqMap.put(knnList.get(i), frequency+1);
			        }
			    }
			    
			    // Examine the HashMap to determine which key (model) has the highest value (frequency)
			    String mostNearLocations = null;
			    int maxFrequency = -1;
			    for(Map.Entry<String, Integer> entry: freqMap.entrySet())
			    {
			        if(entry.getValue() > maxFrequency)
			        {
			            mostNearLocations = entry.getKey();
			            maxFrequency = entry.getValue();
			        }
			    }
			    
			// Finally write to context another NullWritable as key and the most common model just counted as value.
			context.write(NullWritable.get(), new Text(mostNearLocations)); // Use this line to produce a single classification
			context.write(NullWritable.get(), new Text(KnnMap.toString()));	// Use this line to see all K nearest neighbours and distances
		}
	}

	// Main program to run: By calling MapReduce's 'job' API it configures and submits the MapReduce job.
	public static void main(String[] args) throws Exception
	{
		// Create configuration
		Configuration conf = new Configuration();
		
		if (args.length != 3)
		{
			System.err.println("Usage: KnnPattern <in> <out> <parameter file>");
			System.exit(2);
		}

		// Create job
		Job job = new Job(conf, "Find K-Nearest Neighbour");
		job.setJarByClass(KnnPattern.class);
		// Set the third parameter when running the job to be the parameter file and give it an alias
		//DistributedCache.addCacheFile(new URI(args[2] + "#knnParamFile"),conf);
		DistributedCache.addCacheFile(new URI(args[2]),conf);
		//job.addCacheFile(new URI(args[2] + "#knnParamFile")); // Parameter file containing test data
		
		// Setup MapReduce job
		job.setMapperClass(KnnMapper.class);
		job.setReducerClass(KnnReducer.class);
		job.setNumReduceTasks(1); // Only one reducer in this design

		// Specify key / value
		job.setMapOutputKeyClass(NullWritable.class);
		job.setMapOutputValueClass(DoubleString.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);
				
		// Input (the data file) and Output (the resulting classification)
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		
		// Execute job and return status
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}
}
