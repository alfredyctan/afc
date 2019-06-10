
package org.afc.gson;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GsonLocalDateTimeTypeAdapter extends TypeAdapter<LocalDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

	@Override
	public LocalDateTime read(JsonReader in) throws IOException {
		switch (in.peek()) {
		case NULL:
			in.nextNull();
			return null;
		default:
			String date = in.nextString();
			return LocalDateTime.parse(date, formatter);
		}
	}

	@Override
	public void write(JsonWriter out, LocalDateTime date) throws IOException {
		if (date == null) {
			out.nullValue();
		} else {
			out.value(formatter.format(date));
		}
	}
}