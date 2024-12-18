# Hadoop Docker Setup and Java MapReduce Job

This documentation provides step-by-step instructions for setting up a Hadoop environment using Docker, compiling a Java MapReduce job, and executing it to process basketball data.

---

## Prerequisites

- **Docker** and **Docker Compose** installed on your system.
- A **Hadoop container** configured in your `docker-compose.yml` file.
- Java source files located in the `/app/hadoop` directory.
- Input data file: `basketball_data.csv`.

---

## Steps to Run the Hadoop Job

### 1. Navigate to the Docker Directory

```bash
cd docker
```

### 2. Restart Docker Containers

Bring down the containers and bring them back up in detached mode:

```bash
docker-compose down
docker-compose up -d
```

### 3. Access the Hadoop Container

```bash
docker exec -it hadoop-container bash
```

### 4. Compile the Java Code

Navigate to the Hadoop application directory and compile the Java source files:

```bash
cd /app/hadoop
javac -classpath `hadoop classpath` -d build *.java
```

### 5. Create a JAR File

Create a JAR file from the compiled classes:

```bash
jar -cvf build/ScoringQuarter.jar -C build/ .
```

### 6. Start the HDFS DataNode

```bash
hdfs datanode &
```

### 7. Create Input Directory in HDFS

Create an input directory in HDFS:

```bash
hdfs dfs -mkdir -p /input
```

### 8. Upload Input Data to HDFS

Put the `basketball_data.csv` file into the HDFS input directory:

```bash
hdfs dfs -put /input/basketball_data.csv /input
```

### 9. Verify Input Data in HDFS

List the contents of the input directory:

```bash
hdfs dfs -ls /input
```

### 10. Execute the Hadoop Job

Run the Hadoop job using the JAR file and `ScoringDriver` class:

```bash
hadoop jar build/ScoringQuarter.jar ScoringDriver /input /output_quarter_scores
```

### 11. Verify the Output

List the contents of the output directory:

```bash
hdfs dfs -ls /output_quarter_scores
```

### 12. Display the Output Results

Display the contents of the output file:

```bash
hdfs dfs -cat /output_quarter_scores/part-r-00000
```

### 13. Clean Up Output Directory

Remove the output directory from HDFS to allow re-running the job:

```bash
hdfs dfs -rm -r /output_quarter_scores
```

### 14. Exit the Container

```bash
exit
```

---

## Notes

- Ensure that all Java source files are present in the `/app/hadoop` directory before compilation.
- The `basketball_data.csv` file should contain the data necessary for the MapReduce job.
- If you encounter permission errors, ensure that your Docker container has appropriate permissions to access the Hadoop directories.
