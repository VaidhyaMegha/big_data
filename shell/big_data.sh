#!/bin/bash
echo "###############"
echo "Setup"
echo "###############"
# Environment variables that can not be pushed to env.sh are here.
export USER=sandeepkunkunuru
export PROJECT_HOME=/home/$USER/projects/big_data
export SHELL_HOME=$PROJECT_HOME/shell

# Setup Environment Variables
source $SHELL_HOME/env.sh

if [ "$1" == "tutorial" ]; then
    echo "###############"
    echo "Configure"
    echo "###############"
    # configure hadoop services and libraries
    source $SHELL_HOME/config.sh

    echo "###############"
    echo "# Start HDFS processes"
    echo "###############"
    source $SHELL_HOME/start.sh

    echo "###############"
    echo "# Setup Tez"
    echo "###############"
    source $SHELL_HOME/tez.sh

    echo "###############"
    echo "# Web interface"
    echo "###############"
    source $SHELL_HOME/ui.sh

    echo "##############"
    echo "# Map Reduce"
    echo "##############"
    source $SHELL_HOME/mr.sh

    echo "#######"
    echo "# HIVE"
    echo "#######"
    source $SHELL_HOME/hive.sh

    echo "########################"
    echo "# Stop the processes"
    echo "########################"
    source $SHELL_HOME/stop.sh
else
    if [ "$1" == "server" ]; then
        if [ "$2" == "start" ]; then
            echo "###############"
            echo "Configure"
            echo "###############"
            # configure hadoop services and libraries
            source $SHELL_HOME/config.sh

            echo "###############"
            echo "# Start HDFS processes"
            echo "###############"
            source $SHELL_HOME/start.sh

            if [ "$3" == "tez" ]; then
                echo "###############"
                echo "# Setup Tez"
                echo "###############"
                source $SHELL_HOME/tez.sh
            fi
        else
            echo "########################"
            echo "# Stop the processes"
            echo "########################"
            source $SHELL_HOME/stop.sh
        fi
    else
        if [ "$1" == "client" ]; then
            if [ "$2" == "pegasus" ]; then
                echo "###############"
                echo "# Setup and execute Pegasus"
                echo "###############"
                source $SHELL_HOME/pegasus.sh
            elif [ "$2" == "mapreduce" ]; then
                echo "###############"
                echo "# Run MapReduce example and leave REPL prompt"
                echo "###############"
                source $SHELL_HOME/mr.sh
            elif [ "$2" == "hbase" ]; then
                echo "###############"
                echo "# Setup and execute HBASE"
                echo "###############"
                source $SHELL_HOME/hbase_sample.sh
            else
                echo "#######"
                echo "# HIVE"
                echo "#######"
                source $SHELL_HOME/hive.sh

                if [ "$3" == "tez" ]; then
                    hive -v --hiveconf hive.execution.engine=tez
                else
                    hive -v
                fi
            fi
        fi
    fi
fi
