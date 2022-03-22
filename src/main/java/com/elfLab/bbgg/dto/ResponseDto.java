package com.elfLab.bbgg.dto;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ResponseDto {

	private int code;
	private Object data;
	private String messageType = "success";
	private String message;


	public static <T>ResponseDto sendSuccess(T data) {
		ResponseDto result = new ResponseDto();
		result.setCode(HttpStatus.OK.value());
		result.setData(data);

		return result;
	}


	public static ResponseDto sendError(String message) {
		ResponseDto result = new ResponseDto();
		result.setCode(HttpStatus.OK.value());
		result.setMessageType("failure");
		result.setMessage(message);

		return result;
	}


	public static <T>ResponseDto sendData(T data) {
		ResponseDto result = new ResponseDto();
		result.setCode(HttpStatus.OK.value());
		result.setData(data);

		return result;
	}


	public static ResponseDto sendData(Map<String, ?> data) {
		ResponseDto result = new ResponseDto();
		result.setCode(HttpStatus.OK.value());
		result.setData(data);

		if (data.get("messageType") != null) {
			result.setMessageType(String.valueOf(data.get("messageType")));
		}
		if (data.get("message") != null) {
			result.setMessage(String.valueOf(data.get("message")));
		}

		return result;
	}
}
