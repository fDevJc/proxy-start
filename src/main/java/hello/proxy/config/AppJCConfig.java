package hello.proxy.config;

import hello.proxy.app.v1.*;
import hello.proxy.config.jc_proxy.LogTraceInvocationHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class AppJCConfig {
    @Bean
    OrderControllerV1 orderControllerV1(LogTrace logTrace) {
        OrderControllerV1 orderControllerV1 = new OrderControllerV1Impl(orderServiceV1(logTrace));
        String message = "OrderController.request";
        LogTraceInvocationHandler logTraceInvocationHandler = new LogTraceInvocationHandler(orderControllerV1, logTrace, message);

        OrderControllerV1 proxy = (OrderControllerV1) Proxy.newProxyInstance(
                OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                logTraceInvocationHandler
        );

        return proxy;
    }

    @Bean
    OrderServiceV1 orderServiceV1(LogTrace logTrace) {
        OrderServiceV1 orderServiceV1 = new OrderServiceV1Impl(orderRepositoryV1(logTrace));
        String message = "OrderService.orderItem()";
        LogTraceInvocationHandler handler = new LogTraceInvocationHandler(orderServiceV1, logTrace, message);

        OrderServiceV1 proxy = (OrderServiceV1) Proxy.newProxyInstance(OrderServiceV1.class.getClassLoader(), new Class[]{OrderServiceV1.class}, handler);
        return proxy;
    }

    @Bean
    OrderRepositoryV1 orderRepositoryV1(LogTrace logTrace) {
        OrderRepositoryV1 orderRepositoryV1 = new OrderRepositoryV1Impl();
        String message = "OrderRepository.save()";
        LogTraceInvocationHandler handler = new LogTraceInvocationHandler(orderRepositoryV1, logTrace, message);
        OrderRepositoryV1 proxy = (OrderRepositoryV1) Proxy.newProxyInstance(OrderRepositoryV1.class.getClassLoader(), new Class[]{OrderRepositoryV1.class}, handler);
        return proxy;
    }
}
