import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class ScoringQuarterMapper extends Mapper<Object, Text, Text, IntWritable> {

    private Text teamQuarter = new Text();
    private IntWritable points = new IntWritable();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();

        // Skip the header row
        if (line.startsWith("EVENTID")) {
            return;
        }

        String[] fields = line.split(",");

        if (fields.length >= 25) {
            String team = fields[9].trim();               // PLAYER1_TEAM_CITY
            String period = fields[5].trim();             // PERIOD
            String homeDescription = fields[3].trim();    // HOMEDESCRIPTION
            String visitorDescription = fields[24].trim(); // VISITORDESCRIPTION

            // Check if the home team scored
            if (!homeDescription.isEmpty() && homeDescription.contains("PTS")) {
                int score = extractPoints(homeDescription);
                teamQuarter.set(team + "_Q" + period);
                points.set(score);
                context.write(teamQuarter, points);
            }
            // Check if the visiting team scored
            else if (!visitorDescription.isEmpty() && visitorDescription.contains("PTS")) {
                int score = extractPoints(visitorDescription);
                teamQuarter.set(team + "_Q" + period);
                points.set(score);
                context.write(teamQuarter, points);
            }
        }
    }

    private int extractPoints(String description) {
        try {
            String[] parts = description.split(" ");
            return Integer.parseInt(parts[parts.length - 2]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
