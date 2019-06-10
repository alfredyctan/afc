package org.afc.json;

public interface JsonSchemaValidator {
	
	/**
	 * 
	 * @param json - json string to validate
	 * @param schemaRef - path to json schema, similar to $ref pointer with protocol support. eg. classpath://json/schema/payoff.json#/Payoff 
	 */
	public void validate(String json, String schemaRef) throws JsonValidationException;
	
}
