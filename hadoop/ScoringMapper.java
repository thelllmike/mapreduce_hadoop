import java.io.IOException;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;

public class ScoringMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        // Skip the header line
        if (line.contains("EVENTID")) {
            return;
        }

        String[] fields = line.split(",");

        // Check if the line has enough fields
        if (fields.length > 25) {
            try {
                String team = fields[8].trim();        // PLAYER1_TEAM_ABBREVIATION
                String quarter = fields[5].trim();     // PERIOD
                String score = fields[25].trim();      // SCORE (format: X - Y)

                if (!team.isEmpty() && !quarter.isEmpty() && !score.isEmpty() && score.contains("-")) {
                    String[] scores = score.split("-");
                    int points = Integer.parseInt(scores[0].trim());

                    context.write(new Text(team + "_Q" + quarter), new IntWritable(points));
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid score format: " + line);
            } catch (Exception e) {
                System.err.println("Error processing line: " + line);
            }
        } else {
            System.err.println("Invalid line: " + line);
        }
    }
}
