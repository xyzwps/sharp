#!/bin/bash

docker build -f env.dockerfile -t demo-env .

sleep 5
echo "1> "
docker run demo-env | grep HAHA

sleep 5
echo "2> "
# not working
docker run -e HAHA=hey demo-env | grep HAHA