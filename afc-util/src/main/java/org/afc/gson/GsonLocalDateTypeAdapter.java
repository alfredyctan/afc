package org.afc.gson;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GsonLocalDateTypeAdapter extends TypeAdapter<LocalDate> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

	@Override
	public void write(JsonWriter out, LocalDate date) throws IOException {
		if (date == null) {
			out.nullValue();
		} else {
			out.value(formatter.format(date));
		}
	}

	@Override
	public LocalDate read(JsonReader in) throws IOException {
		switch (in.peek()) {
		case NULL:
			in.nextNull();
			return null;
		default:
			String date = in.nextString();
			return LocalDate.parse(date, formatter);
		}
	}
}
