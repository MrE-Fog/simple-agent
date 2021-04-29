# Bouncy Castle issue
      - Tested on Java 8 with Ubuntu 18 and Windows 10
      - When running in FIPS mode with a Java agent, execution time deteriorate from 3 seconds to 300+ seconds
      - Sending one HTTPS GET request (which should normally take about 5 seconds)

# Build
`mvn clean package`

#Run (without bouncy castle)
### As a Java agent
`java -javaagent:<jar-path> -version`
### As a Java application
`java -jar <jar-path>`


## Run with bouncy castle (assuming required Jar `bc-fips-1.0.2.jar` is in `${JRE_HOME}/lib/ext`)
### A) As a Java agent
```
 java -Djavax.net.ssl.trustStoreType=BCFKS \ 
      -Djavax.net.ssl.trustStore=./config/cacerts.bcfks \ 
      -Djavax.net.ssl.trustStorePassword=changeit \    
      -Djava.security.properties==./config/java.security.fips \ 
      -javaagent:simple-agent-1.0-SNAPSHOT.jar -version
```      
### B) As a Java application
```
java  -Djavax.net.ssl.trustStoreType=BCFKS \
      -Djavax.net.ssl.trustStore=./config/cacerts.bcfks \
      -Djavax.net.ssl.trustStorePassword=changeit \  
      -Djava.security.properties==./config/java.security.fips \
      -jar simple-agent-1.0-SNAPSHOT.jar
```      


## Reproducing the issue
      - Execute command (A) and measure total time
      - Execute command (B) and measure total time
      - Exected result, total execution time should be about the same, however, when using option (A) the application takes much more time to run (two orders of magnitutde more)      


## Profiling Data
- See `./config/agent-profile.html`        
- CPU Profile was taken using: `https://github.com/jvm-profiling-tools/async-profiler`
- By adding:
`-agentpath:/opt/dev/async-profiler/build/libasyncProfiler.so=start,svg,file=profile.svg=start,svg,file=agent-cpu-profile.html`

Looks most of the time is spent after connecting to the server (possibly related to certificate validation?)  
