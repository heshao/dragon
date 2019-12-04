package com.ltsw.dragon.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ltsw.dragon.base.entity.AccessLog;
import com.ltsw.dragon.base.service.AccessLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
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

    private ThreadLocal<Long> longThreadLocal;
    private ThreadLocal<AccessLog> logThreadLocal;

    @Pointcut("execution(* com.ltsw.dragon..controller.*.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before(JoinPoint point) {
        longThreadLocal = new ThreadLocal<>();
        longThreadLocal.set(System.currentTimeMillis());

        log.debug("before advise");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        logThreadLocal = new ThreadLocal<>();

        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();

        String httpMethod = request.getMethod();
        String uri = request.getRequestURI();
        String params = objectMapper.writeValueAsString(request.getParameterMap());
        String method = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        String ip = request.getRemoteAddr();

        AccessLog accessLog = new AccessLog(httpMethod, uri, params, method, ip, new Date());
        logThreadLocal.set(accessLog);
        log.info("请求地址：{}", uri);
        log.info("请求参数：{}", params);
        log.debug("around advise");
        return point.proceed(point.getArgs());
    }

    @After("pointcut()")
    public void after(JoinPoint point) {
        long start = longThreadLocal.get();
        AccessLog accessLog = logThreadLocal.get();
        accessLog.setTake(System.currentTimeMillis() - start);
        longThreadLocal.remove();

        log.debug("after advise");
    }

    @AfterReturning(value = "pointcut()", returning = "o")
    public void afterReturning(JoinPoint point, Object o) {

        AccessLog accessLog = logThreadLocal.get();
        logThreadLocal.remove();
        accessLogService.save(accessLog);
        log.info("返回结果:{}", o);
    }

    @AfterThrowing(value = "pointcut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Throwable e) {

        AccessLog accessLog = logThreadLocal.get();
        logThreadLocal.remove();
        accessLog.setException(e.toString());
        accessLogService.save(accessLog);
        log.info("发生异常:{}", e.toString());
    }
}
