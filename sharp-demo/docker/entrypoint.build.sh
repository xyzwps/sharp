#!/bin/bash

docker build -f entrypoint.dockerfile -t demo-entrypoint .

sleep 5
docker run demo-entrypoint

sleep 5
# not working
docker run demo-entrypoint arg1 arg2 "arg3=xx"