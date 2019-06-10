package org.afc.gson;

import java.time.Instant;

import com.google.gson.annotations.JsonAdapter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MockObject {

	@JsonAdapter(GsonInstantTypeAdapter.class)
	private Instant instant = null;
	
}

