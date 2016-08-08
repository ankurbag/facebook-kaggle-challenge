package com.neu.fbchallenge.config;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class FbConfig {
	
	public static double ACCURACY_LIMIT = 300;
	public static String KEY_SHIFT_1 = "s1"; 
	public static String KEY_SHIFT_2= "s2";
	public static String KEY_SHIFT_3 = "s3";
	
	
	public static String getTimeKey(String milliseconds){
		
		if(milliseconds != null && milliseconds.length() > 0){
			Date date = new Date(Long.parseLong(milliseconds));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if(date != null){
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				if(hour >=0 && hour <=8) return KEY_SHIFT_1;
				else if(hour > 8 && hour <=16)return KEY_SHIFT_2;
				else if(hour > 16 && hour <=24)return KEY_SHIFT_3;
			}
		}
		
		
		return KEY_SHIFT_1;
	}
	
	public static int getHour(String milliseconds){
		
		if(milliseconds != null && milliseconds.length() > 0){
			/*Date date = new Date(Long.parseLong(milliseconds));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
			if(date != null){
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
			
				return hour;
			}*/
			
			/*ZonedDateTime utc = Instant.ofEpochMilli(Long.parseLong(milliseconds)*1000).atZone(ZoneOffset.UTC);
			return utc.getHour();*/
			
			return (int)((Long.parseLong(milliseconds)/60) %24);
		}
		
		return 0;
	}

}
