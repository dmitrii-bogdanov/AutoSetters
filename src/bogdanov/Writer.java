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
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final String LINE_END = "\\n";
    private static final String UNKNOWN = "???";
    private static final String AS = "AS";
    private static final String COMMENT = "--";
    private static final String QUOTES = "\"";
    private static final String COMMA = ",";
    private static final String PLUS = "+";
    private static final String RETURN = "\n";

    private static final String INTEGER = "Integer";
    private static final String INT = "Int";

    static void writeSetters(String varName, String outputFile, List<Field> fields) throws IOException {

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

    static void writeColumns(String varName, String outputFile, List<Column> columns) throws IOException {

        File file = new File(outputFile);

        try (FileWriter fileWriter = new FileWriter(file)) {
            if (columns.isEmpty()) {
                return;
            }

            int i = 0;
            while (i < columns.size()-1) {
                fileWriter.write(concat(varName, columns.get(i++)));
            }
            fileWriter.write(concat(varName, columns.get(i), true));

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

    private static String concat(String tableName, Column column) {
        return concat(tableName, column, false);
    }

    private static String concat(String tableName, Column column, boolean isLast) {
        return QUOTES +
                tableName + DOT
                + getColumnName(column) + SPACE
                + AS + getAlias(column) + (isLast ? EMPTY_STRING : COMMA) + SPACE
                + getComment(column)
                + LINE_END + QUOTES + SPACE + PLUS + RETURN
                ;
    }

    private static String upLeading(String str) {
        return upper(str.substring(0, 1)) + str.substring(1);
    }

    private static String getGetter(String type) {

        return GET + (INTEGER.equals(type) ? INT : type);

    }

    private static String getColumnName(Column column){
        return column.name == null ? UNKNOWN : upper(column.name);
    }

    private static String getAlias(Column column){
        return column.alias == null ? getColumnName(column) : upper(column.alias);
    }

    private static String getComment(Column column){
        return column.comment == null ? EMPTY_STRING : COMMENT + column.comment;
    }

    private static String upper(String str) {
        return str.toUpperCase(Locale.ROOT);
    }

}
