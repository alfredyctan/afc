package org.afc.validate;

import java.util.LinkedList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InlineObjectValidator<O> implements ObjectValidator<O> {

	protected List<ObjectValidator<O>> validators = new LinkedList<>();
	
	@Override
	public boolean validate(O inbound) throws ValidationException {
		for (ObjectValidator<O> validator : validators) {
			if (!validator.validate(inbound)) {
				return false;
			}
		}
		return true;
	}

	public InlineObjectValidator<O> addValidator(ObjectValidator<O> objectValidator) {
		validators.add(objectValidator);
		return this;
	}
}
