package hashing;
import java.util.logging.Logger;


import xxl.core.collections.containers.io.BlockFileContainer;
import xxl.core.io.Buffer;
import xxl.core.io.LRUBuffer;
import xxl.core.io.converters.LongConverter;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        int numInserts = 100;

        BlockFileContainer primary = new BlockFileContainer("main", ExternalLinearHashMap.BLOCK_SIZE);
        BlockFileContainer secondary = new BlockFileContainer("overflow", ExternalLinearHashMap.BLOCK_SIZE);
        Buffer<Object, Integer, HashBlock<Long, Long>> buffer = new LRUBuffer<>(512);

        primary.clear();
        secondary.clear();

        ExternalLinearHashMap<Long, Long> map = new ExternalLinearHashMap<Long, Long>(LongConverter.DEFAULT_INSTANCE, LongConverter.DEFAULT_INSTANCE, primary, secondary, buffer);
        for (long i = 0; i < numInserts; i++) {
            System.out.println("Inserting: " + i + "...");
            map.insert(i, i);

            for (long j = 0; j <= i; j++) {
                System.out.println("Checking: " + j + "...");
                if (!map.contains(j)) {
                    System.out.println("Element not found: " + i);
                    // throw new RuntimeException("Element not found: " + j);
                }
            }

            if (map.getSize() != i + 1) {
                throw new RuntimeException("wrong size");
            }
        }

        for (long i = 0; i < numInserts; i++) {
            if (!map.contains(i)) {
                System.out.println("Element not found: " + i);
                // throw new RuntimeException("Element not found: " + i);
            }
        }

        map.close();

        // Map should be persisted and work after re-creating
        map = new ExternalLinearHashMap<Long, Long>(LongConverter.DEFAULT_INSTANCE, LongConverter.DEFAULT_INSTANCE, primary, secondary, buffer);

        for (long i = 0; i < numInserts; i++) {
            if (!map.contains(i)) {
                System.out.println("Element not found: " + i);
                // throw new RuntimeException("Element not found: " + i);
            }
        }
    }
}