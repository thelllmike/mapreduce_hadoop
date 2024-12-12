import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class PlayerScoringMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] fields = value.toString().split(",");
        if (fields.length > 7 && !fields[7].isEmpty() && !fields[9].isEmpty()) {
            String playerName = fields[7];  // PLAYER1_NAME
            String score = fields[9];       // SCORE
            String[] scores = score.split("-");
            if (scores.length == 2) {
                try {
                    int points = Integer.parseInt(scores[0].trim());
                    context.write(new Text(playerName), new IntWritable(points));
                } catch (NumberFormatException e) {
                    // Handle invalid score format
                }
            }
        }
    }
}
