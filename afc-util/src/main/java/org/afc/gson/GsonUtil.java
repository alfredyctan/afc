package org.afc.gson;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static Gson gson() {
		return gsonBuilder().create();
	}

	public static GsonBuilder gsonBuilder() {
		return new GsonBuilder()
			.registerTypeAdapter(Date.class, new GsonDateAdapter())
			.registerTypeAdapter(OffsetDateTime.class, new GsonOffsetDateTimeTypeAdapter())
			.registerTypeAdapter(LocalDate.class, new GsonLocalDateTypeAdapter())
			.registerTypeAdapter(LocalDateTime.class, new GsonLocalDateTimeTypeAdapter())
			.registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTimeTypeAdapter());
	}
}
