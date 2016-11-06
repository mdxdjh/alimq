package src.main.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author ellis.luo
 * @date 16/11/6 上午11:56
 * @description
 */
public class ConsumerMain
{
    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("src/main/test/resources/test.xml");

    }
}
