package ga.overfullstack.dud;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ga.overfullstack.loki.dud.Dud;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DudTest {

  @Test
  @DisplayName("Generate and get with same dudKey and fieldKey")
  void getWithSameDudKeyFieldKey() {
    final var generate = Dud.getOrGenerateValueIfAbsent(null, "dudKey", "fieldKey", String.class);
    final var get1 = Dud.getOrGenerateValueIfAbsent(null, "dudKey", "fieldKey", String.class);
    final var get2 = Dud.getOrGenerateValueIfAbsent("dudKey", "fieldKey");
    assertThat(generate).isInstanceOf(String.class).isEqualTo(get1).isEqualTo(get2);
  }

  @Test
  @DisplayName("Generate and get with same dudKey and different fieldKey")
  void getWithSameDudKeyDifferentFieldKey() {
    final var generate1 = Dud.getOrGenerateValueIfAbsent(null, "dudKey", "fieldKey1", String.class);
    final var generate2 = Dud.getOrGenerateValueIfAbsent(null, "dudKey", "fieldKey2", String.class);
    assertThat(generate1).isInstanceOf(String.class);
    assertThat(generate2).isInstanceOf(String.class);
    assertThat(generate1).isNotEqualTo(generate2);
  }

  @Test
  @DisplayName("Throw when Generate and get with same dudKey and fieldKey is called with different value Type")
  void getThrowsWhenGetWithDifferentValueType() {
    Dud.getOrGenerateValueIfAbsent(null, "dudKey", "fieldKey1", String.class);
    assertThatThrownBy(() -> {
      Dud.getOrGenerateValueIfAbsent(null, "dudKey", "fieldKey1", Integer.class);
    }).isInstanceOf(IllegalArgumentException.class);
  }
}
