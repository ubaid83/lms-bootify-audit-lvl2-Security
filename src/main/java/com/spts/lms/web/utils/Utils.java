package com.spts.lms.web.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

public class Utils {

	private static final Logger logger = Logger.getLogger(Utils.class);

	public static String getBlankIfNull(Object object) {
		return (object == null) ? "" : object.toString();
	}

	public static String formatDate(String formatFrom, String formatTo,
			String dateVal) {
		if (dateVal == null)
			return "";
		DateFormat format1 = new SimpleDateFormat(formatFrom);
		String dateString = "";
		Date date;
		try {
			date = format1.parse(dateVal);
			DateFormat format2 = new SimpleDateFormat(formatTo);
			dateString = format2.format(date);
		} catch (ParseException e) {
			logger.error("Exception", e);
		}

		return dateString;
	}

	public static String formatDate(String formatTo, Date date) {
		if (date == null)
			return "";
		DateFormat format2 = new SimpleDateFormat(formatTo);
		String dateString = format2.format(date);

		return dateString;
	}
	
	public static Date converFormatsDateAlt(String ip) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String formatOut = "yyyy-MM-dd HH:mm:ss";

		Date d = null;
		SimpleDateFormat out = new SimpleDateFormat(formatOut);
		try {
			if (ip != null) {
				d = sdf.parse(ip);

			}
		} catch (Exception e) {
			logger.error("exception", e);
		}
		return d;

	}

	public static int calculateDaysAgo(String formatFromDate)
			throws ParseException {

		LocalDate date1 = LocalDate.parse(formatFromDate);
		LocalDate date2 = LocalDate.now();

		return Days.daysBetween(date1, date2).getDays();
	}

	public static String getCompletionTime(Date completionTime, int duration) {
		logger.info("completeionTime" + completionTime);

		DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		int hour = completionTime.getHours();
		int minutes = completionTime.getMinutes();
		int seconds = completionTime.getSeconds() + 5;

		String time = hour + ":" + minutes + ":" + seconds;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date d;
		String testEndTime = null;
		try {
			d = df.parse(time);

			Calendar cal = Calendar.getInstance(TimeZone
					.getTimeZone("Asia/Calcutta"));
			cal.setTime(d);
			cal.add(Calendar.MINUTE, duration);
			testEndTime = date.format(completionTime) + " "
					+ df.format(cal.getTime());

		} catch (ParseException e) {

			logger.error("exception", e);
		}
		logger.info("calc Time" + testEndTime);

		return testEndTime;

	}

	public static String changeTestStartTimeFormat(Date completionTime) {
		logger.info("completeionTime" + completionTime);

		DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		int hour = completionTime.getHours();
		int minutes = completionTime.getMinutes();
		int seconds = completionTime.getSeconds() + 5;

		String time = hour + ":" + minutes + ":" + seconds;

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		Date d;
		String startTimeTime = null;
		try {
			d = df.parse(time);

			Calendar cal = Calendar.getInstance(TimeZone
					.getTimeZone("Asia/Calcutta"));
			cal.setTime(d);

			startTimeTime = date.format(completionTime) + " "
					+ df.format(cal.getTime());

		} catch (ParseException e) {

			logger.error("exception", e);
		}
		logger.info("calc Time" + startTimeTime);

		return startTimeTime;

	}

	public static Date getCompletionTimeDateType(Date completionTime,
			int duration) {

		logger.info("raw date -----------------" + completionTime);
		Calendar cal = Calendar.getInstance(TimeZone
				.getTimeZone("Asia/Calcutta"));
		if (completionTime != null) {
			cal.setTime(completionTime);
		}

		cal.add(Calendar.MINUTE, duration);
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
		String formatted = format1.format(cal.getTime());
		logger.info("formatted-----------------" + formatted);
		Date completionDate = null;
		try {
			completionDate = format1.parse(formatted);
			logger.info("after formatted ------------------" + completionDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return completionDate;
	}

	public static Date getInIST() {

		DateTime dt = new DateTime(Calendar.getInstance());
		DateTimeZone dtZone = DateTimeZone.forID("Asia/Calcutta");
		DateTime dtus = dt.withZone(dtZone);
		Date date = dtus.toLocalDateTime().toDate();
		logger.info("IST is -->" + date);
		return date;

	}

	public static String converFormats(String ip) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatOut = "yyyy-MM-dd'T'HH:mm:ss";
		String res = null;
		SimpleDateFormat out = new SimpleDateFormat(formatOut);
		try {
			if (ip != null) {
				Date d = sdf.parse(ip);
				res = out.format(d);
			}
		} catch (Exception e) {
			logger.error("exception", e);
		}
		return res;

	}

	public static Date converFormatsDate(String ip) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String formatOut = "yyyy-MM-dd'T'HH:mm:ss";

		Date d = null;
		SimpleDateFormat out = new SimpleDateFormat(formatOut);
		try {
			if (ip != null) {
				d = sdf.parse(ip);

			}
		} catch (Exception e) {
			logger.error("exception", e);
		}
		return d;

	}
	
