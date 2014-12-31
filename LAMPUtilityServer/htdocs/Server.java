package org.myorg;
     
 import java.io.IOException;
 import java.util.*;
 import org.apache.hadoop.fs.Path;
 import org.apache.hadoop.conf.*;
 import org.apache.hadoop.io.*;
 import org.apache.hadoop.mapred.*;
 import org.apache.hadoop.util.*;
     
     public class Server {
     
        public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, DoubleWritable> {
         
          public void map(LongWritable key, Text value, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
       
            String line = value.toString();
           	String[] fields = line.split(" ");
            String hour = fields[0];
            String power = fields[1].toString();
           
              output.collect(new Text (hour),new DoubleWritable(Double.parseDouble(power)));
           
          }
        }
     
        public static class Reduce extends MapReduceBase implements Reducer<Text, DoubleWritable, Text, DoubleWritable> {
          public void reduce(Text key, Iterator<DoubleWritable> values, OutputCollector<Text, DoubleWritable> output, Reporter reporter) throws IOException {
            double sum = 0;
            while (values.hasNext()) {
        sum += Double.parseDouble(values.next().toString());
         	
        
            }
       
        output.collect(key, new DoubleWritable(sum));
          }
        }
     
        public static void main(String[] args) throws Exception {
          JobConf conf = new JobConf(Server.class);
          conf.setJobName("AVG");
     
          conf.setOutputKeyClass(Text.class);
          conf.setOutputValueClass(DoubleWritable.class);
     
          conf.setMapperClass(Map.class);
          //conf.setCombinerClass(Reduce.class);
          conf.setReducerClass(Reduce.class);
     
          conf.setInputFormat(TextInputFormat.class);
          conf.setOutputFormat(TextOutputFormat.class);
     
          FileInputFormat.setInputPaths(conf, new Path(args[0]));
          FileOutputFormat.setOutputPath(conf, new Path(args[1]));
     
          JobClient.runJob(conf);
        }
     }
