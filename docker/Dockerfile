# Use the BDE2020 Hadoop Docker image
FROM bde2020/hadoop-namenode:2.0.0-hadoop3.2.1-java8

# Set environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64
ENV PATH=$PATH:$JAVA_HOME/bin

# Create directories
RUN mkdir -p /app/hadoop
WORKDIR /app/hadoop

# Copy Java source files and dataset
COPY ../hadoop/*.java .
COPY ../data/basketball_data.csv /input/basketball_data.csv

# Compile the Java files and create the JARs
RUN javac -classpath `hadoop classpath` -d build ScoringMapper.java ScoringReducer.java
RUN jar -cvf build/ScoringQuarter.jar -C build/ .

RUN javac -classpath `hadoop classpath` -d build PlayerScoringMapper.java PlayerScoringReducer.java
RUN jar -cvf build/PlayerScoring.jar -C build/ .

RUN javac -classpath `hadoop classpath` -d build MaxScoringPlayerMapper.java MaxScoringPlayerReducer.java
RUN jar -cvf build/MaxScoringPlayer.jar -C build/ .

# Copy script to run MapReduce jobs
COPY ../scripts/run_all.sh .
RUN chmod +x run_all.sh

# Default command
CMD ["./run_all.sh"]
