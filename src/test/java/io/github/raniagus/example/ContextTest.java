package io.github.raniagus.example;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import com.github.flbulgarelli.jpa.extras.test.PersistenceTest;
import io.github.raniagus.example.model.Role;
import io.github.raniagus.example.model.User;
import org.junit.jupiter.api.Test;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ContextTest implements PersistenceTest, WithSimplePersistenceUnit {

  @Test
  public void contextUp() {
    assertThat(entityManager()).isNotNull();
  }

  @Test
  public void contextUpWithTransaction() {
    assertDoesNotThrow(() -> withTransaction(() -> {}));
  }

  @Test
  public void userShouldBePersisted() {
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
      assertThat(user.getPassword()).isEqualTo(sha256Hex("password"));
      assertThat(user.getRole()).isEqualTo(Role.USER);
    });
  }

  @Test
  private void persistUser() {
    var user = new User("Agustin", "Ranieri", "raniagus@github.com", "password", Role.USER);
    entityManager().persist(user);
  }
}
