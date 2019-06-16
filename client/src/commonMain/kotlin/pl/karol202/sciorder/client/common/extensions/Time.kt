package pl.karol202.sciorder.client.common.extensions

enum class TimeUnit(val millis: Long)
{
	MILLIS(1),
	SECONDS(1000),
	MINUTES(60 * TimeUnit.SECONDS.millis),
	HOURS(60 * TimeUnit.MINUTES.millis),
	DAYS(24 * TimeUnit.HOURS.millis)
}

expect fun currentTimeMillis(): Long
