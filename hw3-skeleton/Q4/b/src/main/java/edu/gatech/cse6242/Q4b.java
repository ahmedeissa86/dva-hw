package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q4b {

  public static class myMapper extends Mapper<Object, Text, Text, Text>{
		
    	private Text k = new Text();
	private Text v = new Text();

    	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
	      	String[] parts = value.toString().split("\\t");
		k.set(parts[2]);
		v.set(parts[3]);
		context.write(k, v);
	}
  }

  public static class myReducer
	extends Reducer<Text,Text,Text, Text> {
	    	private Text result = new Text();

	    	public void reduce(Text key, Iterable<Text> values, Context context ) 
		throws IOException, InterruptedException {
	      	double sum = 0;
		String temp;
		double counter = 0.0;
		for (Text val : values) {
			sum += Double.parseDouble(val.toString());
			counter++;
		}
		double d = Math.round(sum/counter*100.0)/100.0;
		result.set(Double.toString(d));
		context.write(key, result);
	}
  }


  public static void main(String[] args) throws Exception {
   
    final String gtid = "aeissa3";

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Q4b");

	job.setJarByClass(Q4b.class);
	job.setMapperClass(myMapper.class);
	job.setReducerClass(myReducer.class);
	job.setOutputKeyClass(Text.class);
    	job.setOutputValueClass(Text.class);


    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
