package run.antleg.sharp.test.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.stream.Collectors;

import static run.antleg.sharp.modules.Facts.*;

public final class TagUtils {

    private static final IntVector[] randomTagBase;

    static {
        var base = new IntVector[TAG_NAME_MAX_LEN + 1];
        for (int i = TAG_NAME_MIN_LEN; i <= TAG_NAME_MAX_LEN; i++) {
            var boundary = new IntVector.Boundary(0, Characters.HANS.length - 1);
            var init = new int[i];
            for (int j = 0; j < i; j++) {
                init[j] = RandomUtils.nextInt(0, Characters.HANS.length);
            }
            base[i] = new IntVector(boundary, init);
        }
        randomTagBase = base;
    }

    public static String randomTagName() {
        var length = RandomUtils.nextInt(TAG_NAME_MIN_LEN, TAG_NAME_MAX_LEN + 1);
        IntVector vector;
        synchronized (TagUtils.class) {
            vector = randomTagBase[length];
            randomTagBase[length] = vector.increment();
        }
        return vector.stream().mapToObj(i -> "" + Characters.HANS[i]).collect(Collectors.joining());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            System.out.println(randomTagName());
        }
    }
}
