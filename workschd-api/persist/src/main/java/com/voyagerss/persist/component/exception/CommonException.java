package com.voyagerss.persist.component.exception;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public class CommonException extends RuntimeException {

    Gson gson ;

    private static final long serialVersionUID = 1L;

    private CommonExceptionType type = null;


    public CommonException(CommonExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public CommonException(CommonExceptionType type, String message) {
        super(type.getMessage() + message);
        this.type = type;
    }

    public CommonException(CommonExceptionType type, String[] params) {
        super(CommonExceptionUtils.buildMessage(String.valueOf(type), params));
        this.type = type;
    }

    public CommonException(CommonExceptionType type, Exception e) {
        super(type.getCode() + type.getMessage() + " / " + e.getMessage() + " / " +
                Arrays.stream(e.getStackTrace()).findFirst().get().getClassName() +  " " +
                Arrays.stream(e.getStackTrace()).findFirst().get().getLineNumber()
        );

        this.type = type;
    }



}
