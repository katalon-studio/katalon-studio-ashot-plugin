package kms.turing.katalon.plugins.helper

import java.util.Formatter.DateTime

import groovy.time.TimeCategory
import groovy.time.TimeDuration

public class DateTimeHelper {
	/**
	 * Get elapsed time in seconds
	 * @param startTime
	 * @param endTime
	 * @return int
	 */
	public static int getElapsedTime(Date startTime, Date endTime){
		TimeDuration elapsedTime = TimeCategory.minus(startTime, endTime)
		return elapsedTime.getSeconds()
	}
}
