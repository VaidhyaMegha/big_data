Change-log to original source code
-------------------------------------
* long instead of int to scale beyond ~2.1 billion
* vector generation is done using MR instead of  local.
    * Iteration 1 is modified to generate Vector hence iterations are reduced by 1.
* Join reduce is simplified to avoid self-loop checks
* Avoid pulling down content to local instead use counters.
* Avoid summarize MR jobs .
* Replace deprecated FileSystem api with new api calls.
* Unit Test cases with graph containing edges 1--2, 2--5 and 3--4

How-to use/run this module
--------------------------
* Command to start hadoop
    
        sudo ./run.sh -m server -c start -e mr
* Command to run GIMV-CC, PEGASUS CC and get PEGASUS REPL
        
        sudo ./run.sh -m client -c pegasus -e mr
* Command to stop hadoop
    
        sudo ./run.sh -m server -c stop -e mr
        
Sample  Input and Output
------------------------
* Input - catepillar_star.edge graph in PEGASUS library

        0	1
        1	2
        1	3
        3	4
        3	6
        5	6
        6	7
        6	8
        6	9
        10	11
        10	12
        10	13
        10	14
        10	15

* Output from rewrite - mr-gimv-cc

        0	v0
        3	v0
        6	v0
        9	v0
        12	v10
        15	v10
        1	v0
        4	v0
        7	v0
        10	v10
        13	v10
        2	v0
        5	v0
        8	v0
        11	v10
        14	v10
        
* Output from PEGASUS library

        0	msf0
        3	msf0
        6	msf0
        9	msf0
        12	msf10
        15	msf10
        1	msf0
        4	msf0
        7	msf0
        10	msf10
        13	msf10
        2	msf0
        5	msf0
        8	msf0
        11	msf10
        14	msf10

        
        
        
        