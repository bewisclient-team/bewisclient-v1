package bewis09.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log {

    static Logger logger = LoggerFactory.getLogger("bewisclient");

    @SuppressWarnings("all")
    public static void log(Object... strings) {
        StringBuilder string = new StringBuilder();
        for (Object str : strings) {
            string.append(str).append(", ");
        }
        System.out.print("\u001B[36m[BEWISCLIENT] \u001B[0m"+string.toString().replaceFirst("..$",""));
        System.out.println();
        logger.info(string.toString().replaceFirst("..$",""));
    }
}
