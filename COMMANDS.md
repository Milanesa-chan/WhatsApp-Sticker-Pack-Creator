# Command-Line Commands

You can use these commands when you execute the app from a command-line.

### Running from a command-line

The file is a jar file so you have to open a command prompt in the .jar folder and execute: 'java -jar "WASPC.jar" -command1'.

WASPC.jar can have a different name.

You can execute with no command.

### Command Syntax

Since version v0.0.2 all commands are preceded with a minus. There are no double-minus or slash comands. Some commands have an argument which has to be separated by a space. "command1" syntax is "-command1 argument".

### Commands List

- **warmup**: Only starts a gradle build. After execution is finished subsequent builds will be much faster. Only useful for automated execution or to save time if you have to wait for a build. No arguments needed.

- **packname NAME**: Skips the "input packname" prompt and in it's place sets "NAME" to it. Replace NAME with any name. The name can't have any spaces and has to be between 3 and 32 characters. If these conditions aren't met build will stop.