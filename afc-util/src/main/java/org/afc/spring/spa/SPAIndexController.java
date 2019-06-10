package org.afc.spring.spa;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SPAIndexController {

	@RequestMapping(value = "/**/{[path:[^\\.]*}")
	public String forward() {
		return "forward:/";
	}
}
