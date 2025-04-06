# 🗃️ Redis-Style Key-Value Store (Java Server & Client)

A lightweight Redis-inspired server and client built in Java, supporting common Redis-like commands such as `SET`, `GET`, `DEL`, `EXISTS`, `SAVE`, `LOAD`, and more.

---

## 📦 Features

- Persistent key-value store
- Supports expiry (`EX`, `PX`, `EXAT`, `PXAT`)
- Atomic operations with concurrency-safe map
- `SAVE`/`LOAD` for persistence
- Custom Redis-like CLI commands

---

## 🚀 Commands Supported

| Command | Description |
|--------|-------------|
| `PING` | Responds with `PONG` |
| `ECHO <msg>` | Returns the message `<msg>` |
| `SET <key> <value>` | Sets a key with optional expiry and conditional flags. <br/> Current supported options are: <br> `EX`, `PX`, `EXAT`, `PXAT`, `NX` and `XX` |
| `GET <key>` | Returns the value of a key |
| `DEL <key>` | Deletes the specified key(s) |
| `EXISTS <key>` | Checks if a key exists |
| `SAVE <filename>` | Saves the current dictionary to a file |
| `LOAD <filename>` | Loads a saved dictionary from a file |
| `QUIT` | Closes the client |

---

## 🛠️ How to Run

### 💡 Prerequisites

- Java 17+
- Maven 3.6+

---

### 🖥️ Running the Server

```bash
mvn compile
java -cp "./target/classes" com.challenge.app.App

```
### 🖥 Running the Client

After the server is running, open a new terminal and run:

```bash
java Client.java 6379
```

## You’ll enter an interactive prompt where you can type commands like:
`SET` foo bar <br>
`GET` foo <br>
`SAVE` mydata.txt <br>
`LOAD` mydata.txt <br>
