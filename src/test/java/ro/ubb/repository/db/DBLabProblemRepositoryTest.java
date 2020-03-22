package ro.ubb.repository.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystems;

import static org.junit.jupiter.api.Assertions.*;

class DBLabProblemRepositoryTest {
  private DBLabProblemRepository labProblemRepository;
  @BeforeEach
  void setUp() {
    labProblemRepository =
            new DBLabProblemRepository(
                    "configuration" + FileSystems.getDefault().getSeparator() + "db-credentials.data");

  }

  @AfterEach
  void tearDown() {}

  @Test
  void findAll() {}

  @Test
  void findOne() {}

  @Test
  void testFindAll() {}

  @Test
  void save() {}

  @Test
  void delete() {}

  @Test
  void update() {}
}