package org.afc.json.everit;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import org.everit.json.schema.FormatValidator;

import com.google.common.collect.ImmutableList;

public class DateFormatValidator implements FormatValidator {

	private static final List<String> FORMATS_ACCEPTED = ImmutableList.of("yyyy-MM-dd");

	private static final String PARTIAL_DATETIME_PATTERN = "yyyy-MM-dd";

	private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder().appendPattern(PARTIAL_DATETIME_PATTERN).toFormatter();

	@Override
	public Optional<String> validate(final String subject) {
		try {
			FORMATTER.parse(subject);
			return Optional.empty();
		} catch (DateTimeParseException e) {
			return Optional.of(String.format("[%s] is not a valid date. Expected %s", subject, FORMATS_ACCEPTED));
		}
	}

	@Override
	public String formatName() {
		return "date";
	}
}
