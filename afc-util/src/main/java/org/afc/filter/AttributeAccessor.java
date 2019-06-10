package org.afc.filter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

public interface AttributeAccessor<T> {

	public String getString(T attributes, String name);

	public BigDecimal getNumeric(T attributes, String name);

	public Date getDate(T attributes, String name);

	public LocalDate getLocalDate(T attributes, String name);

	public LocalTime getLocalTime(T attributes, String name);

	public LocalDateTime getLocalDateTime(T attributes, String name);

	public OffsetDateTime getOffsetDateTime(T attributes, String name);

	public ZonedDateTime getZonedDateTime(T attributes, String name);
			
}
