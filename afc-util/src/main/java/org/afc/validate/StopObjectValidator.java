package org.afc.validate;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class StopObjectValidator<O> implements ObjectValidator<O> {

	private String message;
	
	@Override
	public boolean validate(O inbound) throws ValidationException {
		//throw exception to stop the hold validation flow
		throw new ValidationException(message);
	}

}
