package org.afc.xml;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

import org.afc.util.FileUtil;

public class XSDResourceResolver implements LSResourceResolver {

	private static final Logger logger = LoggerFactory.getLogger(XSDResourceResolver.class);
	
	private static final String UTF_8 = "UTF-8";

	private String[] basePaths;

	public XSDResourceResolver(String[] basePaths) {
		this.basePaths = basePaths;
	}

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		logger.info("lookup schema from below location:{}", Arrays.toString(basePaths));
		Reader reader = Arrays.stream(basePaths)
			.map(basePath -> new InputStreamReader(FileUtil.resolveResourceAsStream(basePath + systemId), Charset.forName(UTF_8)))
			.filter(r -> r != null)
			.findFirst()
			.orElse(null);

		return (reader != null) ? new XSDInput(publicId, systemId, baseURI, null, reader, null, UTF_8, true) : null;
	}

}
