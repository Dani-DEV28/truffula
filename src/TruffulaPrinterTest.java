import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    /**
     * Checks if the current operating system is Windows.
     *
     * This method reads the "os.name" system property and checks whether it
     * contains the substring "win", which indicates a Windows-based OS.
     * 
     * You do not need to modify this method.
     *
     * @return true if the OS is Windows, false otherwise
     */
    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    /**
     * Creates a hidden file in the specified parent folder.
     * 
     * The filename MUST start with a dot (.).
     *
     * On Unix-like systems, files prefixed with a dot (.) are treated as hidden.
     * On Windows, this method also sets the DOS "hidden" file attribute.
     * 
     * You do not need to modify this method, but you SHOULD use it when creating hidden files
     * for your tests. This will make sure that your tests work on both Windows and UNIX-like systems.
     *
     * @param parentFolder the directory in which to create the hidden file
     * @param filename the name of the hidden file; must start with a dot (.)
     * @return a File object representing the created hidden file
     * @throws IOException if an I/O error occurs during file creation or attribute setting
     * @throws IllegalArgumentException if the filename does not start with a dot (.)
     */
    private static File createHiddenFile(File parentFolder, String filename) throws IOException {
        if(!filename.startsWith(".")) {
            throw new IllegalArgumentException("Hidden files/folders must start with a '.'");
        }
        File hidden = new File(parentFolder, filename);
        hidden.createNewFile();
        if(isWindows()) {
            Path path = Paths.get(hidden.toURI());
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
        return hidden;
    }

    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create a hidden file in myFolder
        createHiddenFile(myFolder, ".hidden.txt");

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        ConsoleColor reset = ConsoleColor.RESET;
        ConsoleColor white = ConsoleColor.WHITE;
        ConsoleColor purple = ConsoleColor.PURPLE;
        ConsoleColor yellow = ConsoleColor.YELLOW;

        StringBuilder expected = new StringBuilder();
        expected.append(white).append(white).append("myFolder/").append(nl).append(reset);
        expected.append(white).append(purple).append("   Apple.txt").append(reset).append(nl).append(reset);
        expected.append(white).append(purple).append("   banana.txt").append(reset).append(nl).append(reset);
        expected.append(white).append(purple).append("   Documents/").append(reset).append(nl).append(reset);
        expected.append(white).append(yellow).append("      images/").append(reset).append(nl).append(reset);
        expected.append(white).append(white).append("         cat.png").append(reset).append(nl).append(reset);
        expected.append(white).append(white).append("         Dog.png").append(reset).append(nl).append(reset);
        expected.append(white).append(yellow).append("      notes.txt").append(reset).append(nl).append(reset);
        expected.append(white).append(yellow).append("      README.md").append(reset).append(nl).append(reset);
        expected.append(white).append(purple).append("   zebra.txt").append(reset).append(nl).append(reset);
        // expected.append(nl);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }
    @Test
    public void testPrintTree_ExactOutput_WithoutColor(@TempDir File tempDir) throws IOException {
        // Create directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        new File(myFolder, "Apple.txt").createNewFile();
        new File(myFolder, "banana.txt").createNewFile();
        new File(myFolder, "zebra.txt").createNewFile();
        createHiddenFile(myFolder, ".hidden.txt");

        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");
        new File(documents, "README.md").createNewFile();
        new File(documents, "notes.txt").createNewFile();

        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");
        new File(images, "cat.png").createNewFile();
        new File(images, "Dog.png").createNewFile();

        // Use options with showHidden = false and useColor = false
        TruffulaOptions options = new TruffulaOptions(myFolder, false, false);

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(output);

        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
        printer.printTree();

        // String  = baos.toString();
        String nl = System.lineSeparator();

        StringBuilder expected = new StringBuilder();
        expected.append(ConsoleColor.WHITE).append(ConsoleColor.WHITE).append("myFolder/").append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   Apple.txt").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   banana.txt").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   Documents/").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("      images/").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("         cat.png").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("         Dog.png").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("      notes.txt").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("      README.md").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   zebra.txt").append(ConsoleColor.RESET).append(nl);
        expected.append(ConsoleColor.RESET);

        assertEquals(expected.toString(), output.toString());
    }

    @Test
