package com.voyagerss.api.component.exceptionhandler;

import com.voyagerss.persist.component.exception.CommonException;
import com.voyagerss.persist.component.exception.CommonExceptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse {

    private String code;
    private String message;

    public ExceptionResponse(CommonExceptionType type) {
        this.code = type.getCode();
        this.message = type.getMessage();
    }

    public ExceptionResponse(CommonExceptionType type, Exception e) {
        this.code = type.getCode();
        this.message = type.getMessage() + ", " + e.getMessage();
    }

    public ExceptionResponse(CommonException e) {
        this.code = e.getType().getCode();
        this.message = e.getMessage();
    }

    public ExceptionResponse(CommonExceptionType type, String message) {
        this.code = type.getCode();
        this.message = type.getMessage()+ ", " + message;
    }
}
