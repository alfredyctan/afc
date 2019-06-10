package org.afc.spring.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ResourceNotFoundRedirectController {

	private static final Logger logger = LoggerFactory.getLogger(ResourceNotFoundRedirectController.class);

	@Value("${server.error.404.redirect-url}")
	private String redirectUrl;
	
	@RequestMapping("/redirect-on-resource-not-found")
    public RedirectView redirectOnResourceNotFound() {
		logger.debug("resource not found and redirect to {}", redirectUrl);
		RedirectView view = new RedirectView(redirectUrl);
		view.setHttp10Compatible(false);;
        return view;
    }
}