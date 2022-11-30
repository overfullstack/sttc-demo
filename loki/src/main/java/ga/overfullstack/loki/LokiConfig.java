package ga.overfullstack.loki;

import static ga.overfullstack.loki.BeanName.ENTITY_ACCESSOR_LOKI;
import static ga.overfullstack.loki.BeanName.LOGGER_SUPPLIER_LOKI;

import ga.overfullstack.loki.adapter.EntityAccessor;
import ga.overfullstack.loki.adapter.LoggerSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LokiConfig {

  @Bean(LOGGER_SUPPLIER_LOKI)
  public LoggerSupplier loggerSupplier() {
    return new LoggerSupplier() {};
  }

  @Bean(ENTITY_ACCESSOR_LOKI)
  public EntityAccessor entityAccessor() {
    return new EntityAccessor() {};
  }
}
