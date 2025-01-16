package bloomfilter;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class BloomFilter<E> {
    protected static final int H_MUL = 31;

    private final byte[] bits;
    private final Function<Integer, Integer>[] hf;

    public BloomFilter(DataInput input) throws IOException {
        this(input.readInt());
        // TODO
        input.readFully(this.bits);
    }

    public BloomFilter(final int numBytes) {
        final int vecSize = numBytes * Byte.SIZE;
        this.bits = new byte[numBytes * Byte.SIZE];
        // False positive rate of 1%:
        this.hf = new Function[(int) -(Math.log(0.01) / Math.log(2))];
        for (int i = 0; i < this.hf.length; i++) {
            final int j = i;
            this.hf[i] = k -> (((j * H_MUL * k) % vecSize) + vecSize) % vecSize;
        }
    }

    /**
     * Serialize this filter into the data output, packing 8 bytes from the bits vector into one output byte.
     */
    public void close(DataOutput output) throws IOException {
        // TODO
        output.writeInt(bits.length / Byte.SIZE); // Writing the number of bytes
        output.write(bits); // Writing the serialized bit vector
    }

    /**
     * Add an element to this filter.
     */
    public void add(E element) {
        // TODO:
        int hash = element.hashCode();
        for (Function<Integer, Integer> hashFunction : hf) {
            int index = hashFunction.apply(hash); // Calculating the index in the bit array
            bits[index / Byte.SIZE] |= 1 << (index % Byte.SIZE); // Seting the corresponding bit
        }
    }

    /**
     * Returns false, if the element was definitely not added to the filter, true otherwise.
     */
    public boolean containsMaybe(E element) {
        // TODO
        int hash = element.hashCode();
        for (Function<Integer, Integer> hashFunction : hf) {
            int index = hashFunction.apply(hash); // Calculate index in the bit array
            if ((bits[index / Byte.SIZE] & (1 << (index % Byte.SIZE))) == 0) {
                return false; // If any bit is not set, the element is definitely not present
            }
        }
        return true; // All bits are set, so the element may be present
    }

    /**
     * Reset the state of the bloom filter.
     */
    public void reset() {
        // TODO
        Arrays.fill(bits, (byte) 0); // Clearing all bits in the bit vector
    }
}
