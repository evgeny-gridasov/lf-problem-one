## Problem 1 Solution

### Prerequisites
 - Java 11
 - Maven 3.6

### Usage

For your convenience, everything has been automated through the `problemOne.sh` bash script. When run for the first time, it will compile the code and display usage:
```
Usage:

Generate fixed width file:
./problemOne.sh generate spec.json output_file number_of_records

Convert fixed width file to CSV file:
./problemOne.sh convert spec.json input_file output_file
``` 

### Limitations
 
 - Single threaded operation
 - Limited by local storage free space
  
  
### Potential improvements
 
 - Parallel operation with thread pools / queues
 - Use NIO for file I/O