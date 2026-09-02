# Advanced Java Lab Projects

Java lab projects: Swing apps, Sockets, RMI, JDBC, and a webapp. Requires **JDK** (tested on JDK 26); MySQL needed only for the JDBC project. Tomcat is required for Experiment 6.

## Projects

### 1. Calculator (Swing MVC)
A simple calculator (add, subtract, multiply, divide, percentage) built with Java Swing using MVC.

```bash
cd Calculator
javac *.java
java Calculator.Main
```

### 2. Registration Form (Swing)
Student registration form with Name/Roll/Branch fields, gender radio buttons, terms checkbox, validation, submit & reset.

```bash
cd "Registration Form"
javac aniket.java
java aniket
```

### 3. RMI
Client calls remote `Calculator` methods (add, subtract) on a server via Java RMI. Use three terminals in `RMI`:

```bash
javac *.java
start rmiregistry          # Terminal 1
java Server                # Terminal 2
java Client                # Terminal 3  -> Addition: 30 Subtraction: 10
```

### 4. Server Client Basics (Sockets)
TCP client-server on port 5000. Two terminals in `Server Client Basics`:

```bash
javac server.java; java server   # Terminal 1
javac client.java; java client   # Terminal 2  -> Server: Hello Client
```

### 5. MySQL Connect (JDBC)
Connects to MySQL (`college` DB), inserts a row into `student`. Needs a running MySQL server and the `mysql-connector-j-26.7.0.jar` in `lib/`.

```bash
cd "Mysql Connect/jdbc"
javac -cp "lib/mysql-connector-j-26.7.0.jar" -d bin src/App.java
java  -cp "bin;lib/mysql-connector-j-26.7.0.jar" App
```

### 6. Student Registration Form (Webapp)
A web-based student registration form using plain HTML/CSS/JS, deployed under Tomcat.

```bash
# Place under Tomcat webapps, then start Tomcat
# Access at:
http://localhost:8080/studentform/
```

## License
MIT
