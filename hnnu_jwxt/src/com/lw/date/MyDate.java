package com.lw.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyDate {

	
	 public static String getDate(int n){
		 SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	        Date d=new Date();
	        Calendar rightNow = Calendar.getInstance();
	        rightNow.setTime(d);
	        rightNow.add(Calendar.DAY_OF_YEAR,n);//日期加10天
	        Date dt1=rightNow.getTime();
	        String reStr = sdf.format(dt1);
	        return reStr;
	 }
}
