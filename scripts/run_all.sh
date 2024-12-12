#!/bin/bash

# Start HDFS
echo "Starting HDFS..."
start-dfs.sh

# Wait for HDFS to start
sleep 5

# Create input directory in HDFS and upload the dataset
hdfs dfs -mkdir -p /input
hdfs dfs -put /input/basketball_data.csv /input

# Run the Scoring by Quarter MapReduce job
echo "Running Scoring by Quarter MapReduce job..."
hadoop jar build/ScoringQuarter.jar ScoringMapper ScoringReducer /input /output_quarter_scores

# Display results
echo "Scoring by Quarter Results:"
hdfs dfs -cat /output_quarter_scores/part-r-00000
