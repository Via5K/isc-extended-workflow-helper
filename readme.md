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
3. [Calling APIs Inside Java Code](#calling-apis-inside-java-code)
4. [Setting Up Locally](#setting-up-locally)

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
[
  {
    "language": "beanshell",
    "script": "<Java code here>"
  }
]
```

---

## 3. Response Format

Generic structure of API response:
```json
[
  {
    "timestamp": "YYYY-MM-DDTHH:MM:SS.sssZ",
    "data": <Returned data>,
    "status": 200,
    "error": null,
    "language": "beanshell"
  }
]
```

---

## 4. Return Types

### 4.1 Return Type: String (`""`)
```json
[
  {
    "language": "beanshell",
    "script": "return \"Hello from BeanShell!\";"
  }
]
```
**Expected Output:**
```json
"data": "Hello from BeanShell!"
```

---

### 4.2 Return Type: List (`[]`)
```json
[
  {
    "language": "beanshell",
    "script": "import java.util.*; return Arrays.asList(\"apple\", \"banana\", \"cherry\");"
  }
]
```
**Expected Output:**
```json
"data": ["apple", "banana", "cherry"]
```

---

### 4.3 Return Type: Map (`{}`)
```json
[
  {
    "language": "beanshell",
    "script": "import java.util.*; Map<String,Object> map=new LinkedHashMap<>(); map.put(\"key\",\"value\"); map.put(\"status\",\"ok\"); return map;"
  }
]
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
[
  {
    "language": "beanshell",
    "script": "System.out.println(\"Hello World\"); System.out.println(\"Generated Password: 12345\");"
  }
]
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
[
  {
    "language": "beanshell",
    "script": "System.out.println(\"This will not be included\"); return \"Final Result\";"
  }
]
```
**Expected Output:**
```json
"data": "Final Result"
```

---

## 5. Error Handling

If the script contains syntax or runtime errors:
```json
[
  {
    "language": "beanshell",
    "script": "int x = \"wrong\";"
  }
]
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
[
  {
    "escaped": "<escaped Java Code>",
    "length": <int length>
  }
]
```

## 4. Return Types
Same as BeanShell API.

## 5. Error Handling
No error handling — pure escaping.

---

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
[
  {
    "timestamp": "YYYY-MM-DDTHH:MM:SS.sssZ",
    "data": <Returned data>,
    "status": 200,
    "error": null,
    "language": "beanshell"
  }
]
```

## 4. Error Handling
```json
[
  {
    "language": "beanshell",
    "script": "int x = \"wrong\";"
  }
]
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
