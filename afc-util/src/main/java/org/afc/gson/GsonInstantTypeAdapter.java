package org.afc.gson;

import java.io.IOException;
import java.time.Instant;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;


public class GsonInstantTypeAdapter extends TypeAdapter<Instant> {

	@Override
	public void write(JsonWriter out, Instant value) throws IOException {
        if (value == null) {
            out.nullValue();
        } else {
            out.value(value.toString());
        }
	}

	@Override
	public Instant read(JsonReader in) throws IOException {
        switch (in.peek()) {
		    case NULL:
		        in.nextNull();
		        return null;
		    default:
		        return Instant.parse(in.nextString());
	    }
	}
}