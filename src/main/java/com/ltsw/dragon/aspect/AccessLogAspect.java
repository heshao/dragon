package com.ltsw.dragon.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltsw.dragon.base.entity.AccessLog;
import com.ltsw.dragon.base.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 访问日志切面
 * @author heshaobing
 */
@Slf4j
@Aspect
@Component
public class AccessLogAspect {

    @Autowired
    private AccessLogService accessLogService;
    @Autowired
    private ObjectMapper objectMapper;

    @Pointcut("execution(* com.ltsw.dragon..controller.*.*(..))")
    public void pointcut() {

    }

    @Around("pointcut() ")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String title = "";
        String uri = request.getRequestURI();
        String params = objectMapper.writeValueAsString(request.getParameterMap());
        String ip = request.getRemoteAddr();
        String httpMethod = request.getMethod();
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        Object obj;
        Date now = new Date();
        AccessLog accessLog = new AccessLog(title, httpMethod, uri, params, method, ip, now);

        new AccessLog();
        try {
            obj = point.proceed(point.getArgs());
        } catch (Throwable e) {
            accessLog.setException(e.toString());
            throw e;
        } finally {
            accessLog.setTake(System.currentTimeMillis() - now.getTime());
            accessLogService.save(accessLog);
        }
        return obj;
    }
}
