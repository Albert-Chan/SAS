
package com.dataminer.constants;

import java.nio.charset.Charset;

public interface Constants {

	public static final String DELIMITER = ",";

	public static final String YM_FORMAT = "yyyy/MM";
	public static final String YMD_FORMAT = "yyyy/MM/dd";
	public static final String YMDHMS_FORMAT = "yyyy/MM/dd HH:mm:ss";
	public static final String ORACLE_DATA_FORMAT = "yyyy/mm/dd hh24:mi:ss";

	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	
	public static final String KAFKA_BROKERS = "kafka.brokers";
	public static final String KAFKA_MONITOR_TOPIC = "kafka.monitor.topic";

}