#!/bin/bash
# steps to install Hadoop 2.x release on single node cluster setup
# Prerequisites:
#       Java 6 installed
#       Download Hadoop tarball - hadoop-2.4.1.tar.gz  from http://archive.apache.org/dist/hadoop/core/hadoop-2.4.1/
#           and extract contents to say /home/sandeep/tools/hadoop/2.4.1
#       Download Hive tarball - https://archive.apache.org/dist/hive/hive-0.13.1/apache-hive-0.13.1-bin.tar.gz
#           from  https://archive.apache.org/dist/hive/hive-0.13.1/
#           and extract contents to say /home/sandeep/tools/hive/0.13.1
#       Install protobuf - http://www.confusedcoders.com/random/how-to-install-protocol-buffer-2-5-0-on-ubuntu-13-04
#       Clone Tez - https://github.com/apache/tez, checkout branch-0.5.1
#           and install it - http://tez.apache.org/install.html
#       Clone Giraph  - git clone http://git-wip-us.apache.org/repos/asf/giraph.git
#           build it - http://giraph.apache.org/build.html
#           deploy it - http://giraph.apache.org/quick_start.html#qs_section_4
echo "###############"
echo "Setup"
echo "###############"
# Environment variables that can not be pushed to env.sh are here.
export USER=sandeep
export PROJECT_HOME=/home/$USER/projects/big_data
export SHELL_HOME=$PROJECT_HOME/shell

# Setup Environment Variables
source $SHELL_HOME/env.sh

# Move to HADOOP home
cd $HADOOP_HOME

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
            if [ "$2" == "giraph" ]; then
                echo "###############"
                echo "# Setup and execute Giraph"
                echo "###############"
                source $SHELL_HOME/giraph.sh
            else
                echo "#######"
                echo "# HIVE"
                echo "#######"
                source $SHELL_HOME/hive.sh

                $HIVE_HOME/bin/hive -v
            fi
        fi
    fi
fi
