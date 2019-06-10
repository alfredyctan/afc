package org.afc.jackson;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = JacksonInstantSerializer.class)
public class JacksonInstantSerializer extends JsonSerializer<Instant> {

	@Override
	public void serialize(Instant instant, JsonGenerator generator, SerializerProvider serializer) throws IOException {
		generator.writeString(instant.toString());
	}
}