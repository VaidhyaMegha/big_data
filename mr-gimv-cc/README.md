Change-log to original source code
-------------------------------------
* long instead of int to scale beyond ~2.1 billion
* vector generation is done using MR instead of  local.
* Join reduce is simplified to avoid self-loop checks
* Avoiding pull down content to local.
* Avoid summarize MR jobs instead use counters.
* Replace deprecated FileSystem api with new api calls.