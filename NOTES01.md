# What Obervability Gives You
* Real-time visibility
* Find root causes fast
* Reduce blame game
* Better performance insights
* Happier developers & users

# What will you learn
* Observability with OpenTelemetry from scratch!

## Targeted Debugging
* Enable DEBUG Level logging **dynamically** for a specific request

# Observability & OpenTelemetry Basics

## Observability Vs Monitoring
* Monitoring (something is wrong)
* Observability (why it is wrong?)
* Fix

Example for an application:  
* Healthy -> Response time in 800ms
* Monitoring -> Response time > 2s
* Observability -> Slow DB Query
* Fix -> Adding Index

### Monitoring
* Monitoring is the process of collecting and analyzing predefined metrics (like cpu usage, response time, etc...) to detect issues and raise alerts when something goes wrong
* Example of predefined checks/alerts
  * CPU usage > 90%
  * Response Time > 2s
* Monitoring tells us "something is wrong"

## Observability
* Ability to understand the internal state of a system by analysing the outputs/signals it produces.

### Three Pilars of Observability
* Signals / Telemetry Data
  * Logs
  * Metrics
  * Traces


### Metrics
* Numeric Measuraments collected at regular intervals
* Track trends over time
* Example:
  * Application Throughput
  * Average Response Time
* Use Case
  * Monitoring / Alerting

### Logs
* Timestamped records of events that happened inside the system.
  * Diary of an application!
  * Human readable and detailed information about specific events.
* Use Case
  * If a user reports an error, the logs can provide the exact sequence of events that led to the problem.


### Traces
Represent the end-to-end journey of a single request as it flows to our distributed system.  
Debugging performance issues!  

Example:  
```
GET Request to api 1 -> (50ms)
    SQL SELECT Query
    POST to the API 2 -> (110ms)
        POST to the API 3 -> (50ms)
        Inserting data to a DB (120ms)
```

# Problem Statement
* Every observability vendor has its own SDK.
  * Prometheus, InfluxDB, Jaeger, Datadog, NewRelic, ELK,...
* No Standard format for telemetry data
* Vendor Lock-in
  * Observability was fragmented, vendor-specific, and costly to maintain.
  * Developers cloud NOT easily switch tools or get a unified view across logs, metrics, and traces.

## OpenTelemetry (Otel)
* JPA
  * Provide a standard API to interact with relational databases
  * Decouples your application from the relational database vendor
* Otel
  * Provides a standard API to collect telemetry (logs, metrics, traces)
  * Decouples your application from the observability backen/tools


### Before OpenTelemetry (OTEL)
add a image here.  


### OpenTelemetry (Otel) - Simplified
Otel collector.  
add a image here.  

## OpenTelemetry (Otel)
OpenTelemetry is a open-source, vendor-neutral observability framework that provides standard APIs, SDKs, and tools to generate, collect, and export telemetry data (logs, metrics and traces).  

# Why not Micrometer?
Micrometer vs OpenTelemetry.  
add a image here

* Pros
  * Deep integration with spring-boot
  * zero/auto configuration
  * Sumply/easy to learn
* Cons
  * Limited scope (Metrics only)
  * Lack of native distributed tracing
  * **Inconsistence & Fragmented Observability**

## Instrumentation
Adding the necessary code/tooling to your application so that it emits telemetry data (logs, metrics, traces).  

* Automatic / Zero-Code
  * You do NOT do any code change. Instead a specialized agent runs alongside your application to emit telemetry data
  * Good for:
    * Quick setup and instant visibility
    * Standard frameworks (HTTP, JDBC, gRPC, messageing, etc...)
    * Teams new to observability who want data without effort
* Manual / Code-based
  * You add the code to emit telemetry data
  * Good for:
    * Capturing business-specific operations (e.g., "available inventory" or "number of items sold")
    * Debugging complex issues beyond auto-instrumentation covers
    * Fine-grained control ovser what gets observed