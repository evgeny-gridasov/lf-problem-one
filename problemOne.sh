#!/bin/bash

function check_requisites() {
  if [[ ! $(type -t mvn) ]]; then
    echo "Maven executable not found. Please install Maven.";
    exit 1;
  fi;
  if [[ ! $(type -t java) ]]; then
    echo "Java executable not found. Please install Java 11.";
    exit 1;
  fi;
}

function mvn_compile() {
  if [[ ! -d target ]]; then
    mvn compile test;
    if [[ $? -ne 0 ]]; then
      echo
      echo "!!! BUILD FAILED !!!"
      echo
      echo -n "Cleaning target directory ... "
      MAVEN_OPTS='-XX:+TieredCompilation -XX:TieredStopAtLevel=1' mvn -q clean
      echo "done."
      exit 2;
    fi;
  fi;
}

function classpath() {
  export CLASSPATH=$(MAVEN_OPTS='-XX:+TieredCompilation -XX:TieredStopAtLevel=1' mvn -q exec:exec -Dexec.classpathScope="compile" -Dexec.executable="echo" -Dexec.args="%classpath");
}

check_requisites
mvn_compile

MODE=$1;

if [[ $MODE == "generate" ]]; then
 classpath
 java com.kata.problem.one.Generate $2 $3 $4
 exit $?;
fi;

if [[ $MODE == "convert" ]]; then
  classpath
 java com.kata.problem.one.Convert $2 $3 $4
 exit $?;
fi;

echo "Usage:

Generate fixed width file:
${0} generate spec.json output_file number_of_records

Convert fixed width file to CSV file:
${0} convert spec.json input_file output_file
"

exit 1;
