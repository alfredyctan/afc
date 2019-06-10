package org.afc.querydsl;

import java.util.Map;

import org.afc.jpa.EnumAttributeConverter;
import org.afc.util.EnumUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TradeStatusType {
	
	PENDING("PD", "Pending"),
	ALLOCATED("AL", "Allocated"),
	EXECUTED("ED", "Executed");
	
	private String abbr;

	private String desc;
	
	private static final Map<String, TradeStatusType> ABBREVIATIONS = EnumUtil.mapper(values(), TradeStatusType::getAbbr); 

	private static final Map<String, TradeStatusType> DESCRIPTIONS = EnumUtil.mapper(values(), TradeStatusType::getDesc); 
	
	public static TradeStatusType fromAbbr(String abbr) {
		return EnumUtil.from(ABBREVIATIONS, abbr);
	}

	public static TradeStatusType fromDesc(String desc) {
		return EnumUtil.from(DESCRIPTIONS, desc);
	}
	
	public static class Converter extends EnumAttributeConverter<TradeStatusType, String> {

		@Override
		public String toColumn(TradeStatusType attribute) {
			return attribute.getAbbr();
		}

		@Override
		public TradeStatusType toEnum(String dbData) {
			return fromAbbr(dbData);
		}
	}
}
