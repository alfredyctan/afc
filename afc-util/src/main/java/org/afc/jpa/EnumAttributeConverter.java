package org.afc.jpa;

import javax.persistence.AttributeConverter;

public abstract class EnumAttributeConverter<E, D> implements AttributeConverter<E, D> {

	@Override
	public D convertToDatabaseColumn(E attribute) {
		return (attribute == null) ? null : toColumn(attribute);
	}

	@Override
	public E convertToEntityAttribute(D dbData) {
		return (dbData == null) ? null : toEnum(dbData);
	}

	public abstract D toColumn(E attribute);

	public abstract E toEnum(D dbData);
}
