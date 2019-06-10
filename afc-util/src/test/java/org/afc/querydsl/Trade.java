package org.afc.querydsl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trade")
public class Trade {

	@Id
	@Column(name = "id")
	private Long id;

	@Convert(converter = ProductType.Converter.class)
	@Column(name = "product")
	private ProductType product;

	@Convert(converter = TradeStatusType.Converter.class)
	@Column(name = "status")
	private TradeStatusType status;

}
