package org.afc.jpa;

import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ZonedDateTimeAttributeConverter implements AttributeConverter<ZonedDateTime, Timestamp> {

    @Override
    public Timestamp convertToDatabaseColumn(ZonedDateTime zoneDateTime) {
    	return Timestamp.from(zoneDateTime.toInstant());
    }


    @Override
    public ZonedDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
    	return sqlTimestamp.toInstant().atOffset(ZoneOffset.UTC).toZonedDateTime();
    }
}