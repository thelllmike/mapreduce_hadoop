import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ScoringQuarterReducer extends Reducer<Text, IntWritable, Text, Text> {

    private Map<String, Integer> teamMaxPoints = new HashMap<>();
    private Map<String, String> teamBestQuarter = new HashMap<>();

    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int totalPoints = 0;

        // Sum up the points for the given team-quarter
        for (IntWritable val : values) {
            totalPoints += val.get();
        }

        // Key format: Team_Q<Quarter>
        String[] parts = key.toString().split("_");
        String team = parts[0];
        String quarter = parts[1];

        // Check if this is the highest score for the team
        if (!teamMaxPoints.containsKey(team) || totalPoints > teamMaxPoints.get(team)) {
            teamMaxPoints.put(team, totalPoints);
            teamBestQuarter.put(team, quarter);
        }
    }

    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        for (Map.Entry<String, Integer> entry : teamMaxPoints.entrySet()) {
            String team = entry.getKey();
            String quarter = teamBestQuarter.get(team);
            int points = entry.getValue();
            context.write(new Text(team), new Text("scored most of the points in " + quarter + " with " + points + " points"));
        }
    }
}
