package bewis09.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Util {
    public static <T,S> Map<T,S> withMethod(Collection<T> t, MethodInterface<T,S> method) {
        Map<T,S> map = new HashMap<>();
        for (T ts : t) {
            map.put(ts,method.getS(ts));
        }
        return map;
    }

    public static <T,S> Map<T,S> withMethod(T[] t, MethodInterface<T,S> method) {
        Map<T,S> map = new HashMap<>();
        for (T ts : t) {
            map.put(ts,method.getS(ts));
        }
        return map;
    }

    public interface MethodInterface<T,S> {
        S getS(T t);
    }
}
