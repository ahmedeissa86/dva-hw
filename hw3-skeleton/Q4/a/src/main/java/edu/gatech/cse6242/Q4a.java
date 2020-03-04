package edu.gatech.cse6242;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.util.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import java.io.IOException;

public class Q4a {

	public static class firstMapper extends Mapper<Object, Text, Text, Text>{
		private Text out = new Text("1");
		private Text in = new Text("-1");
	    	private Text pickup = new Text();
		private Text dropoff = new Text();

	    	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
		      	String[] parts = value.toString().split("\\t");

			pickup.set(parts[0]);
			dropoff.set(parts[1]);
			context.write(pickup, out);
			context.write(dropoff, in);
		}
	  }

	public static class myReducer extends Reducer<Text,Text,Text, Text> {
	    	private Text result = new Text();

	    	public void reduce(Text key, Iterable<Text> values, Context context ) throws IOException, InterruptedException {
			int sum = 0;
			for (Text val: values){
				sum += Integer.parseInt(val.toString());
			}
			result.set(Integer.toString(sum));
			context.write(key, result);
		}
	}

	public static class secondMapper extends Mapper<Object, Text, Text, Text>{
		private Text count = new Text("1");
		private Text newKey = new Text();

	    	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
			String[] parts = value.toString().split("\\t");
		     	newKey.set(parts[1]);
			context.write(newKey, count);
		}
	  }		

  public static void main(String[] args) throws Exception {

	final String gtid = "aeissa3";

	Configuration conf = new Configuration();

	Job job1 = Job.getInstance(conf, "first");

	job1.setJarByClass(Q4a.class);
	job1.setMapperClass(firstMapper.class);
	job1.setReducerClass(myReducer.class);
	job1.setOutputKeyClass(Text.class);
    	job1.setOutputValueClass(Text.class);

	FileInputFormat.addInputPath(job1, new Path(args[0]));
	FileOutputFormat.setOutputPath(job1, new Path("./data/temp"));
	//System.exit(job1.waitForCompletion(true) ? 0 : 1);

	job1.waitForCompletion(true);

	Job job2 = Job.getInstance(conf, "second");

	job2.setJarByClass(Q4a.class);
	job2.setMapperClass(secondMapper.class);
	job2.setReducerClass(myReducer.class);
	job2.setOutputKeyClass(Text.class);
    	job2.setOutputValueClass(Text.class);

	FileInputFormat.addInputPath(job2, new Path("./data/temp/part-r-00000"));
	FileOutputFormat.setOutputPath(job2, new Path(args[1]));
	System.exit(job2.waitForCompletion(true) ? 0 : 1);
  }
}