public void testPrintTree_ShowsHiddenFiles_WhenEnabled(@TempDir File tempDir) throws IOException {
    // myFolder/
    //    .hidden.txt
    //    Apple.txt

    File myFolder = new File(tempDir, "myFolder");
    assertTrue(myFolder.mkdir(), "Folder should be created");

    File apple = new File(myFolder, "Apple.txt");
    apple.createNewFile();

    createHiddenFile(myFolder, ".hidden.txt");


   // Verifies that hidden files are printed when showHidden = true
    TruffulaOptions options = new TruffulaOptions(myFolder, true, false);

    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(output);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String nl = System.lineSeparator();
    StringBuilder expected = new StringBuilder();
    expected.append(ConsoleColor.WHITE).append(ConsoleColor.WHITE).append("myFolder/").append(nl);
    expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   .hidden.txt").append(ConsoleColor.RESET).append(nl);
    expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   Apple.txt").append(ConsoleColor.RESET).append(nl);
    expected.append(ConsoleColor.RESET);

    assertEquals(expected.toString(), output.toString());
}


@Test
public void testPrintTree_NestedDirectory(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "root");
    assertTrue(root.mkdir());

    File sub = new File(root, "subdir");
    assertTrue(sub.mkdir());

    new File(sub, "file.txt").createNewFile();
// Test nested directory structure
    TruffulaOptions options = new TruffulaOptions(root, false, false);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(output);

    TruffulaPrinter printer = new TruffulaPrinter(options, printStream);
    printer.printTree();

    String nl = System.lineSeparator();
    StringBuilder expected = new StringBuilder();
    expected.append(ConsoleColor.WHITE).append(ConsoleColor.WHITE).append("root/").append(nl);
    expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   subdir/").append(ConsoleColor.RESET).append(nl);
    expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("      file.txt").append(ConsoleColor.RESET).append(nl);
    expected.append(ConsoleColor.RESET);

    assertEquals(expected.toString(), output.toString());
}


@Test
public void testPrintTree_EmptyFolder(@TempDir File tempDir) {
    //  empty folder..... should only print root folder name
    File empty = new File(tempDir, "emptyFolder");
    assertTrue(empty.mkdir());

    TruffulaOptions options = new TruffulaOptions(empty, false, false);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    TruffulaPrinter printer = new TruffulaPrinter(options, new PrintStream(output));

    printer.printTree();

    String nl = System.lineSeparator();
    StringBuilder expected = new StringBuilder();
    expected.append(ConsoleColor.WHITE).append(ConsoleColor.WHITE).append("emptyFolder/").append(nl);
    expected.append(ConsoleColor.RESET);

    assertEquals(expected.toString(), output.toString());
}

@Test
public void testPrintTree_HiddenFolderSkipped_WhenHiddenFalse(@TempDir File tempDir) throws IOException {
    // hidden folder is skipped when showHidden = false
    File parent = new File(tempDir, "parent");
    assertTrue(parent.mkdir());
    createHiddenFile(parent, ".secretFolder");

    TruffulaOptions options = new TruffulaOptions(parent, false, false);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    TruffulaPrinter printer = new TruffulaPrinter(options, new PrintStream(output));
    printer.printTree();

    String nl = System.lineSeparator();
    StringBuilder expected = new StringBuilder();
    expected.append(ConsoleColor.WHITE).append(ConsoleColor.WHITE).append("parent/").append(nl);
    expected.append(ConsoleColor.RESET);

    assertEquals(expected.toString(), output.toString());
}


@Test
public void testPrintTree_FileNamesWithSpacesAndSymbols(@TempDir File tempDir) throws IOException {
    // filenames with spaces and special characters
    File folder = new File(tempDir, "specials");
    assertTrue(folder.mkdir());
    new File(folder, "hello everyone.txt").createNewFile();
    new File(folder, "@doName$.csv").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    TruffulaPrinter printer = new TruffulaPrinter(options, new PrintStream(output));
    printer.printTree();

    String nl = System.lineSeparator();
    StringBuilder expected = new StringBuilder();
    expected.append(ConsoleColor.WHITE).append(ConsoleColor.WHITE).append("specials/").append(nl);
    expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   @doName$.csv").append(ConsoleColor.RESET).append(nl);
    expected.append(ConsoleColor.RESET).append(ConsoleColor.WHITE).append("   hello everyone.txt").append(ConsoleColor.RESET).append(nl);
    expected.append(ConsoleColor.RESET);

    assertEquals(expected.toString(), output.toString());
}





}
