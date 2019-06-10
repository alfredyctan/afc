package org.afc.querydsl;

import java.util.Map;

import org.afc.jpa.EnumAttributeConverter;
import org.afc.util.EnumUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductType {

	ELN("ELN", "Equity Linked Note"),
	FCN("FCN", "Fixed Coupon Note");

	private String abbr;

	private String desc;

	private static final Map<String, ProductType> ABBREVIATIONS = EnumUtil.mapper(values(), ProductType::getAbbr); 

	private static final Map<String, ProductType> DESCRIPTIONS = EnumUtil.mapper(values(), ProductType::getDesc); 
	
	public static ProductType fromAbbr(String abbr) {
		return EnumUtil.from(ABBREVIATIONS, abbr);
	}

	public static ProductType fromDesc(String desc) {
		return EnumUtil.from(DESCRIPTIONS, desc);
	}
	
	public static class Converter extends EnumAttributeConverter<ProductType, String> {

		@Override
		public String toColumn(ProductType attribute) {
			return attribute.getAbbr();
		}

		@Override
		public ProductType toEnum(String dbData) {
			return fromAbbr(dbData);
		}
	}
}
