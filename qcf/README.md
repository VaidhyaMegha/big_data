QCF - Query Compressed Files
----------------------------
$ grep -ni "nav-links-analytics-data" datasets/tingri/access.log.6 | wc -l
138
$ grep -ni "nav-links-analytics-data" datasets/tingri/access.log.6.gz | wc -l
0
$ zgrep -ni "nav-links-analytics-data" datasets/tingri/access.log.6.gz | wc -l # uncompress and grep the file.
138
