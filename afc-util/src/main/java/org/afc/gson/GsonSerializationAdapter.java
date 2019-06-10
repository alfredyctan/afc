package org.afc.gson;

import java.lang.reflect.Type;
import java.util.function.Function;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonSerializationAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {

	private Function<T, JsonElement> serializer;
	
	private Function<JsonElement, T> deserializer;
	
	public GsonSerializationAdapter(Function<T, JsonElement> serializer, Function<JsonElement, T> deserializer) {
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	@Override
	public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
		return deserializer.apply(json);
	}
	
	@Override
	public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
		return serializer.apply(src); 
	}
}
