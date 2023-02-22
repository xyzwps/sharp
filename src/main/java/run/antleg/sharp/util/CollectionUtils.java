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

    public static <K, V> HashMap<K, V> mutMap() {
        return new HashMap<>();
    }

    public static <K, V> HashMap<K, V> mutMap(K k, V v) {
        var map = new HashMap<K, V>();
        map.put(k, v);
        return map;
    }

    public static <K, V> HashMap<K, V> mutMap(K k1, V v1, K k2, V v2) {
        var map = new HashMap<K, V>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    @SafeVarargs
    public static <E> HashSet<E> mutSet(Collection<E>... collections) {
        var set = new HashSet<E>();
        for (var c : collections) {
            set.addAll(c);
        }
        return set;
    }

    public static <K, E> Map<K, E> keyBy(Collection<E> c, Function<E, K> toKey) {
        var map = new HashMap<K, E>();
        c.forEach(it -> map.put(toKey.apply(it), it));
        return map;
    }
}
