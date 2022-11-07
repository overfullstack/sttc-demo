package ga.overfullstack.loki;

import static ga.overfullstack.loki.BeanName.LOGGER_SUPPLIER_LOKI;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EntityAccessor.class})
public class CoreConfig {

  @Bean
  @Qualifier(LOGGER_SUPPLIER_LOKI)
  public LoggerSupplier loggerSupplier() {
    return LoggerFactory::getLogger;
  }
}
