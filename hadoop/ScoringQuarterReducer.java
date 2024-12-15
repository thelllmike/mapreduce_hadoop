import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScoringQuarterReducer extends Reducer<Text, IntWritable, Text, Text> {

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int totalPoints = 0;

        for (IntWritable val : values) {
            totalPoints += val.get();
        }

        context.write(key, new Text("Total Points: " + totalPoints));
    }
}
