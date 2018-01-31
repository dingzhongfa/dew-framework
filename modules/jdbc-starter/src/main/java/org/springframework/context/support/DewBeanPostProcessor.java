package org.springframework.context.support;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * desription:
 * Created by ding on 2018/1/30.
 */
public class DewBeanPostProcessor implements BeanPostProcessor {



    public DewBeanPostProcessor() {
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.contains("exampleDao")){
            System.out.println("we can get it");
        }
        try(FileWriter fileWriter = new FileWriter(this.getClass().getResource("/").getPath() + "bean.text", true)) {
            fileWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + beanName + "    init\r\n");
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + beanName + "    init");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        try(FileWriter fileWriter = new FileWriter(this.getClass().getResource("/").getPath() + "bean.text", true);) {
            fileWriter.write(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + beanName + "    init success\r\n");
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + beanName + "    init success\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bean;
    }
}
