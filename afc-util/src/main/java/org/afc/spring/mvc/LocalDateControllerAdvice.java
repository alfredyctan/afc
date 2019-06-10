package org.afc.spring.mvc;

import java.beans.PropertyEditorSupport;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class LocalDateControllerAdvice {

	@InitBinder
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				setValue(LocalDate.parse(text, DateTimeFormatter.ISO_DATE));
			}
		});
	}
}