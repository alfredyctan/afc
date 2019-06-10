package org.afc.gson;

import java.lang.reflect.Type;
import java.util.Date;

import org.afc.util.DateUtil;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

	@Override
	public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
		if (src == null) {
			return JsonNull.INSTANCE;
		} else {
			return new JsonPrimitive(DateUtil.formatUTCDatetime(src));
		}
	}

	@Override
	public Date deserialize(JsonElement json, Type date, JsonDeserializationContext context) throws JsonParseException {
		String str = json.getAsJsonPrimitive().getAsString();
		try {
			return DateUtil.parseUTCDatetime(str);
		} catch (RuntimeException e) {
			throw new JsonParseException(e);
		}
	}
}
