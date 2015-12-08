#!/bin/bash

usage() { echo "Usage: $0 [-m <server|client|tutorial>] [-c <start|stop|pegasus|giraph>] [-e <mr|tez>]" 1>&2; exit 1; }

# setting default values
m="tutorial";
c="start";
e="tez";

while getopts ":m:c:e:" o; do
    case "${o}" in
        m)
            m=${OPTARG}
            ((m == "server" || m == "client" || m == "tutorial" )) || usage
            ;;
        c)
            c=${OPTARG}
            ((c == "start" || c == "stop" || m == "pegasus" || c == "giraph")) || usage
            ;;
        e)
            e=${OPTARG}
            ((e == "mr" || e == "tez")) || usage
            ;;
        *)
            usage
            ;;
    esac
done

if [ "${m}" == "client" ]; then
    /bin/bash ./shell/big_data.sh ${m} ${c} ${e} 2>&1 | tee results-${c}-${e}-client.log
else
    /bin/bash ./shell/big_data.sh ${m} ${c} ${e} 2>&1 | tee results-${c}-${e}.log
fi

