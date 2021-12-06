package datagenerator.transaction;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class Out {
    public Out(String path) {
        try {
            OutputStream os = new FileOutputStream(path, true);
            PrintStream print = new PrintStream(os);
            System.setOut(print);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
