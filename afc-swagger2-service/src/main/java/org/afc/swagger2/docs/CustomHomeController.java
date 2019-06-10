package org.afc.swagger2.docs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;

@Controller
@ApiIgnore
public class CustomHomeController {
	
	@Value("${springfox.documentation.swagger.v2.home}")
	private String home;

	@RequestMapping(value = "${springfox.documentation.swagger.v2.home}")
    public String index() {
        return "redirect:" + home + "/swagger-ui.html";
    }
}
