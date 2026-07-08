# iLib
Basic system for managing a library on Java using Look And Feel and DAO Pattern. The ITPLibrary Version 2.0.

## Features
✔️ Graphic interface with Material Design style (Using Look And Feel).\
✔️ MySQL database connection.\
✔️ Management of books, loans and returns.\
✔️ Penalties for late returns.\
✔️ Movement logs.\
✔️ Lookup tables.\
✔️ DAO Pattern.


## Getting Started
Import the Java project in your preferred IDE and import the database from `database/ilib.sql`. Just edit the class `Database.java` with your database connection credentials.

## Project Structure
- `src/main/java`: application source code.
- `src/main/resources`: images and static resources used by the UI.
- `src/test/java`: tests.
- `database/`: SQL scripts for local setup.
- `scripts/`: helper scripts for local execution.
- `logs/`: local runtime logs.
- `lib/`: local third-party JARs required by the project.

## Notes
- `target/` contains generated build artifacts and should not be treated as source code.
- The project script lives in `scripts/run-ilib.ps1`.
- If you open the parent folder as a VS Code workspace, `.vscode/run-ilib.ps1` simply delegates to the project script.
