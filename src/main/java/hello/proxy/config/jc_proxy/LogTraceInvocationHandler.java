package hello.proxy.config.jc_proxy;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceInvocationHandler implements InvocationHandler {
    private final Object target;
    private final LogTrace logTrace;
    private final String message;

    public LogTraceInvocationHandler(Object target, LogTrace logTrace, String message) {
        this.target = target;
        this.logTrace = logTrace;
        this.message = message;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TraceStatus status = null;
        try {
            status = logTrace.begin(message);

            Object result = method.invoke(target, args);

            logTrace.end(status);
            return result;
        } catch (Exception e) {
            logTrace.exception(status, e);
            throw e;
        }

    }
}
