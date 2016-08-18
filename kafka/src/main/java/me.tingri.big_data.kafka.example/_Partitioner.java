package me.tingri.big_data.kafka.example;

import kafka.producer.Partitioner;
import kafka.utils.VerifiableProperties;

public class _Partitioner implements Partitioner {
	public _Partitioner(VerifiableProperties props) {
		
	}

	public int partition(Object key, int a_numPartitions) {
		int partition = 0;
		String stringKey = (String) key;
		int offset = stringKey.lastIndexOf('.');
		
		if (offset > 0) {
			partition = Integer.parseInt(stringKey.substring(offset+1)) % a_numPartitions;
		}
		return partition;
	}

}
