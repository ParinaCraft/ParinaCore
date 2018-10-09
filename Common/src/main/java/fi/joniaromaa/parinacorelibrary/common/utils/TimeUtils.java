package fi.joniaromaa.parinacorelibrary.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

public final class TimeUtils
{
	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
	private static final PeriodFormatter PERIOD_FORMAT = new PeriodFormatterBuilder().printZeroAlways().appendMinutes().appendSeparator(":").printZeroAlways().appendSeconds().appendSeparator(":").printZeroAlways().appendMillis().toFormatter();
	private static final PeriodFormatter SIMPLE_PERIOD_FORMAT = new PeriodFormatterBuilder().appendDays().appendSuffix("d").appendHours().appendSuffix("h").appendMinutes().appendSuffix("m").appendSeconds().appendSuffix("s").toFormatter();
	
	public static String getHumanReadableDate()
	{
		return TimeUtils.DATE_FORMAT.format(new Date());
	}
	
	public static String getHumanReadableDataFromNanos(long nanos)
	{
		return TimeUtils.PERIOD_FORMAT.print(new Period(nanos / 1000000L));
	}
	
	public static String getHumanReadableSimplePeriod(int seconds)
	{
		return TimeUtils.SIMPLE_PERIOD_FORMAT.print(new Period(seconds * 1000));
	}
}
