import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class PlayerScoringReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int totalScore = 0;
        for (IntWritable val : values) {
            totalScore += val.get();
        }
        context.write(key, new IntWritable(totalScore));
    }
}
