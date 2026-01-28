# Sampling Strategies

## Problem Statement
* Do we really need to trace every single request?
* Who is going to look at the traces if everything if fine in production?
* Maintenance Cost

## Sampling
* Sampling is the process of deciding which traces (or spans) to record and export, and which ones to drop
  * Recording every single request in production could generate massive amounts of data (High costs). OpenTelemetry allows us to sample only a subset.
* Control the volume of telemetry data
* Reduce the storage and processing costs
* Still capture enough traces to troubleshoot issues efectvely 

### Types os Sampling
* Head Sampling
  * Always on (collect every single trace)
  * Always off (collect nothing)
  * Traceid Ratio (only keep a certain percentage of traces)
  * Parent Based Sampling
* Tail Sampling (Rule based Sampling) (The collector decides if it should keep the traces or drop it)
  * Status Code
  * Latency
  * Attribute (Fox example the URI/Path)

#### Always ON
* Non-production environments
* This is the default setting


#### Tail Sampling
The collector decide to keep or not.  
Use case -> Lets keep the traces that the requests fails. (Based on HTTP Status Code)
Keep the slow traces.  


# Metrics
* Metrics are numerical measurements collected over time.
* They are lightweight & aggregated.
  * Cheap to store & query
* Big picture view
* Perfect to detec
  * Memory leak
  * CPU usage/spikes
  * ...

## Types of Metrics
* Counter
  * Only goes Up
    * number os requests processes
    * number of errors
* Gauge / UpDownCounter
  * Can go up and down
    * CPU Usage
    * Available Inventory
* Histogram
  * Distribution of values
    * Request latency (requests completed in < 10ms, <50ms, <100ms, etc...)
    * Size of uploaded files (<10MB, <50MB)

1. Counter -> events
2. Gauge -> current state
3. Histogram -> distributions

## Metrics - Zero Code
* JVM Metrics
  * Memory usage
  * GC Stats (nunber of collections GC pause time)
  * Thread counts
  * Class loading counts (loaded / unloaded classes)
* CPU usage
* HTTP/gRPC Client and server metrics
  * Request count
  * Request duration (Histogram)
  * ...
* ...

### Tempo VS Prometheus
* Tempo
  * select * from traces
* Prometheus
  * aggregate queries like sum/avg with where
  * group by with having

PromQL -> Prometheus Query Language.  


### Metrics Backend
* Prometheus
* InfluxDB
* NewRelic
* Mimir
* Dynatrace
* ...

Each backend has its own query language.  

## Prometheus
* Prometheus is a time-series database designed for storing and querying metrics
  * Collects, Stores and QUery these metrics
* PromQL -> Prometheus Query Language
  * How many requests failed in the last 5 minutes?
  * What is the average response time of movie-service?

### Time Series DB (Simplified)
* Time-series data is LIKE a RDBMS table where the timestap (+ labels) acts as the primary key.
  * Table => Measurement / Metric

| timestap                 | service   | containerId   | value  |
|--------------------------|-----------|---------------|--------|
| 2025-09-19 00:12:02      |  movie-svc|   1020        |   23   |
| 2025-09-19 00:11:05      |  movie-svc|   3455        |   55   |
| 2025-09-19 00:10:14      |  movie-svc|   1020        |   10   |


### Prometheus Architecture
* Pull Based DB
* Prometheus is PULL based for metrics, it will ask for the metrics of the application
  * Service discovery


### Prometheus Data Accurary
* Metrics are NOT mission critical data. They are operational signals
* Metrics are NOT meant for business use cases like billing, customer transactions, or audits
* Metrics are aggregated
  * We do NOT store every single HTTP request details. We store counter, sums, and buckets.
* Data is scraped at regular intervals (e.g every 15 secods). Very short-lived events might be missed.
* Functions like rate(), increase() are approximations. It is all about finding the trends/patterns.