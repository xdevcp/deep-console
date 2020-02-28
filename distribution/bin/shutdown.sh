#!/bin/sh

# Copyright 2020

cd `dirname $0`/../target
target_dir=`pwd`

pid=`ps ax | grep -i 'app.app' | grep ${target_dir} | grep java | grep -v grep | awk '{print $1}'`
if [ -z "$pid" ] ; then
        echo "No appServer running."
        exit -1;
fi

echo "The appServer(${pid}) is running..."

kill ${pid}

echo "Send shutdown request to appServer(${pid}) OK"
