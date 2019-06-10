package org.afc.jackson;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockObject {

	private String event = null;

	@JsonRawValue
	@JsonDeserialize(using = JacksonRawValueDeserializer.class)
	private String context = null;

	private String message = null;

	private String organization = null;

	private String audience = null;

	@JsonDeserialize(using = JacksonInstantDeserializer.class)
	@JsonSerialize(using = JacksonInstantSerializer.class)
	private Instant instant = null;
	
}

