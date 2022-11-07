package ga.overfullstack.loki;

import org.slf4j.Logger;

@FunctionalInterface
public interface LoggerSupplier {
  @SuppressWarnings("rawtypes")
  Logger supply(Class clazz);
}
