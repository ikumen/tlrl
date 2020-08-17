package com.gnoht.tlrl.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author ikumen@gnoht.com
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware {
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    ApplicationContextHelper.applicationContext = applicationContext;
  }

  public static void registerSingleton(String name, Object bean) {
    if (applicationContext == null)
      throw new IllegalStateException("No ApplicationContext available!");
    ((ConfigurableApplicationContext) applicationContext).getBeanFactory()
        .registerSingleton(name, bean);
  }

  public static ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public static <T> T getBean(Class<T> cls) {
    return applicationContext.getBean(cls);
  }

  public static <T> T getBean(String name, Class<T> cls) {
    return applicationContext.getBean(name, cls);
  }
}
