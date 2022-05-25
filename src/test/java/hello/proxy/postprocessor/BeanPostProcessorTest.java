package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class BeanPostProcessorTest {
    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostConfig.class);

        B beanB = applicationContext.getBean("beanA", B.class);
        beanB.helloB();

        assertThatThrownBy(() -> applicationContext.getBean(A.class))
                .isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Slf4j
    @Configuration
    static class BeanPostConfig {
        @Bean(name = "beanA")
        public A a() {
            return new A();
        }

        @Bean
        public AtoBPostProcessor atoBPostProcessor() {
            return new AtoBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("helloA");
        }
    }
    @Slf4j
    static class B {
        public void helloB() {
            log.info("helloB");
        }
    }

    @Slf4j
    static class AtoBPostProcessor implements BeanPostProcessor {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("beanName={}, bean={}", beanName, bean);
            if (bean instanceof A) {
                return new B();
            }
            return bean;
        }
    }
}
