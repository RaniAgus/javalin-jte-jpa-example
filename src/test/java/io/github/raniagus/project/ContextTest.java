package io.github.raniagus.project;

import com.github.flbulgarelli.jpa.extras.simple.WithSimplePersistenceUnit;
import com.github.flbulgarelli.jpa.extras.test.PersistenceTest;
import io.github.raniagus.project.model.Role;
import io.github.raniagus.project.model.User;
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
      assertThat(user.getUsername()).isEqualTo("raniagus");
      assertThat(user.getPassword()).isEqualTo(sha256Hex("password"));
      assertThat(user.getRole()).isEqualTo(Role.USER);
    });
  }

  @Test

  private void persistUser() {
    var user = new User("raniagus", "password", Role.USER);
    entityManager().persist(user);
  }
}
