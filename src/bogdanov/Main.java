package bogdanov;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            Converter.convert(args[0], args[1], args[2], args[3], args[4], args[5],args[6]);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
}
