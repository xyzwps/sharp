package run.antleg.sharp.test.util;

import java.util.Arrays;
import java.util.Objects;

public class IntVector {

    public final int size;

    private final int[] data;

    private final Boundary boundary;

    public IntVector(Boundary boundary, int... elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("No elements");
        }
        this.boundary = Objects.requireNonNull(boundary);
        if (!boundary.isValid()) {
            throw new IllegalArgumentException("Invalid boundary");
        }
        for (var e : elements) {
            if (!this.boundary.cover(e)) {
                throw new IllegalArgumentException("Element " + e + " out of boundary.");
            }
        }

        this.data = elements;
        this.size = data.length;
    }

    public IntVector(final int size, Boundary boundary) {
        this(boundary, init(size, boundary.inf));
    }

    private static int[] init(final int size, int init) {
        int[] elements = new int[size];
        Arrays.fill(elements, init);
        return elements;
    }

    public int get(int index) {
        this.checkIndex(index);
        return this.data[index];
    }

    public IntVector increment() {
        int[] newData = new int[this.size];
        boolean shouldIncrement = true;
        for (int i = this.size - 1; i >= 0; i--) {
            int di = data[i];
            if (shouldIncrement) {
                if (this.boundary.isSup(di)) {
                    newData[i] = this.boundary.inf;
                } else {
                    newData[i] = di + 1;
                    shouldIncrement = false;
                }
            } else {
                newData[i] = di;
            }
        }
        if (shouldIncrement) {
            return new IntVector(this.size, this.boundary);
        } else {
            return new IntVector(this.boundary, newData);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    /**
     * @param inf included
     * @param sup included
     */
    public record Boundary(int inf, int sup) {
        public boolean isValid() {
            return sup >= inf;
        }

        public boolean cover(int value) {
            return value >= inf && value <= sup;
        }

        public boolean isSup(int value) {
            return value == sup;
        }
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append('(');
        boolean first = true;
        for (var e : this.data) {
            if (first) first = false;
            else sb.append(',');
            sb.append(e);
        }
        sb.append(')');
        return sb.toString();
    }
}
