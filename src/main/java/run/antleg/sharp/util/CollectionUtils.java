package run.antleg.sharp.util;

import java.util.*;
import java.util.function.Function;

public final class CollectionUtils {

    public static <E> ArrayList<E> mutList(Iterable<E> itr) {
        return mutList(itr.iterator());
    }

    public static <E> ArrayList<E> mutList(Iterator<E> itr) {
        var list = new ArrayList<E>();
        while (itr.hasNext()) list.add(itr.next());
        return list;
    }

    public static <K, E> Map<K, E> keyBy(Collection<E> c, Function<E, K> toKey) {
        var map = new HashMap<K, E>();
        c.forEach(it -> map.put(toKey.apply(it), it));
        return map;
    }
}
