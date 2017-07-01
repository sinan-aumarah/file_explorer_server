### Prerequisites
 1. JDK1.8
 2. Gradle 3.5+

### Quick start
Run the app using 
`gradle run`


### Usage
1. Via generated jar
  - Run `gradle clean build jar`
  - cd to build/libs/
  - run `java -jar fileServer-1.0-SNAPSHOT.jar "{optional: path to directory}"`
    - i.e. `java -jar fileServer-1.0-SNAPSHOT.jar` -> will use current directory
    - i.e. `java -jar fileServer-1.0-SNAPSHOT.jar "c:/"` -> will explore the C drive


2. Via gradle
  - `gradle run -Pargs="{path to directory}"`
    - i.e. `gradle run -Pargs="c:/" `
    
    
#### Changing port number 
    You can Change SOCKET_PORT inside FileServerApp
    Default is set to 3222
    
    
### Design decisions: 
You will notice I used interfaces for everything even for something as simple as FileDownloader. This mainly
a practice that I always do to hide the implementation and also testing purposes. You can always go and change the implementation
without worrying about the side effects.

### App structure and functionality
HttpFileServer#doRun(): Runs a simple HTTP server using HTTP1.1 spec. When a request is received
  it will validated it and then checks if it's a;
- Directory?
    - Use DisplayService.java implementation to draw HTML table of the requested directory
- File?
   - Use FileDownloaderService.java implementation to download the file
- Invalid request
   - Return an error page



### What's missing? and future improvements
- Sorting
- Socket Number as a program argument (Currently it's hardcoded)
- Not hiding the program's jar from the explorer!
- Threading
- Use log4j or any logging library


    
    
