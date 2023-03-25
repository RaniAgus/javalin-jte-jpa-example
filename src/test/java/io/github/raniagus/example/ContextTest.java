package io.github.raniagus.example;

import io.github.flbulgarelli.jpa.extras.test.SimplePersistenceTest;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ContextTest implements SimplePersistenceTest {

  @Test
  void contextUp() {
    assertThat(entityManager()).isNotNull();
  }

  @Test
  void contextUpWithTransaction() {
    assertDoesNotThrow(() -> withTransaction(() -> {}));
  }

  @Test
  void userShouldBePersisted() {
    assertDoesNotThrow(() -> withTransaction(this::persistUser));
  }

  @Test
  void userSouldBeRetreived() {
    withTransaction(this::persistUser);
    withTransaction(() -> {
      var user = entityManager().find(User.class, 1L);
      assertThat(user).isNotNull();
      assertThat(user.getFirstName()).isEqualTo("Agustin");
      assertThat(user.getLastName()).isEqualTo("Ranieri");
      assertThat(user.getEmail()).isEqualTo("raniagus@github.com");
      assertThat(user.hasPassword("password")).isTrue();
      assertThat(user.getRole()).isEqualTo(Role.USER);
    });
  }

  private void persistUser() {
    var user = new User("Agustin", "Ranieri", "raniagus@github.com", "password", Role.USER);
    entityManager().persist(user);
  }
}
