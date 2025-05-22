import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-nc", "-h", directoryPath};

    // Act: Create TruffulaOptions instance
    TruffulaOptions options = new TruffulaOptions(args);

    // Assert: Check that the root directory is set correctly
    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testFakePath(@TempDir File tempDir) throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    String[] args = {"-nc", "-h", "some/nonexistent/path"};


    FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));

    assertEquals("The folder doesn't exist: " + "some/nonexistent/path", exception.getMessage());
  }

  @Test
  void testMissingFolder() throws FileNotFoundException {
    // Arrange: Prepare the arguments with the temp directory
    String[] args = {};


    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));

    assertEquals("Missing folder path.", exception.getMessage());
  }

  @Test
  void testWithNoFlags_UsesDefaults(@TempDir File tempDir) throws FileNotFoundException {
    // Test: no flags ... hide hidden files, use color
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }
  @Test
  void testWithHiddenFlagOnly(@TempDir File tempDir) throws FileNotFoundException {
    // Test: -h only ... show hidden files, color stays enabled
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-h", directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testWithNoColorFlagOnly(@TempDir File tempDir) throws FileNotFoundException {
    // Test: -nc only .... hide hidden files, disable color
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-nc", directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);
   assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertFalse(options.isUseColor());
  }
}
 