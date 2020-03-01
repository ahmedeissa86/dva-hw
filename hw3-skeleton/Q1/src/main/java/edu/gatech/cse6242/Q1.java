package edu.gatech.cse6242;

import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class Q1 {
/* TODO: Update variable below with your gtid */
  final String gtid = "aeissa3";

  public static class myMapper
       	extends Mapper<Object, Text, Text, Text>{
		private Text fare = new Text();
	    	private Text pickupId = new Text();

	    	public void map(Object key, Text value, Context context
			    ) throws IOException, InterruptedException {
	      	String[] parts = value.toString().split(",");

		float temp = Float.parseFloat(parts[3]);
		if (parts.length == 4 && (Float.parseFloat(parts[2]) !=0)  && (temp > 0)){
			pickupId.set(parts[0]);
			fare.set(Float.toString(temp));
			context.write(pickupId, fare);
		}
	}
  }

  public static class myReducer
	extends Reducer<Text,Text,Text, Text> {
	    	private Text result = new Text();

	    	public void reduce(Text key, Iterable<Text> values, Context context ) 
		throws IOException, InterruptedException {
	      	double sum = 0;
		String temp;
		int counter = 0;
		for (Text val : values) {
			sum += Double.parseDouble(val.toString());
			counter++;
		}
		
		if (sum >= 1000){
			double d = Math.round(sum*100.0)/100.0;
			String str = Double.toString(d);
			int i = str.indexOf('.');
			temp = str.substring(i-3);
			String temp2 = str.substring(0, i-3);
			while (temp2.length() >3){
				temp = temp2.substring(temp2.length()-3) + "," + temp;
				temp2 = temp2.substring(0, temp2.length()-3);
			}

			temp = counter + "," + temp2 + "," + temp;
		} else {
			temp = counter + "," + Double.toString(Math.round(sum*100.0)/100.0);
		}
		result.set(temp);
		context.write(key, result);
	}
  }

  public static void main(String[] args) throws Exception {
    	Configuration conf = new Configuration();
    	Job job = Job.getInstance(conf, "Q1");
    	job.setJarByClass(Q1.class);
	job.setMapperClass(myMapper.class);
	job.setReducerClass(myReducer.class);
	job.setOutputKeyClass(Text.class);
    	job.setOutputValueClass(Text.class);

    /* TODO: Needs to be implemented */

    	FileInputFormat.addInputPath(job, new Path(args[0]));
    	FileOutputFormat.setOutputPath(job, new Path(args[1]));
    	System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
