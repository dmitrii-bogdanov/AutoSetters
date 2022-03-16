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
    private static final String FUNCTION_END = "()";
    private static final String TITLE = "TITLE:";
    private static final String DETAIL = "DETAIL:";
    private static final String X_POS = "%X_POS%";
    private static final String UUID = "%UUID%";
    private static final String JASPER_TEXT_FIELD_START =
            "<textField>\n" +
                    "   <reportElement style=\"ReportCell\" positionType=\"Float\" x=\"%X_POS%\" " +
                    "y=\"0\" width=\"70\" height=\"15\" uuid=\"fbd09389-c387-4d5e-8cca-093bf91a0%UUID%\"/>\n" +
                    "   <textFieldExpression><![CDATA[";
    private static final String JASPER_TEXT_FIELD_END =
            "]]></textFieldExpression>\n" +
                    "   </textField>";
    private static final String JASPER_STATIC_TEXT_START =
            "<staticText>\n" +
                    "   <reportElement style=\"ReportCell\" positionType=\"Float\" mode=\"Opaque\" " +
                    "x=\"%X_POS%\" y=\"0\" width=\"70\" height=\"15\" backcolor=\"#CCCCCC\" " +
                    "uuid=\"9df46860-1aa3-4959-9720-ffbddd8ea%UUID%\">\n" +
                    "   \t<property name=\"com.jaspersoft.studio.unit.y\" value=\"px\"/>\n" +
                    "   </reportElement>\n" +
                    "   <text><![CDATA[";
    private static final String JASPER_STATIC_TEXT_END =
            "]]></text>\n" +
                    "   </staticText>";


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

    static void writeColumns(String tableName, String outputFile, List<Column> columns) throws IOException {

        File file = new File(outputFile);

        try (FileWriter fileWriter = new FileWriter(file)) {
            if (columns.isEmpty()) {
                return;
            }

            int i = 0;
            while (i < columns.size() - 1) {
                fileWriter.write(concat(tableName, columns.get(i++)));
            }
            fileWriter.write(concat(tableName, columns.get(i), true));

        }
    }

    static void writeJasper(String varName, String outputFile, List<Field> fields, List<Column> columns) throws IOException {

        File file = new File(outputFile);

        try (FileWriter fileWriter = new FileWriter(file)) {
            if (columns.isEmpty()) {
                return;
            }

            fileWriter.write(TITLE + "\n");
            for (int i = 0; i < columns.size(); i++) {
                fileWriter.write(
                        concatStaticText(columns.get(i))
                                .replace(X_POS, String.valueOf(i * 70))
                                .replace(UUID, String.valueOf(i))
                );
            }
            fileWriter.write("\n");
            fileWriter.write(DETAIL + "\n");
            for (int i = 0; i < columns.size(); i++) {
                fileWriter.write(
                        concatTextField(varName, fields.get(i))
                                .replace(X_POS, String.valueOf(i * 70))
                                .replace(UUID, addZeros(String.valueOf(i)))
                );
            }

        }
    }

    private static String addZeros(String uuidEnding) {
        switch (uuidEnding.length()) {
            case 1: uuidEnding = "0" + uuidEnding;
            case 2: uuidEnding = "0" + uuidEnding;
        }
        return uuidEnding;
    }

    private static String concat(String varName, Field field) {
        return varName + DOT + SET
                + upLeading(field.name) + OPENING_BRACKET
                + getGetter(field.type) + COLUMN_START
                + field.column.toUpperCase(Locale.ROOT)
                + END
                ;
    }

    private static String concatTextField(String varName, Field field) {
        return JASPER_TEXT_FIELD_START
                + varName + DOT + GET
                + upLeading(field.name) + FUNCTION_END
                + JASPER_TEXT_FIELD_END + RETURN;
    }

    private static String concatStaticText(Column column) {
        return JASPER_STATIC_TEXT_START
                + column.comment
                + JASPER_STATIC_TEXT_END + RETURN;
    }

    private static String concat(String tableName, Column column) {
        return concat(tableName, column, false);
    }

    private static String concat(String tableName, Column column, boolean isLast) {
        return QUOTES +
                tableName + DOT
                + getColumnName(column) + SPACE
                + AS + SPACE + getAlias(column) + (isLast ? EMPTY_STRING : COMMA) + SPACE
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

    private static String getColumnName(Column column) {
        return column.name == null ? UNKNOWN : upper(column.name);
    }

    private static String getAlias(Column column) {
        return column.alias == null ? getColumnName(column) : upper(column.alias);
    }

    private static String getComment(Column column) {
        return column.comment == null ? EMPTY_STRING : COMMENT + column.comment;
    }

    private static String upper(String str) {
        return str.toUpperCase(Locale.ROOT);
    }

}
