#!/bin/bash

usage() { echo "Usage: $0 [-m <server|client|tutorial>] [-c <start|stop|tutorial>] [-e <mr|tez>]" 1>&2; exit 1; }

# setting default values
m="tutorial";
c="start";
e="tez";

while getopts ":m:c:e:" o; do
    case "${o}" in
        m)
            m=${OPTARG}
            ((m == "server" || m == "client" || m == "tutorial")) || usage
            ;;
        c)
            c=${OPTARG}
            ((c == "start" || c == "stop")) || usage
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
    /bin/bash ./shell/big_data.sh ${m} ${c} ${e}
else
    /bin/bash ./shell/big_data.sh ${m} ${c} ${e} >& results-${e}.log
fi

