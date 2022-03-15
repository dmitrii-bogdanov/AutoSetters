package bogdanov;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

class Reader {

    private static final String ANNOTATION_START = "/**";
    private static final String ANNOTATION_END = "*/";
    private static final String ANNOTATION_COLUMN = "* Таблица БД:";
    private static final String COLUMN = "БД:";
    private static final String ANNOTATION_ALIAS = "* <br>AS:";
    private static final String ALIAS = "<br>AS:";
    private static final String ANNOTATION_REPORT_HEADER = "* <br>Отчет:";
    private static final String REPORT_HEADER = "<br>Отчет:";
    private static final String MODIFIER_PRIVATE = "private";
    private static final String SPACE = " ";
    private static final String TODO = "//TODO";
    private static final String SEMICOLON = ";";
    private static final String EMPTY_STRING = "";

    static void read(String inputFile, List<Field> fields, List<Column> columns) throws FileNotFoundException {

        File file = new File(inputFile);

        try (Scanner scanner = new Scanner(file, "UTF-8")) {

            fields.clear();
            columns.clear();
            String str;
            String tmp;
            String[] splitted;
            int i;
            int index;
            Field field = new Field();
            Column column = new Column();
            boolean isAnnotation = false;

            while (scanner.hasNextLine()) {
                str = scanner.nextLine();

                if (str.contains(TODO)) {
                    continue;
                }

                if (str.contains(ANNOTATION_START)) {
                    isAnnotation = true;
                    continue;
                }
                if (str.contains(ANNOTATION_END)) {
                    isAnnotation = false;
                    columns.add(column);
                    column = new Column();
                    continue;
                }

                if (isAnnotation) {
                    if (str.contains(ANNOTATION_COLUMN)) {
                        splitted = str.split(SPACE);
                        for (tmp = splitted[i = 0]; !tmp.equals(COLUMN); tmp = splitted[++i]) ;
                        if (i < splitted.length - 1) {
                            field.column = splitted[++i];
                            column.name = splitted[i];
                        }
                    } else if (str.contains(ANNOTATION_ALIAS)) {
                        splitted = str.split(SPACE);
                        for (tmp = splitted[i = 0]; !tmp.equals(ALIAS); tmp = splitted[++i]) ;
                        if (i < splitted.length - 1) {
                            field.column = splitted[++i];
                            column.alias = splitted[i];
                        }
                    } else if (str.contains(ANNOTATION_REPORT_HEADER)) {
                        if ((index = str.indexOf(REPORT_HEADER)) < (str.length() - 1)) {
                            column.comment = str.substring(++index + REPORT_HEADER.length());
                        }
                    }
                } else {
                    if (str.contains(MODIFIER_PRIVATE)) {
                        splitted = str.split(SPACE);
                        for (tmp = splitted[i = 0]; !tmp.equals(MODIFIER_PRIVATE); tmp = splitted[++i]) ;

                        field.type = splitted[++i];
                        field.name = splitted[++i].replace(SEMICOLON, EMPTY_STRING);

                        fields.add(field);
                        field = new Field();
                    }
                }

            }

        }
    }

}
