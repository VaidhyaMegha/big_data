QCF - Query Compressed Files
----------------------------
* Search in uncompressed file
 
        $ grep -ni "nav-links-analytics-data" datasets/tingri/access.log.6 | wc -l
        138

* Uncompress and search a compressed file
        
        $ zgrep -ni "nav-links-analytics-data" datasets/tingri/access.log.6.gz | wc -l # uncompress and grep the file.
        138
        
* Search fails in compressed file

        $ grep -ni "nav-links-analytics-data" datasets/tingri/access.log.6.gz | wc -l
        0
