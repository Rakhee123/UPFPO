package com.brightleaf.mongoservice.utils;

import java.util.Calendar;
import java.util.TimeZone;

public class GenerateTransactionId {
	public String generateTransactionId(int companyId) {
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("TXN_");
		stringbuilder.append(companyId);
		stringbuilder.append("_");
		synchronized (this) {
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			long time = cal.getTimeInMillis();
			stringbuilder.append(time);
		}

		return stringbuilder.toString();
	}
}