import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class MaxScoringPlayerReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
    private IntWritable maxScore = new IntWritable();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int max = Integer.MIN_VALUE;
        for (IntWritable val : values) {
            max = Math.max(max, val.get());
        }
        maxScore.set(max);
        context.write(key, maxScore);
        System.out.println("Reducing: " + key.toString() + " -> " + max); // Debug statement
    }
}
