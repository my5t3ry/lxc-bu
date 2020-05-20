#!/usr/bin/env bash
if [[ ! `cat target/native-image/test-output.txt | grep -E "Application run failed|No suitable logging system located"` ]]; then
if [[ "$RESPONSE" == '{"value":"Hello"}' ]]; then
  exit 0
else
  exit 1
fi
else
  exit 1
fi
