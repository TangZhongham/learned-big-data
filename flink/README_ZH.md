<!--
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.
-->

# Apache Flink Training Exercises

Exercises that go along with the training content in the documentation.

本次练习会通过一个真实的流处理场景去介绍 Flink 的基本使用，包含 Filter、Window 和 Process Function 的使用。

## 目录

[**开发环境设置**](#setup-your-development-environment)

1. [软件要求](#software-requirements)
1. [Clone and build the flink-training project](#clone-and-build-the-flink-training-project)
1. [导入项目到IDE](#import-the-flink-training-project-into-your-ide)

[**使用 Taxi Ride 数据源**](#using-the-taxi-data-streams)

1. [Taxi Ride 事件的数据schema](#schema-of-taxi-ride-events)
1. [Generating Taxi Ride Data Streams in a Flink program](#generating-taxi-ride-data-streams-in-a-flink-program)

[**如何完成 Lab**](#how-to-do-the-labs)

1. [了解Data格式](#learn-about-the-data)
1. [修改 `ExerciseBase`](#modify-exercisebase)
1. [在IDE里跑跑并DEBUG你的Flink program](#run-and-debug-flink-programs-in-your-ide)
1. [Exercises, Tests, and Solutions](#exercises-tests-and-solutions)

[**Labs**](LABS-OVERVIEW_ZH.md)

[**License**](#license)

## Setup your Development Environment

The following instructions guide you through the process of setting up a development environment for the purpose of developing, debugging, and executing solutions to the Flink developer training exercises and examples.

接下来会手把手带你去搭建一个Flink 开发环境

### Software requirements

Flink supports Linux, OS X, and Windows as development environments for Flink programs and local execution. The following software is required for a Flink development setup and should be installed on your system:

- a JDK for Java 8 or Java 11 (单独 JRE 不行，其他版本的 JDK 暂时不支持)
- Git
- an IDE for Java (and/or Scala) development with Gradle support.
  We recommend IntelliJ, but Eclipse or Visual Studio Code can also be used so long as you stick to Java. For Scala you will need to use IntelliJ (and its Scala plugin).

> **:information_source: Note for Windows users:** The examples of shell commands provided in the training instructions are for UNIX systems. To make things easier, you may find it worthwhile to setup cygwin or WSL. For developing Flink jobs, Windows works reasonably well: you can run a Flink cluster on a single machine, submit jobs, run the webUI, and execute jobs in the IDE.

### Clone and build the flink-training project

This `flink-training` project contains exercises, tests, and reference solutions for the programming exercises. Clone the `flink-training` project from Github and build it.

> **:information_source: Repository Layout:** This repository has several branches set up pointing to different Apache Flink versions, similarly to the [apache/flink](https://github.com/apache/flink) repository with:
> - a release branch for each minor version of Apache Flink, e.g. `release-1.10`, and
> - a `master` branch that points to the current Flink release (not `flink:master`!)
>
> If you want to work on a version other than the current Flink release, make sure to check out the appropriate branch.

```bash
git clone https://github.com/apache/flink-training.git
cd flink-training
./gradlew test shadowJar
// 如果看到 抛出 ERROR 那么说明 build 成功了，开始完成练习吧
```

If you haven’t done this before, at this point you’ll end up downloading all of the dependencies for this Flink training project. This usually takes a few minutes, depending on the speed of your internet connection.

If all of the tests pass and the build is successful, you are off to a good start.

<details>
<summary><strong>Users in China: click here for instructions about using a local maven mirror.</strong></summary>

If you are in China, we recommend configuring the maven repository to use a mirror. You can do this by uncommenting the appropriate line in our [`build.gradle`](build.gradle) like this:

```groovy
    repositories {
        // for access from China, you may need to uncomment this line
        maven { url 'http://maven.aliyun.com/nexus/content/groups/public/' }
        mavenCentral()
        maven {
            url "https://repository.apache.org/content/repositories/snapshots/"
            mavenContent {
                snapshotsOnly()
            }
        }
    }
```
</details>


### Import the flink-training project into your IDE

The project needs to be imported as a gradle project into your IDE.

Once that’s done you should be able to open [`RideCleansingTest`](ride-cleansing/src/test/java/org/apache/flink/training/exercises/ridecleansing/RideCleansingTest.java) and successfully run this test.

> **:information_source: Note for Scala users:** You will need to use IntelliJ with the JetBrains Scala plugin, and you will need to add a Scala 2.12 SDK to the Global Libraries section of the Project Structure. IntelliJ will ask you for the latter when you open a Scala file.

## Using the Taxi Data Streams

These exercises use data [generators](common/src/main/java/org/apache/flink/training/exercises/common/sources) that produce simulated event streams 
inspired by those shared by the [New York City Taxi & Limousine Commission](http://www.nyc.gov/html/tlc/html/home/home.shtml)
in their public [data set](https://uofi.app.box.com/NYCtaxidata) about taxi rides in New York City.

数据集来源是通过一个编写的data gernerators 去模拟纽约市出租车的开源数据集

### Schemas of Taxi Ride and Taxi Fare Events

Our taxi data set contains information about individual taxi rides in New York City. Each ride is represented by two events: a trip start, and a trip end event. Each event consists of eleven fields:

我们的 taxi data 数据集包括纽约市每趟出租车taxi rides。每趟出租车ride 有两个事件组成：trip start 和 trip end。每个事件包含以下七个字段：

```
rideId         : Long      // a unique id for each ride 每一趟的id
taxiId         : Long      // a unique id for each taxi 每台车id
driverId       : Long      // a unique id for each driver 每个司机id
isStart        : Boolean   // TRUE for ride start events, FALSE for ride end events 一个代表是ride 开始还是结束的bool字段
startTime      : Instant   // the start time of a ride 开始时间
endTime        : Instant   // the end time of a ride, 结束时间
                           //   "1970-01-01 00:00:00" for start events （开始时间暂时都是该值）
startLon       : Float     // the longitude of the ride start location 事件开始坐标的经度
startLat       : Float     // the latitude of the ride start location 事件开始坐标的纬度
endLon         : Float     // the longitude of the ride end location 事件结束坐标的经度
endLat         : Float     // the latitude of the ride end location 事件结束坐标的纬度
passengerCnt   : Short     // number of passengers on the ride 每趟车有几位乘客
```

There is also a related data set containing fare data about those same rides, with these fields:

同时还有一个辅助的费用数据集，和rides 相关联

```
rideId         : Long      // a unique id for each ride 每一趟的id
taxiId         : Long      // a unique id for each taxi 每台车id
driverId       : Long      // a unique id for each driver 每个司机id
startTime      : Instant   // the start time of a ride  开始时间
paymentType    : String    // CASH or CARD 支付方式
tip            : Float     // tip for this ride 小费金额
tolls          : Float     // tolls for this ride 车费
totalFare      : Float     // total fare collected 总费用
```

## How to do the Labs

这个 Lab 会教你用基本的 Flink APIs

In the hands-on sessions you will implement Flink programs using various Flink APIs.

The following steps guide you through the process of using the provided data streams, implementing your first Flink streaming program, and executing your program in your IDE.

We assume you have setup your development environment according to our [setup guide above](#setup-your-development-environment).

### Learn about the data

The initial set of exercises are all based on data streams of events about taxi rides and taxi fares. These streams are produced by source functions which reads data from input files. Please read the [instructions above](#using-the-taxi-data-streams) to learn how to use them.

### Run and debug Flink programs in your IDE

Flink programs can be executed and debugged from within an IDE. This significantly eases the development process and provides an experience similar to working on any other Java (or Scala) application.

Starting a Flink program in your IDE is as easy as running its `main()` method. Under the hood, the execution environment will start a local Flink instance within the same process. Hence it is also possible to put breakpoints in your code and debug it.

Assuming you have an IDE with this `flink-training` project imported, you can run (or debug) a simple streaming job as follows:

- Open the `org.apache.flink.training.examples.ridecount.RideCountExample` class in your IDE
- Run (or debug) the `main()` method of the `RideCountExample` class using your IDE.

### Exercises, Tests, and Solutions

Each of these exercises includes an `...Exercise` class with most of the necessary boilerplate code for getting started, as well as a JUnit Test class (`...Test`) with a few tests for your implementation, and a `...Solution` class with a complete solution.

> **:information_source: Note:** As long as your `...Exercise` class is throwing a `MissingSolutionException`, the provided JUnit test classes will ignore that failure and verify the correctness of the solution implementation instead.

There are Java and Scala versions of all the exercise, test, and solution classes.

-----

Now you are ready to begin with the first exercise in our [**Labs**](LABS-OVERVIEW_ZH.md).

-----

## License

The code in this repository is licensed under the Apache Software License 2.
