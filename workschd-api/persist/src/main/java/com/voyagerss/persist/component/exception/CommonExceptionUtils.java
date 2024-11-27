package com.voyagerss.persist.component.exception;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.text.MessageFormat;

public abstract class CommonExceptionUtils {
    /**
     * Build a message for the given base message and root cause.
     *
     * @return the full exception message
     */
    public static String buildMessage(CommonExceptionType type, final Object[] msgParams) {

    	//message property 파일 사용 X처리 -> 공통 에러콜백함수로 키값 및 파라미터 전달

    	String pMsgKey = type.getCode();
    	String[] pMsgParams = (String[]) msgParams;

    	//JSON 데이터를 위한 StringBuffer 데이터 생성
    	StringBuffer sb = new StringBuffer();

    	sb.append("{'msgKey': '");
    	sb.append(pMsgKey);
    	sb.append("', 'msgParams':[");

		for(int i=0; i < pMsgParams.length; i++){
			if(i > 0) sb.append(",");
			sb.append("'" + pMsgParams[i] + "'");
    	}
		sb.append("]}");

		//StringBuffer -> JSON 변경
		String jsonString = sb.toString();
		JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();

    	return jsonObject.toString();
    }

    public static String buildMessage(final String msgKey, final Object[] msgParams) {
        String userMessage = msgKey;
        if (msgParams != null) {
            userMessage = MessageFormat.format(msgKey, msgParams);
        }
        return userMessage;
    }

}
