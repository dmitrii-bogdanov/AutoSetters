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
    private static final String MODIFIER_PRIVATE = "private";
    private static final String SPACE = " ";
    private static final String TODO = "//TODO";
    private static final String SEMICOLON = ";";
    private static final String EMPTY_STRING = "";

    static List<Field> read(String inputFile) throws FileNotFoundException {

        File file = new File(inputFile);

        try (Scanner scanner = new Scanner(file, "UTF-8")) {

            List<Field> fields = new ArrayList<>();
            String str;
            String tmp;
            String[] splitted;
            int i;
            Field field = new Field();
            boolean isAnnotation = false;

            while (scanner.hasNextLine()) {
                str = scanner.nextLine();
                System.out.println(str);

                if (str.contains(TODO)) {
                    continue;
                }

                if (str.contains(ANNOTATION_START)) {
                    isAnnotation = true;
                    continue;
                }
                if (str.contains(ANNOTATION_END)) {
                    isAnnotation = false;
                    continue;
                }

                if (isAnnotation) {
                    if (str.contains(ANNOTATION_COLUMN)) {
                        splitted = str.split(SPACE);
                        System.out.println(Arrays.toString(splitted));
                        for (tmp = splitted[i = 0]; !tmp.equals(COLUMN); tmp = splitted[++i]) ;
                        if (i < splitted.length - 1) {
                            field.column = splitted[++i];
                        }
                        System.out.println(splitted[i]);
                        System.out.println(field.column);
                    } else if (str.contains(ANNOTATION_ALIAS)) {
                        splitted = str.split(SPACE);
                        System.out.println(Arrays.toString(splitted));
                        for (tmp = splitted[i = 0]; !tmp.equals(ALIAS); tmp = splitted[++i]) ;
                        if (i < splitted.length - 1) {
                            field.column = splitted[++i];
                        }
                        System.out.println(splitted[i]);
                        System.out.println(field.column);
                    }
                } else {
                    if (str.contains(MODIFIER_PRIVATE)) {
                        splitted = str.split(SPACE);
                        System.out.println(Arrays.toString(splitted));
                        for (tmp = splitted[i = 0]; !tmp.equals(MODIFIER_PRIVATE); tmp = splitted[++i]) ;
                        field.type = splitted[++i];

                        System.out.println(splitted[i]);
                        System.out.println(field.type);
                        field.name = splitted[++i].replace(SEMICOLON, EMPTY_STRING);

                        System.out.println(splitted[i]);
                        System.out.println(field.name);
                        fields.add(field);
                        field = new Field();
                    }
                }

            }

            return fields;

        }
    }

}
