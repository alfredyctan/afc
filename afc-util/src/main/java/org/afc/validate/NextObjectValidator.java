package org.afc.validate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NextObjectValidator<O> implements ObjectValidator<O> {

	@Override
	public boolean validate(O inbound) throws ValidationException {
		//return false to let next validator to afc to check
		return false;
	}

}
