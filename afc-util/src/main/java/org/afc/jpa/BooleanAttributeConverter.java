package org.afc.jpa;

import javax.persistence.AttributeConverter;

public class BooleanAttributeConverter implements AttributeConverter<Boolean, String> {

	private static final String Y = "Y";
	
	private static final String N = "N";

	@Override
	public String convertToDatabaseColumn(Boolean attribute) {
		if (attribute == null) {
			return null;
		}
		return (attribute) ? Y : N;
	}

	@Override
	public Boolean convertToEntityAttribute(String dbData) {
		if (dbData == null) {
			return null;
		}
		return (dbData.equals(Y)) ? Boolean.TRUE : Boolean.FALSE;
	}
}
