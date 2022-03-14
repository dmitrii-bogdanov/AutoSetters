package bogdanov;

import java.io.IOException;

public class Converter {

    public static void convert(String varName, String inputFile, String outputFile) throws IOException {
        Writer.write(varName, outputFile, Reader.read(inputFile));
    }

}
