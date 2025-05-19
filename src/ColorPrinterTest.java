import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPrinterTest {

  @Test
  void testPrintlnWithRedColorAndReset() {
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.println(message);


    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  

  @Test
  void testPrintlnWithRedColorAndResetFalse() {
    //  // Test: println in RED without reset
    // Arrange: Capture the printed output
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    // Act: Print the message
    String message = "I speak for the trees";
    printer.println(message, false);


    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator();

    // Assert: Verify the printed output
    assertEquals(expectedOutput, outputStream.toString());
  }
  @Test
  void testPrintlnWithGreenColorAndReset() {
      // Test: println in GREEN with reset
    // Arrange
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);
  
    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.GREEN);
  
    // Act
    String message = "Protect nature";
    printer.println(message);
  
    // Assert
    String expectedOutput = ConsoleColor.GREEN + message + System.lineSeparator() + ConsoleColor.RESET;
    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
void testPrintlnWithYellowColorAndResetFalse() {
    // Test: println in YELLOW without reset
  // Arrange
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(outputStream);

  ColorPrinter printer = new ColorPrinter(printStream);
  printer.setCurrentColor(ConsoleColor.YELLOW);

  // Act
  String message = "Sunshine is bright";
  printer.println(message, false);

  // Assert
  String expectedOutput = ConsoleColor.YELLOW + message + System.lineSeparator();
  assertEquals(expectedOutput, outputStream.toString());
}
@Test
void testPrintWithBlueColorAndResetTrue() {
    // Test: print in BLUE with reset
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(outputStream);

  ColorPrinter printer = new ColorPrinter(printStream);
  printer.setCurrentColor(ConsoleColor.BLUE);

  String message = "Blue ocean";
  printer.print(message); 

  String expectedOutput = ConsoleColor.BLUE + message + ConsoleColor.RESET;
  assertEquals(expectedOutput, outputStream.toString());
}


@Test
void testPrintWithCyanColorAndResetTrue() {
    // Test: print in CYAN with reset
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(outputStream);

  ColorPrinter printer = new ColorPrinter(printStream);
  printer.setCurrentColor(ConsoleColor.CYAN);

  String message = "Cool as cyan";
  printer.print(message);  

  String expectedOutput = ConsoleColor.CYAN + message + ConsoleColor.RESET;
  assertEquals(expectedOutput, outputStream.toString());
}

@Test
void testPrintlnWithWhiteColorAndNoReset() {
    // Test: println in WHITE without reset
  ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
  PrintStream printStream = new PrintStream(outputStream);

  ColorPrinter printer = new ColorPrinter(printStream);
  printer.setCurrentColor(ConsoleColor.WHITE);

  String message = "Neutral white tone";
  printer.println(message, false);

  String expectedOutput = ConsoleColor.WHITE + message + System.lineSeparator();
  assertEquals(expectedOutput, outputStream.toString());
}

}