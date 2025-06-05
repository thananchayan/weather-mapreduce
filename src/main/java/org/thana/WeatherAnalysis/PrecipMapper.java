package org.thana.WeatherAnalysis;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PrecipMapper extends Mapper<LongWritable, Text, Text, DoubleWritable> {
  public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] parts = value.toString().split(",");
    if (parts.length > 6 && !parts[2].equals("Country") && !parts[6].isEmpty()) {
      try {
        String country = parts[2].trim();
        double precipitation = Double.parseDouble(parts[6]);
        context.write(new Text(country), new DoubleWritable(precipitation));
      } catch (NumberFormatException ignored) {
      }
    }
  }
}

