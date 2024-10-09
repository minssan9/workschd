package com.voyagerss.api.component.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@Configuration
public class LogInterceptor implements HandlerInterceptor {
    @Autowired    private Environment environment;
//    @Autowired    private LogUserActionService logUserActionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("===================  Interceptor START       ===================");
        log.debug(" Request URI \t:  " + request.getRequestURI());

        if (saveLog(request)) return true;

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        log.debug("===================  Interceptor End       ===================");
        /***************************************************************************
         * System Version 체크
         ***************************************************************************/
//        String oldVer = request.getHeader("$systemVer");
//        SysConf curVerSysConf = sysConfRepository.findByCode("sysVer").get();

//        if(!oldVer.equals(curVerSysConf.getValue())) {
//            response.addHeader("Access-Control-Expose-Headers", "isUpdate");
//            response.addHeader("isUpdate", "Y");
            // response.sendError(CustomHttpStatus.UPDATED, "updated.");
            // return false;
//        }
    }

    public boolean saveLog(HttpServletRequest request) {
        if(request.getRequestURI().contentEquals("/api/health")) return true;
        if(request.getContentType() != null && request.getContentType().contains("multipart")) return true;

        String accountId = request.getHeader("$accountId") != null ? request.getHeader("$accountId") : "";
//        LogUserActionDTO logUserActionDTO = new LogUserActionDTO();
//        logUserActionDTO.setUrl(request.getRequestURL().toString());
//        logUserActionDTO.setActionType(request.getMethod());
//        logUserActionDTO.setAddIpAddr(request.getRemoteAddr());
//        logUserActionDTO.setAddUser(accountId);
//
//        switch (request.getMethod()){
//            case "GET":
//                String queryString = request.getQueryString() != null ? request.getQueryString() : "";
//                logUserActionDTO.setParam(queryString);
//                break;
//            default:
//                String requestBody = (String) request.getAttribute("requestBody");
//                logUserActionDTO.setParam(requestBody);
//                break;
//        }
//        logUserActionService.save(logUserActionDTO);
        return false;
    }
}
