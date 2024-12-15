import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MostScoredPlayerMapper extends Mapper<Object, Text, Text, IntWritable> {

    private Text playerName = new Text();
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
            String homeDescription = fields[3].trim();    // HOMEDESCRIPTION
            String visitorDescription = fields[24].trim(); // VISITORDESCRIPTION
            String player1Name = fields[7].trim();        // PLAYER1_NAME

            // Check if home team scored
            if (!homeDescription.isEmpty() && homeDescription.contains("PTS")) {
                int score = extractPoints(homeDescription);
                playerName.set(player1Name);
                points.set(score);
                context.write(playerName, points);
            }

            // Check if visitor team scored
            else if (!visitorDescription.isEmpty() && visitorDescription.contains("PTS")) {
                int score = extractPoints(visitorDescription);
                playerName.set(player1Name);
                points.set(score);
                context.write(playerName, points);
            }
        }
    }

    // Helper method to extract points from description
    private int extractPoints(String description) {
        try {
            String[] parts = description.split(" ");
            return Integer.parseInt(parts[parts.length - 2]);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
