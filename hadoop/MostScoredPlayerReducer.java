import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MostScoredPlayerReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    private Text mostScoredPlayer = new Text();
    private IntWritable highestScore = new IntWritable(0);

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int totalPoints = 0;

        for (IntWritable val : values) {
            totalPoints += val.get();
        }

        // Keep track of the player with the highest score
        if (totalPoints > highestScore.get()) {
            mostScoredPlayer.set(key);
            highestScore.set(totalPoints);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        context.write(mostScoredPlayer, highestScore);
    }
}
