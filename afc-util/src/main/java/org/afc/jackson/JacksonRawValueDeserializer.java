package org.afc.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JacksonRawValueDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser parser, DeserializationContext context) throws IOException, JsonProcessingException {
		return parser.getCodec().readTree(parser).toString();
	}
}