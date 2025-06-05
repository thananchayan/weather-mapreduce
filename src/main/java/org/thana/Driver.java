package org.thana;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.thana.WeatherAnalysis.PrecipMapper;
import org.thana.WeatherAnalysis.PrecipReducer;
import org.thana.WeatherAnalysis.TempMapper;
import org.thana.WeatherAnalysis.TempReducer;

public class Driver {
  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: Driver <input path> <output path> <task>");
      System.exit(-1);
    }

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf, "Weather Analysis");
    job.setJarByClass(Driver.class);

    switch (args[2].toLowerCase()) {
      case "temperature":
        job.setMapperClass(TempMapper.class);
        job.setReducerClass(TempReducer.class);
        break;
      case "precipitation":
        job.setMapperClass(PrecipMapper.class);
        job.setReducerClass(PrecipReducer.class);
        break;
      default:
        System.err.println("Invalid task. Use 'temperature' or 'precipitation'");
        System.exit(-1);
    }

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(DoubleWritable.class);

    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    System.exit(job.waitForCompletion(true) ? 0 : 1);
  }
}
