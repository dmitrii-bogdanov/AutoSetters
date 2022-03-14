package bogdanov;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

class Writer {

    private static final String SET = "set";
    private static final String DOT = ".";
    private static final String COLUMN_START = "(\"";
    private static final String OPENING_BRACKET = "(";
    private static final String GET = "get";
    private static final String END = "\"));\n";

    private static final String INTEGER = "Integer";
    private static final String INT = "Int";

    static void write(String varName, String outputFile, List<Field> fields) throws IOException {

        File file = new File(outputFile);

        try (FileWriter fileWriter = new FileWriter(file)) {
            if (fields.isEmpty()) {
                return;
            }

            for (Field field : fields) {
                fileWriter.write(concat(varName, field));
            }

        }
    }

    private static String concat(String varName, Field field) {
        return varName + DOT + SET
                + upLeading(field.name) + OPENING_BRACKET
                + getGetter(field.type) + COLUMN_START
                + field.column.toUpperCase(Locale.ROOT)
                + END
                ;
    }

    private static String upLeading(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ROOT) + str.substring(1);
    }

    private static String getGetter(String type) {

        return GET + (INTEGER.equals(type) ? INT : type);

    }

}
