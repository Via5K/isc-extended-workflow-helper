# BeanShell API Documentation

This document describes the usage of the **BeanShell API** for dynamically executing Java code via HTTP requests and returning structured JSON responses.  
The API supports returning different data formats such as `List` (`[]`), `Map/Object` (`{}`), and `String` (`""`).

---

## Table of Contents
1. [BeanShell API Documentation](#beanshell-api-documentation)
   - [1. API Endpoint](#1-api-endpoint)
   - [2. Request Format](#2-request-format)
   - [3. Response Format](#3-response-format)
   - [4. Return Types](#4-return-types)
   - [5. Error Handling](#5-error-handling)
2. [ESCAPE API Documentation](#escape-api-documentation)
3. [Upload JAR API Documentation](#upload-jar-api-Documentation)
4. [Calling APIs Inside Java Code](#calling-apis-inside-java-code)
5. [Setting Up Locally](#setting-up-locally)

---

## 1. API Endpoint

```http
POST /api/scripts
Content-Type: application/json
```

---

## 2. Request Format

**Example request body:**
```json

  {
    "language": "beanshell",
    "script": "<Java code here>"
  }

```

---

## 3. Response Format

Generic structure of API response:
```json

  {
    "timestamp": "YYYY-MM-DDTHH:MM:SS.sssZ",
    "data": <Returned data>,
    "status": 200,
    "error": null,
    "language": "beanshell"
  }

```

---

## 4. Return Types

### 4.1 Return Type: String (`""`)
```json

  {
    "language": "beanshell",
    "script": "return \"Hello from BeanShell!\";"
  }

```
**Expected Output:**
```json
"data": "Hello from BeanShell!"
```

---

### 4.2 Return Type: List (`[]`)
```json

  {
    "language": "beanshell",
    "script": "import java.util.*; return Arrays.asList(\"apple\", \"banana\", \"cherry\");"
  }

```
**Expected Output:**
```json
"data": ["apple", "banana", "cherry"]
```

---

### 4.3 Return Type: Map (`{}`)
```json

  {
    "language": "beanshell",
    "script": "import java.util.*; Map<String,Object> map=new LinkedHashMap<>(); map.put(\"key\",\"value\"); map.put(\"status\",\"ok\"); return map;"
  }

```
**Expected Output:**
```json
"data": {
  "key": "value",
  "status": "ok"
}
```

---

### 4.4 Printed Output Only
```json

  {
    "language": "beanshell",
    "script": "System.out.println(\"Hello World\"); System.out.println(\"Generated Password: 12345\");"
  }

```
**Expected Output:**
```json
"data": {
  "output": "Hello World\nGenerated Password: 12345"
}
```

---

### 4.5 Mixed Return and Print
```json

  {
    "language": "beanshell",
    "script": "System.out.println(\"This will not be included\"); return \"Final Result\";"
  }

```
**Expected Output:**
```json
"data": "Final Result"
```

---

## 5. Error Handling

If the script contains syntax or runtime errors:
```json

  {
    "language": "beanshell",
    "script": "int x = \"wrong\";"
  }

```
**Expected Output:**
```json
"data": null,
"status": 500,
"error": "Encountered \"=\" at line 1..."
```

---

# ESCAPE API Documentation

This API escapes raw Java code into a format usable in `/api/scripts`.

## 1. API Endpoint
```http
POST /api/escape
Content-Type: text/plain
```

## 2. Request Format
```java
import java.net.*;
import java.io.*;

URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");

BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
String inputLine;
StringBuffer content = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    content.append(inputLine);
}
in.close();
conn.disconnect();
return content.toString();
```

## 3. Response Format
```json

  {
    "escaped": "<escaped Java Code>",
    "length": <int length>
  }

```

## 4. Return Types
Same as BeanShell API.

## 5. Error Handling
No error handling — pure escaping.

---
# Upload JAR API Documentation

## 1. API Endpoint

```http
POST /api/upload-jar
Content-Type: multipart/form-data
```

---

## 2. Request Format

Send a JAR file using `multipart/form-data`.

**Example using Postman:**  
- Set **Method** = `POST`  
- Set **URL** = `http://localhost:8080/api/upload-jar`  
- In the **Body** tab:  
  - Select **form-data**  
  - Add a key `file` (type = *File*)  
  - Choose a `.jar` file from your system and send the request.

**cURL Example:**
```bash
curl -X POST "http://localhost:8080/api/upload-jar"   -H "Content-Type: multipart/form-data"   -F "file=@com.google.gson_2.10.1.jar"
```

---

## 3. Response Format

Generic structure of API response:
```json
{
  "status": "<uploaded|already_exists|error>",
  "path": "Absolute file path where the JAR was stored",
  "timestamp": "YYYY-MM-DDTHH:MM:SS.sssZ",
  "error": null
}
```

Example of API response:
```json
{
  "status": "uploaded",
  "path": "D:\\project\\uploaded-jars\\example.jar",
  "timestamp": "2025-08-18T12:34:56Z"
}
```

---

## 4. Response Scenarios

### 4.1 Successful Upload
```json
{
  "status": "uploaded",
  "path": "D:\\isc-extended-workflow-helper\\uploaded-jars\\com.google.gson_2.10.1.jar",
  "timestamp": "2025-08-18T12:34:56Z"
}
```

### 4.2 JAR Already Exists
```json
{
  "status": "already_exists",
  "path": "D:\\isc-extended-workflow-helper\\uploaded-jars\\com.google.gson_2.10.1.jar",
  "timestamp": "2025-08-17T14:22:31Z"
}
```

### 4.3 Invalid File Type
```json
{
  "error": "Only .jar files are allowed"
}
```

### 4.4 Empty File Upload
```json
{
  "error": "File is empty"
}
```

---

## 5. Error Handling

If the server encounters an unexpected issue (e.g., file write issue, disk permission errors, invalid directory), response format:

```json
{
  "status": 500,
  "error": "Internal server error message"
}
```



## 6. Example Usage in BeanShell Scripts

Once a JAR is uploaded, it can be dynamically loaded in BeanShell scripts using:

```java
addClassPath("D:\\isc-extended-workflow-helper\\beanshell-runner\\uploaded-jars\\com.google.gson_2.10.1.jar"); //add the path that was returned by Upload JAR API
//add another JAR Location
//add another JAR Location

import com.google.gson.Gson; //Normal, library import statements for the jar that was uploaded
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

gson = new Gson();

// Create array [ "foo", "bar" ]
JsonArray arr = new JsonArray();
arr.add(new JsonPrimitive("foo"));
arr.add(new JsonPrimitive("bar"));

// Create object { "foo":true, "bar":true, "foo-bar":false }
JsonObject orgs = new JsonObject();
orgs.add("foo", new JsonPrimitive(true));
orgs.add("bar", new JsonPrimitive(true));
orgs.add("foo-bar", new JsonPrimitive(false));

// Wrap into parent object
JsonObject root = new JsonObject();
root.add("array", arr);
root.add("orgs", orgs);

// Return as Map so Spring serializes it as JSON object
return gson.fromJson(root, java.util.Map.class);
```

**Expected Output:**
```json
{
    "timestamp": "2025-08-18T13:15:29.938366600Z",
    "data": {
        "array": [
            "foo",
            "bar"
        ],
        "orgs": {
            "foo": true,
            "bar": true,
            "foo-bar": false
        }
    },
    "status": 200,
    "error": null,
    "language": "beanshell"
}
```


# Calling APIs Inside Java Code

## 1. API Endpoint
```http
POST /api/scripts
Content-Type: application/json
```

## 2. Request Format
> **Note:** Must be escaped before sending to `/api/scripts`.
```java
import java.net.*;
import java.io.*;

URL url = new URL("https://jsonplaceholder.typicode.com/posts/1");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");

BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
String inputLine;
StringBuffer content = new StringBuffer();
while ((inputLine = in.readLine()) != null) {
    content.append(inputLine);
}
in.close();
conn.disconnect();
return content.toString();
```

## 3. Response Format
```json

  {
    "timestamp": "YYYY-MM-DDTHH:MM:SS.sssZ",
    "data": <Returned data>,
    "status": 200,
    "error": null,
    "language": "beanshell"
  }

```

## 4. Error Handling
```json

  {
    "language": "beanshell",
    "script": "int x = \"wrong\";"
  }

```
**Expected Output:**
```json
"timestamp": "",
"data": null,
"status": 500,
"error": "Encountered \"=\" at line 1..."
```

---

# Setting Up Locally

## 1. Commands
Install dependencies:
```bash
mvn clean install -Dmaven.test.skip=true
```

Run with API calling enabled:
```bash
mvn spring-boot:run -DskipTests -Dspring-boot.run.jvmArguments="--add-opens java.base/sun.net.www.protocol.https=ALL-UNNAMED"
```

Run without API calling:
```bash
mvn exec:java
```

## 2. Postman Testing
Base URL:
```
http://localhost:8080/
```
Endpoints:
- `/api/scripts`
- `/api/escape`
- `/test/beanshell/run` (testing only, will be removed)

## License
This project is licensed under the GNU AGPLv3 License - see the [LICENSE](LICENSE) file for details.

## Credits
Created and maintained by [Neeraj](https://github.com/via5k).

## Support
If you find this project useful, please consider supporting development:
Future developments
- Signature Jar Validation during uploads
- Powershell Integration
- JWT Authentication (proposed but highly unlikely to implement due to hosting in client env.)
