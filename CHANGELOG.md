# Version v0.0.3:

### Fixed:
 -Application halt when warming up if logs directory wasn't present.

### Added:
 -Command: "-stopgd" which stops all Gradle Daemon background processes. Check the commands list for more details.

# Version v0.0.2:

### Fixed:
 -Stickerpacks not having more than 3 images.

 -Cwebp console consumption which didn't allow more than 5 input images.
 
 -Images not properly resized.

### Changed:
 -Syntax of command line arguments from "/command" to "-command".
 
 -Both CWebp and Gradle output their Error and Console outputs to a total of 4 log files in the "logs" folder.

### Added:
 -Command: "-warmup" which warms up gradle. After using this command subsequent builds will be much faster.

# Version v0.0.1:

First version. Only basic functionality available.
