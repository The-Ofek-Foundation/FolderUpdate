#!/bin/bash
javac OFile/OFile.java
javac OPrompt/OPrompt.java
javac -cp ".:OFile/.:OPrompt/." FolderUpdate.java
