#!/bin/bash
if [ "$1" == "-tez" ]; then
    /bin/bash ./shell/big_data.sh -tez >& results-tez.log
else
    /bin/bash ./shell/big_data.sh >& results.log
fi