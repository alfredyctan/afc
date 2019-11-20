package afc.org.maven.plugin.util;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class GsonMerger {

	private static final Logger logger = LoggerFactory.getLogger(GsonMerger.class);
	
	private static boolean overwrite = true;
	
	public static void merge(String target, String... sources) {
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonObject targetElement = new JsonObject();
			for (String source : sources) {
				JsonElement sourceElement = gson.fromJson(new JsonReader(new FileReader(source)), JsonElement.class);
				logger.info("merging {} to {}", source, target);
				merge(sourceElement, targetElement);
			}
			write(target, gson.toJson(targetElement));
		} catch (IOException e) {
			logger.info("error {}", e.getMessage());
		}
	}

	private static void write(String output, String json) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(output))) {
			writer.write(json);
		}
	}

	private static void merge(JsonElement source1, JsonElement source2) {
		if (source2.isJsonArray()) {
			JsonArray targetArray = (JsonArray) source2;
			JsonArray sourceArray = (JsonArray) source1;
			targetArray.addAll(sourceArray);
		} else if (source2.isJsonObject()) {
			JsonObject targetObject = (JsonObject) source2;
			JsonObject sourceObject = (JsonObject) source1;
			sourceObject.entrySet().forEach(sourceEntry -> {
				JsonElement targetValue = targetObject.get(sourceEntry.getKey());
				if (targetValue == null) {
					targetObject.add(sourceEntry.getKey(), sourceEntry.getValue());
				} else if (overwrite) {
					if (targetValue.isJsonPrimitive()) {
						targetObject.add(sourceEntry.getKey(), sourceEntry.getValue());
					} else {
						merge(sourceEntry.getValue(), targetValue);
					}
				} else if (targetValue.isJsonArray()) {
					merge(sourceEntry.getValue(), targetValue);
				}
			});
		}
	}
}
