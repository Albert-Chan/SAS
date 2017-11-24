package com.dataminer.configuration.options;

import static com.dataminer.constants.Constants.YMD_FORMAT;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.function.Function;

public class ParserUtil {
	private static <T> Optional<T> convert(String value, Function<String, T> func) {
		try {
			return Optional.of(func.apply(value));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static Optional<String> toString(String value) {
		if (null == value || value.isEmpty())
			return Optional.empty();
		return Optional.of(value);
	}

	public static Optional<Boolean> toBoolean(String value) {
		if (null == value || value.isEmpty())
			return Optional.empty();
		return Optional.of(Boolean.valueOf(value));
	}

	public static Optional<Integer> toInt(String value) {
		return convert(value, Integer::valueOf);
	}

	public static Optional<Long> toLong(String value) {
		return convert(value, Long::valueOf);
	}

	public static Optional<Float> toFloat(String value) {
		return convert(value, Float::valueOf);
	}

	public static Optional<Double> toDouble(String value) {
		return convert(value, Double::valueOf);
	}

	public static Optional<LocalDate> toDate(String value) {
		try {
			return Optional.of(LocalDate.parse(value, DateTimeFormatter.ofPattern(YMD_FORMAT)));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static Optional<Long> minsToMilliSecs(String value) {
		return toLong(value).map(v -> v * 60L * 1000L);
	}
	
	// extends more converters ...
}