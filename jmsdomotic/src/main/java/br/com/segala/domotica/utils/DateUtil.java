package br.com.segala.domotica.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class DateUtil {
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

	public static String format(Date date) {
		return sdf.format(date);
	}
	
}
