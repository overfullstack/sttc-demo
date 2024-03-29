package ga.overfullstack.loki.fake.adapter;

import static ga.overfullstack.loki.fake.BeanName.LOGGER_NO_OP_SUPPLIER_LOKI;

import ga.overfullstack.loki.adapter.LoggerSupplier;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({EntityAccessorFake.class})
public class LokiConfigForTest {
  @Bean(LOGGER_NO_OP_SUPPLIER_LOKI)
  public LoggerSupplier loggerSupplier() {
    return new LoggerSupplier() {
      @Override
      public Logger supply() {
        return Mockito.mock(Logger.class);
      }
    };
  }
}
