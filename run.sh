#!/usr/bin/env bash
set -e
mkdir -p out
javac -d out src/main/java/hms/*.java
java -cp out hms.HospitalManagementSystem
