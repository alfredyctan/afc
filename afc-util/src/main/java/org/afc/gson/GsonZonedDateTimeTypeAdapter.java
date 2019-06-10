
package org.afc.gson;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class GsonZonedDateTimeTypeAdapter extends TypeAdapter<ZonedDateTime> {

	private final static DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;

	@Override
	public ZonedDateTime read(JsonReader in) throws IOException {
		switch (in.peek()) {
		case NULL:
			in.nextNull();
			return null;
		default:
			String date = in.nextString();
			return ZonedDateTime.parse(date, formatter);
		}
	}

	@Override
	public void write(JsonWriter out, ZonedDateTime date) throws IOException {
		if (date == null) {
			out.nullValue();
		} else {
			out.value(formatter.format(date));
		}
	}
}