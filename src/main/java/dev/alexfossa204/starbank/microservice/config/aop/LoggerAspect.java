package dev.alexfossa204.starbank.microservice.config.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Optional;

@Component
@Aspect
@Slf4j
public class LoggerAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void springAnnotatedBeanPointcut() {
    }

    @Pointcut("within(by.andersen.abank.microservice.controller..*)")
    public void applicationPackagePointcut() {
    }

    @AfterThrowing(pointcut = "springAnnotatedBeanPointcut() && applicationPackagePointcut()", throwing = "throwable")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable throwable) {
        log.error("[Exception -- {}]: during method execution {}.{}() with cause = {}", throwable.getClass().getName() , joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), throwable.getMessage() != null ? throwable.getMessage() : "NULL");
    }

    @Around("springAnnotatedBeanPointcut() && applicationPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Optional<Object> result = Optional.empty();
        final StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            result = Optional.of(joinPoint.proceed());
            stopWatch.stop();
            if (log.isDebugEnabled()) {
                log.debug("[Called method -- {}()]: (timeExec = {} ms) of class {} with the result = {}", joinPoint.getSignature().getName(), stopWatch.getTotalTimeMillis(), joinPoint.getSignature().getDeclaringTypeName(), result);
            }
        } catch (Throwable e) {
            throw e;
        }
        return result.get();
    }

}
