package org.afc.gson;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GsonOffsetDateTimeTypeAdapter extends TypeAdapter<OffsetDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

	@Override
	public void write(JsonWriter out, OffsetDateTime date) throws IOException {
		if (date == null) {
			out.nullValue();
		} else {
			out.value(formatter.format(date));
		}
	}

	@Override
	public OffsetDateTime read(JsonReader in) throws IOException {
		switch (in.peek()) {
		case NULL:
			in.nextNull();
			return null;
		default:
			String date = in.nextString();
			if (date.endsWith("+0000")) {
				date = date.substring(0, date.length() - 5) + "Z";
			}

			return OffsetDateTime.parse(date, formatter);
		}
	}
}
