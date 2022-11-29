package ga.overfullstack.pokemon.now;

import static ga.overfullstack.loki.BeanName.LOGGER_SUPPLIER_LOKI;
import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import ga.overfullstack.legacy.Entity;
import ga.overfullstack.legacy.LoadFromDBException;
import ga.overfullstack.loki.LokiConfig;
import ga.overfullstack.loki.adapter.EntityAccessor;
import ga.overfullstack.loki.adapter.LoggerSupplier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(LokiConfig.class)
class Config {
  public static final String ENTITY_ACCESSOR_EXTENDED = "EntityAccessorExtended";
  public static final StackWalker STACK_WALKER = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

  @Bean(ENTITY_ACCESSOR_EXTENDED)
  EntityAccessor entityAccessorExtended(
      @Qualifier(LOGGER_SUPPLIER_LOKI) LoggerSupplier loggerSupplier) {
    return new EntityAccessor() {
      @Override
      public <T extends Entity> T loadNew(Class<T> type) throws LoadFromDBException {
        loggerSupplier
            .supply(STACK_WALKER.getCallerClass())
            .info("Loading class of type: {}", type);
        return EntityAccessor.super.loadNew(type);
      }
    };
  }
}
