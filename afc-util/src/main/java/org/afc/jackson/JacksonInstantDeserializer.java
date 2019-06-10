package org.afc.jackson;

import java.io.IOException;
import java.time.Instant;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JacksonInstantDeserializer extends JsonDeserializer<Instant> {

	@Override
	public Instant deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		return Instant.parse(parser.getCodec().readValue(parser, String.class));
	}
}