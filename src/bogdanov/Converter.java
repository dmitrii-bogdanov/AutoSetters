package bogdanov;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static void convert(String inputFile,
                               String varName, String outputSetterFile,
                               String tableName, String outputTableFile) throws IOException {
        List<Field> fields = new ArrayList<>();
        List<Column> columns = new ArrayList<>();
        Reader.read(inputFile, fields, columns);
        Writer.writeSetters(varName, outputSetterFile, fields);
        Writer.writeColumns(tableName, outputTableFile, columns);
    }

}
