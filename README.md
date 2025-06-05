# 🌦️ Weather MapReduce Project

This project performs large-scale analysis of global weather data to compute:
- **Average Temperature per Country**
- **Average Precipitation per Country**

Using the dataset: [The Weather of 187 Countries in 2020](https://www.kaggle.com/datasets/amirhoseinsedaghati/the-weather-of-187-countries-in-2020)

Built using **Java**, **Hadoop MapReduce**, and executed inside a **Docker-based Hadoop environment**.

---

## 📋 What You'll Do

- Clean and load a real-world weather dataset
- Write Java MapReduce jobs to analyze temperature and precipitation
- Run the jobs using Hadoop in Docker
- Export and format the output into `.csv` files

---

## 📥 Step 1: Clone This Repository

Pull the repository to get all project files including source code, README, sample outputs, and screenshots.

```bash
git clone https://github.com/thananchayan/weather-mapreduce
cd weather-mapreduce
```

---

## 🧱 Step 2: Build the Project Using Maven

Compile the Java MapReduce project using Maven. This generates a `.jar` file which will be executed on Hadoop.

```bash
mvn clean package
```

Check that the JAR is generated at:
```
target/weather-mapreduce-1.0-SNAPSHOT.jar
```

---


---

## 🐳 Hadoop Docker Installation (Optional)

If you do not have Hadoop set up and want to run the project in a self-contained environment, use the following instructions.

### Step 1: Pull Prebuilt Docker Image

Depending on your system:

```bash
# For AMD/Intel-based CPUs
docker pull silicoflare/hadoop:amd

# For ARM-based (Mac M1/M2) CPUs
docker pull silicoflare/hadoop:arm
```

### Step 2: Run a Hadoop Container

Replace `weather-hadoop` with any name you'd like for your container:

```bash
docker run -it -p 9870:9870 -p 8088:8088 -p 9864:9864 --name weather-hadoop silicoflare/hadoop:amd
```

### Step 3: Initialize Hadoop Services

Once inside the container:

```bash
init
jps  # Check if ~7 processes are running (e.g., NameNode, DataNode, ResourceManager, etc.)
```

You can now continue with uploading data and running MapReduce jobs.

### Step 4: Exit or Reopen Container

```bash
exit                            # To exit
docker start -ai weather-hadoop  # To re-enter later
```

---

## 🐳 Step 3: Copy JAR and Dataset to Docker Container

Transfer the compiled JAR and dataset into the running Hadoop Docker container.

```bash
docker cp target/weather-mapreduce-1.0-SNAPSHOT.jar my-hadoop:/root/weather-mapreduce.jar
docker cp weather.csv my-hadoop:/root/weather.csv
```

---

## 📂 Step 4: Upload Dataset to HDFS

Move the dataset into Hadoop Distributed File System (HDFS) so it can be processed by the MapReduce job.

```bash
docker exec -it my-hadoop bash
hdfs dfs -mkdir -p /input
hdfs dfs -put /root/weather.csv /input/
hdfs dfs -ls /input
```

---

## 🚀 Step 5: Run MapReduce Jobs

Use Hadoop to execute the Java JAR file. This will launch the mappers and reducers across the data.

### Average Precipitation:

```bash
hdfs dfs -rm -r /output/avg_precipitation
hadoop jar /root/weather-mapreduce.jar org.thana.Driver /input/weather.csv /output/avg_precipitation precipitation
```

### Average Temperature:

```bash
hdfs dfs -rm -r /output/avg_temperature
hadoop jar /root/weather-mapreduce.jar org.thana.Driver /input/weather.csv /output/avg_temperature temperature
```

---

## 📤 Step 6: Export Output from HDFS

Bring the MapReduce results from HDFS back to the Docker container’s local file system.

```bash
hdfs dfs -get /output/avg_precipitation/part-r-00000 /root/avg_precipitation.csv
hdfs dfs -get /output/avg_temperature/part-r-00000 /root/avg_temperature.csv
```

---

## 🔁 Step 7: Format and Add Headers

Replace tab separators with commas and add CSV column headers for easier reading in Excel.

```bash
sed 's/\t/,/g' /root/avg_precipitation.csv > /root/avg_precipitation_cleaned.csv
sed 's/\t/,/g' /root/avg_temperature.csv > /root/avg_temperature_cleaned.csv

echo "Country,Average Precipitation" | cat - /root/avg_precipitation_cleaned.csv > /root/avg_precipitation_final.csv
echo "Country,Average Temperature" | cat - /root/avg_temperature_cleaned.csv > /root/avg_temperature_final.csv
```

---

## 💻 Step 8: Copy Results Back to Host

Retrieve the final CSV files from the Docker container to your host computer.

```bash
docker cp my-hadoop:/root/avg_precipitation_final.csv ./final_outputs/avg_precipitation.csv
docker cp my-hadoop:/root/avg_temperature_final.csv ./final_outputs/avg_temperature.csv
```

---

## 📸 Screenshots to Include

- `screenshots/hdfs_upload.png`
- `screenshots/job_execution.png`
- `screenshots/output_preview.png`
- `screenshots/tsv_in_excel.png`

---

## 📊 Result Files

You can find final outputs in the `final_outputs/` directory:
- `avg_precipitation.csv`
- `avg_temperature.csv`

---

## ✅ Done!

You now have a complete Hadoop-based analysis of weather data. 🎉