public static String addDaysToDate(String date,int numberOfDaysToAdd){
		
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try{
		   //Setting the date to the given date
		   c.setTime(sdf.parse(date));
		}catch(ParseException e){
			e.printStackTrace();
		 }
		   
		//Number of Days to add
		c.add(Calendar.DAY_OF_MONTH, numberOfDaysToAdd);  
		//Date after adding the days to the given date
		String newDate = sdf.format(c.getTime());  
		//Displaying the new Date after addition of Days
		System.out.println("Date after Addition: "+newDate);
		
		return newDate;
	}
	
	public int compareToDate(String date1,String date2){
	       int var1 = date1.compareTo( date2 );
	       System.out.println("str1 & str2 comparison: "+var1);
	       return var1;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
	
	public static <T> Predicate<T> distinctByKeys(Function<? super T, ?>... keyExtractors) 
	  {
	    final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
	     
	    return t -> 
	    {
	      final List<?> keys = Arrays.stream(keyExtractors)
	                  .map(ke -> ke.apply(t))
	                  .collect(Collectors.toList());
	       
	      return seen.putIfAbsent(keys, Boolean.TRUE) == null;
	    };
	  }
	
	public static <T> boolean IsNullOrEmpty(Collection<T> list) {
	    return list == null || list.isEmpty();
	}	
	
	/* New Audit changes start */
	public static boolean validateStartAndEndDates(String date1,String date2){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d1 = null;
		Date d2 = null;
		Date d3 = Utils.getInIST();
		String date3 = format.format(d3);
		date3 = date3.split(" ")[0].concat(" 00:00:00");
//		System.out.println("date3: "+date3);
//		long startEndDiff = 0;
//		long startDiff = 0;
//		long endDiff = 0;
		try {
			if(date1.contains("T")) {
				date1 = date1.replace("T", " ");
			}
			if(date2.contains("T")) {
				date2 = date2.replace("T", " ");
			}
			d1 = format.parse(date1);
			d2 = format.parse(date2);
			d3 = format.parse(date3);
			if(d1.after(d2)) {
//				System.out.println("False - startDate after endDate");
				return false;
			}
			if(d1.compareTo(d2) == 0) {
//				System.out.println("False - startDate equals endDate");
				return false;
			}
			if(d1.before(d3)) {
//				System.out.println("False - startDate before currentDate");
				return false;
			}
			if(d2.before(d3)) {
//				System.out.println("False - endDate before currentDate");
				return false;
			}
//			System.out.println("True");
//			startEndDiff = ((d2.getTime() - d1.getTime()) / 1000);
//			startDiff = ((d1.getTime() - d3.getTime()) / 1000);
//			endDiff = ((d2.getTime() - d3.getTime()) / 1000);
//			System.out.println("startEndDiff: "+startEndDiff);
//			System.out.println("startDiff: "+startDiff);
//			System.out.println("endDiff: "+endDiff);
//			if(startEndDiff > 0 && startDiff > - 300 && endDiff > 0) {
//				System.out.println("True");
//			}else {
//				System.out.println("False");
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	/* New Audit changes end */
}
