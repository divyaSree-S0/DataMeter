# Data Meter - Mobile Data Usage Aggregator

Java 21 application that processes large mobile data usage files and generates categorized, costed reports per mobile number.

---

##  How to Run the Project

###  Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/DataMeter.git
cd DataMeter
```

> ⚠️ Make sure you have:
>
> * Java 21 installed
> * Maven installed (for building the jar)

---

### Alternative: Run without cloning(JAR only)
> If you don't want to clone or build the project, you can simply download the pre-built JAR file and run it directly.

#### Steps
1. Download the JAR file:
```
DataMeter-1.0-SNAPSHOT.jar
```
2. Place it in a folder that also contains:
```
config.properties
input_files/
    └── your_input_file.txt
```
3. Run the application using Java 21:
```
java -jar DataMeter-1.0-SNAPSHOT.jar input_files
```
4. The results and logs will be created inside:
```
output/
    ├── data_usage_report_yyyyMMdd_HH-mm-ss.txt
    └── malformed_lines_yyyyMMdd_HH-mm-ss.log
```
> ⚠️ Make sure you have:
>  * You have Java 21 installed
>  * The ```input_files/``` folder contains valid .txt input files
>  * The ```config.properties``` file is in the same directory as the JAR

---


###  Step 2: Ensure Directory Structure

Before running, make sure your root directory contains:

```
DataMeter/
├── input_files/               #  Create this folder (if it doesn't exist)
│   └── sample_input.txt       #  Sample test file included
├── config.properties          #  Required config file (sample included, can be changed as requird)
├── tools/
│   └── GenerateTestData.java  #  (Optional) test data generator
├── src/                       #  Java source code
```

---

###  Step 3: Run with Sample Input

Sample file:

```
input_files/sample_input.txt
```

Run the program using Maven:

```bash
mvn clean package
java -jar target/DataMeter-1.0-SNAPSHOT.jar input_files
```

> Output will be saved in the `output/` folder, with timestamped `.txt` and `.log` files.

---

## Optional: Generate More Test Data

If you want to test the system under larger loads (e.g.,1000+ lines,5000+ lines, 100,000+ lines):

1. Run the test data generator:

   ```bash
   javac tools/GenerateTestData.java
   java tools.GenerateTestData
   ```

2. This will create files like:

   ```
   input_files/generated_input_1.txt
   input_files/generated_input_2.txt
   input_files/generated_input_3.txt
   ```

3. Then run the program again on the `input_files/` directory.

---

##  Input Format

Each input file has pipe-separated lines:

```
MobileNumber|Tower|4G|5G|Roaming
```

Example:

```
9000600601|TowerA|12345|45678|Yes
```

---

##  Output

After processing, the program generates:

*  `data_usage_report_yyyyMMdd_HH-mm-ss.txt`
*  `malformed_lines_yyyyMMdd_HH-mm-ss.log`

in the `output/` folder.

### Example Output Format:

```
Mobile Number|4G|5G|4G Roaming|5G Roaming|Cost
9000600601|0|5000|1345|0|103
```

---

##  Validation Rules

*  Mobile number: Exactly 10 digits (only numeric)
*  Tower: Cannot be empty
*  4G/5G usage: Must be integers ≥ 0
*  Roaming: Must be `Yes`, `No`, `True`, or `False` (case-insensitive)
*  Anything else is logged as malformed with a reason to log file.

---

##  Cost Calculation

Rates are read from `config.properties`(sample):

```properties
rate.4g=0.01
rate.5g=0.02
roaming.multiplier.4g=1.1
roaming.multiplier.5g=1.15
usage.threshold=100000
threshold.multiplier=1.05
```

Billing Rules:

* Home 4G/5G is charged at base rate
* Roaming data is charged at a higher multiplier
* If total usage exceeds threshold (e.g., 100MB), apply a 5% surcharge

---

## Run Tests (Optional)

To run unit tests for cost calculation and input validation:

```bash
mvn test
```
The JUnit tests are available in
```
test/java/com.datameter/
```


