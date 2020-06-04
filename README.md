# UNO

This is simple uno engine with a CLI interface.

## Features
* A CLI interface
* Configurations available thru a yaml file

## Build/Run

***Requirements***

* JDK of preference (_11 or higher_)
  * [OpenJDK](https://openjdk.java.net/)
  * [ZuluJDK community](https://www.azul.com/downloads/zulu-community/?architecture=x86-64-bit&package=jdk)
  * [OracleJDk](https://www.oracle.com/java/technologies/javase-downloads.html)


***Linux/macOS***

run: `gradlew deploy`

***Windows***

run: `gradlew.bat deploy`

The generated jar will be deployed at `build/uno/ alongside the dependencies 
and resources 

## Configuration
A configuration file won't be created for you, but the program will look for one
at the same directory where the executable _jar_ is. This file has to go by the
name of _uno.yml_. You can find a sample at the project's _resources_ directory.
Bear in mind that the available settings are somewhat limited at this point.
