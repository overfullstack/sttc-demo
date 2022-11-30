package ga.overfullstack.loki.adapter;

import static java.lang.StackWalker.Option.RETAIN_CLASS_REFERENCE;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface LoggerSupplier {
  StackWalker STACK_WALKER = StackWalker.getInstance(RETAIN_CLASS_REFERENCE);

  @SuppressWarnings("rawtypes")
  default Logger supply() {
    return LoggerFactory.getLogger(STACK_WALKER.getCallerClass());
  }
}
