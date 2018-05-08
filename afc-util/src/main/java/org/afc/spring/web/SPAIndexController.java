package org.afc.spring.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/* 
 * see https://spring.io/blog/2015/05/13/modularizing-the-client-angular-js-and-spring-security-part-vii 
 */
@Controller
public class SPAIndexController {

	@RequestMapping(value = "/{[path:[^\\.]*}")
	public String forward() {
		return "forward:/";
	}
}
