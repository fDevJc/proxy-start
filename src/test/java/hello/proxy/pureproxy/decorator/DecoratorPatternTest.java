package hello.proxy.pureproxy.decorator;

import hello.proxy.pureproxy.decorator.code.*;
import org.junit.jupiter.api.Test;

public class DecoratorPatternTest {
    @Test
    void noDecoratorTest() {
        Component component = new RealComponent();
        DecoratorPatternClient client = new DecoratorPatternClient(component);
        client.execute();
    }

    @Test
    void decoratorTest() {
        Component realComponent = new RealComponent();
        Component component = new MessageDecorator(realComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(component);
        client.execute();
    }

    @Test
    void decoratorChainTest() {
        Component realComponent = new RealComponent();
        Component timeComponent = new TimeDecorator(realComponent);
        Component component = new MessageDecorator(timeComponent);
        DecoratorPatternClient client = new DecoratorPatternClient(component);
        client.execute();
    }
}
