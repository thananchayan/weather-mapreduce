package org.thana.WeatherAnalysis;

import java.io.IOException;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class TempReducer extends Reducer<Text, DoubleWritable, Text, Text> {

  @Override
  protected void setup(Context context) throws IOException, InterruptedException {
    context.write(new Text(String.format("%-30s %s", "Country", "Average Temperature")), null);
    context.write(new Text("----------------------------------------------------------"), null);
  }

  @Override
  public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
      throws IOException, InterruptedException {

    double sum = 0;
    int count = 0;

    for (DoubleWritable val : values) {
      sum += val.get();
      count++;
    }

    if (count > 0) {
      double average = sum / count;
      String formattedLine = String.format("%-30s %8.2f", key.toString(), average);
      context.write(new Text(formattedLine), null);
    }
  }
}