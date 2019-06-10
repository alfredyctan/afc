package org.afc.jpa;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.AttributeConverter;

public abstract class BigDecimalAttributeConverter implements AttributeConverter<BigDecimal, BigDecimal> {

	@Override
	public BigDecimal convertToDatabaseColumn(BigDecimal attribute) {
		return (attribute == null) ? null : attribute;
	}

	@Override
	public BigDecimal convertToEntityAttribute(BigDecimal dbData) {
		return (dbData == null) ? null : dbData.setScale(getScale(), RoundingMode.HALF_UP);
	}

	protected abstract int getScale();
	
	public static class Scale2 extends BigDecimalAttributeConverter {

		protected int getScale() {
			return 2;
		}
	}

	public static class Scale4 extends BigDecimalAttributeConverter {

		protected int getScale() {
			return 4;
		}
	}

	public static class Scale6 extends BigDecimalAttributeConverter {

		protected int getScale() {
			return 6;
		}
	}
}
