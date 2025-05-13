# Truffula Notes
As part of Wave 0, please fill out notes for each of the below files. They are in the order I recommend you go through them. A few bullet points for each file is enough. You don't need to have a perfect understanding of everything, but you should work to gain an idea of how the project is structured and what you'll need to implement. Note that there are programming techniques used here that we have not covered in class! You will need to do some light research around things like enums and and `java.io.File`.

PLEASE MAKE FREQUENT COMMITS AS YOU FILL OUT THIS FILE.

## App.java

~ The interface for Truffula, this class is for output of the Truffla program from the root directory. Should have option to include hidden files, or colored output

~ It processes some command-line arguments to load flags to decide on the handling of hidden files and for colored output, constructs the TruffulaOptions object, and uses it in conjunction with the TruffulaPrinter to output the directory tree.

## ConsoleColor.java

This is to set the color of the console output, when selected from App.java.

## ColorPrinter.java / ColorPrinterTest.java

ColorPrinter is from applying color to the console output with called by App.java

Objects:
- currentColor (has a getter/setter method)
- printStream

Methods <- All method uses overload
- print/ln 
- ColorPrinter

## TruffulaOptions.java / TruffulaOptionsTest.java



## TruffulaPrinter.java / TruffulaPrinterTest.java
It's able to print the directory tree in optional color and to sort files in case-insensitive order, and also supports hiding hidden files. It outputs using ColorPrinter, emitting colors as it scans in order to clarify visually.



## AlphabeticalFileSorter.java