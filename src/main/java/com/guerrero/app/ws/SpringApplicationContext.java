package com.guerrero.app.ws;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * clase que permite con el metodo getBean
 * recibir un nombre y retornar un bean ya creado por el framework
 * para ser utilizado en clasese con no sean inyectables
 * por ejemplo los servicios inyectables los cuales son accesibles
 * desde cualquier parte de la aplicación si se tiene acceso al applicationContext
 * se recibe el nombre de la implementación no de la interfaz en
 * el string del metodo de entrada
 * cuando el bean es cargado por el framework la primera letra de la clase
 * se pone en minuscula
 *
 * esta clase debe estar disponible como bean al inicio de la aplicación
 * en la clase main
 */

public class SpringApplicationContext implements ApplicationContextAware {

  private static ApplicationContext CONTEXT;
  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    CONTEXT = applicationContext;
  }


  public static Object getBean (String beanName) {

    return CONTEXT.getBean(beanName);
  }
}
