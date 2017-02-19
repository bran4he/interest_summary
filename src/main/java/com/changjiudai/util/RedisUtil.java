package com.changjiudai.util;

public class RedisUtil {

	public static final String REPORT_MODEL_KEY = "reportData";

	public static final String REPORT_CURRENT_DATE_KEY = "reportCurrentDate";

	/**
	 * key: {username}reportData
	 * @return
	 */
	public static String getReportDataKey() {
		return CommonUtil.getUserName() + REPORT_MODEL_KEY;
	}

	/**
	 * key: {username}reportCurrentDate
	 * @return
	 */
	public static String getReportCurrentDateKey() {
		return CommonUtil.getUserName() + REPORT_CURRENT_DATE_KEY;
	}

}
