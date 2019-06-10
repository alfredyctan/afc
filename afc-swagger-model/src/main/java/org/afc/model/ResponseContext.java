package org.afc.model;

import static org.afc.model.ResponseContext.Code.*;
import static org.afc.util.ExceptionUtil.*;

import java.time.OffsetDateTime;

import org.afc.util.ClockUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@ApiModel(description = "The context of the response to provide additional information of the main responsed content")
@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseContext {

	/* default response code constant */
	public static class Code {

		/* default response code constant - SUCCESS */
		public static final String SUCCESS = "SUCCESS";

		/* default response code constant - FAIL */
		public static final String FAIL = "FAIL";

	}

	@ApiModelProperty(value = "response code, underlying application can supplement additional code for client action mapping")
	private String code;

	@ApiModelProperty(value = "the title of this response, can be used as display title")
	private String title;

	@ApiModelProperty(value = "free text content provided for direct display. eg. UI msgbox")
	private String message;
	
	@ApiModelProperty(value = "free text content to provide further detail for client processing. eg. render display in UI, not for direct display")
	private String context;

	@ApiModelProperty(value = "timestamp of the response generated")
	private OffsetDateTime timestamp;

	public static ResponseContext from(Exception e) {
		return new ResponseContext(FAIL, null, unwrap(e).getMessage(), null, ClockUtil.offsetDateTime());
	}

	public static ResponseContext from(String code, String message) {
		return new ResponseContext(code, null, message, null, ClockUtil.offsetDateTime());
	}
}