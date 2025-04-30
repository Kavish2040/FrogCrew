#!/bin/bash

# Find all test files and replace $.success with $.flag
find src/test/java/com/frogcrew/frogcrew/web/rest -name "*Test.java" -type f -exec sed -i '' 's/\$\.success/\$\.flag/g' {} \;

echo "Done replacing $.success with $.flag in all test files" 