package org.afc.swagger.docs;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import springfox.documentation.annotations.ApiIgnore;
import springfox.documentation.swagger2.web.Swagger2Controller;

@Controller
@ApiIgnore
@RequestMapping(value = "${springfox.documentation.swagger.v2.home}")
public class CustomHomeSwagger2Controller extends Swagger2Controller {

}
