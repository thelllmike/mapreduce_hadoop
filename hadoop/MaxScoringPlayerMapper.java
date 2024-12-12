import java.io.IOException;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MaxScoringPlayerMapper extends Mapper<Object, Text, Text, IntWritable> {
    private Text playerName = new Text();
    private IntWritable score = new IntWritable();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        System.out.println("Processing line: " + line); // Debug statement

        String[] fields = line.split(",");
        
        // Check if the line has enough fields and contains a score
        if (fields.length > 24 && !fields[24].isEmpty()) {
            String name = fields[7]; // Assuming PLAYER1_NAME is the 8th column (index 7)
            String scoreStr = fields[24].trim(); // Assuming SCORE is the 25th column (index 24)

            try {
                int playerScore = extractScore(scoreStr);
                playerName.set(name);
                score.set(playerScore);
                context.write(playerName, score);
                System.out.println("Emitted: " + name + " -> " + playerScore); // Debug statement
            } catch (NumberFormatException e) {
                System.err.println("Invalid score format: " + scoreStr); // Error handling
            }
        }
    }

    // Helper method to extract the numeric score from a string like "67 - 65"
    private int extractScore(String scoreStr) throws NumberFormatException {
        String[] scores = scoreStr.split("-");
        return Integer.parseInt(scores[0].trim());
    }
}
