# TodoBot

A comprehensive task management application built in Java with both CLI and GUI interfaces.

## Features

- **Multiple Task Types**: Support for todos, deadlines, and events
- **Priority Management**: Set task priorities (High, Medium, Low)
- **Flexible Interface**: Both command-line and graphical user interfaces
- **Persistent Storage**: Tasks are automatically saved and loaded
- **Smart Parsing**: Intuitive command parsing with comprehensive error handling
- **Search Functionality**: Find tasks by keyword
- **Task Management**: Add, delete, mark/unmark, and list tasks

## Getting Started

### Prerequisites

- Java 24 or higher
- Gradle (included via wrapper)

### Building the Application

```bash
# Build the project
./gradlew build

# Create standalone executable jar
./gradlew shadowJar
```

### Running the Application

#### GUI Mode (Default)
```bash
java -jar app/build/libs/app-all.jar
```

#### CLI Mode
```bash
java -jar app/build/libs/app-all.jar --cli
```

#### Development Mode
```bash
./gradlew run
```

## Usage

### Available Commands

- `todo <description>` - Add a new todo task
- `deadline <description> /by <date> [time]` - Add a deadline task
- `event <description> /from <date> [time] /to <date> [time]` - Add an event
- `list` - Show all tasks
- `mark <task_number>` - Mark task as completed
- `unmark <task_number>` - Mark task as not completed
- `delete <task_number>` - Delete a specific task
- `deleteall` - Delete all tasks (with confirmation)
- `find <keyword>` - Search for tasks containing keyword
- `priority <task_number> <level>` - Set task priority (high/h, medium/m, low/l)
- `help` - Show available commands
- `bye` - Exit the application

### Date Format

Use `dd-MM-yyyy` format for dates (e.g., `25-12-2024`)
Time format: `HH:mm` (e.g., `14:30`)

### Examples

```bash
# Add a simple todo
todo Read a book

# Add a deadline with date and time
deadline Submit assignment /by 25-12-2024 23:59

# Add an event
event Team meeting /from 26-12-2024 09:00 /to 26-12-2024 10:30

# Set priority
priority 1 high

# Find tasks
find book
```

## Architecture

The application follows a clean architecture pattern with:

- **Model**: Task entities (Todo, Deadline, Event) with priority support
- **Parsers**: Command parsing with regex-based validation
- **Service**: Business logic and task management
- **Storage**: JSON-based persistence using Gson
- **UI**: Separate CLI and GUI implementations
- **Commands**: Command pattern for action execution

## Testing

Run the test suite:

```bash
./gradlew test
```

The project includes comprehensive unit tests covering:
- Model classes and validation
- Parser logic and edge cases
- Service layer functionality
- Storage serialization/deserialization
- Error handling scenarios

## Technologies Used

- **Java 24**: Core language
- **JavaFX**: GUI framework
- **Gson**: JSON serialization
- **JUnit 5**: Testing framework
- **Gradle**: Build system

## Project Structure

The main application code is organized under app/src/main/java/org/todobot/ with the following packages:

- **app/** - Application entry points (Launcher, Main, CLI, GUI)
- **commands/** - Command implementations using command pattern
- **common/** - Shared constants, enums, and bot messages
- **model/** - Task entities (Todo, Deadline, Event, Priority)
- **parsers/** - Command parsing logic with regex validation
- **service/** - Business logic and task management
- **storage/** - Data persistence using JSON serialization
- **ui/** - User interface components and themes

Unit tests are located in app/src/test/java/ following the same package structure.

## License

This project is for educational purposes.