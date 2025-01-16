package bloomfilter;

import java.util.Collection;
import java.util.LinkedList;

public class BloomList<E> extends LinkedList<E> {
    private final BloomFilter<E> bf;

    /**
     * Create a BloomList from a BloomFilter instance.
     */
    public BloomList(BloomFilter<E> bf) {
        this.bf = bf;
    }

    /**
     * Reset the bloom filter to bring it to the optimal state
     * (i.e. the state reached when inserting all current element into the filter).
     * Can be called after (many) deletions to reduce the number of false positives.
     */
    public void resetBloomFilter() {
        // TODO
        bf.reset();
        for (E element : this) {
            bf.add(element); // Reinserting all current elements into the BloomFilter
        }
    }

    @Override
    public boolean add(E e) {
        // TODO
        boolean added = super.add(e); // Adding element to the list
        if (added) {
            bf.add(e); // Adding element to the BloomFilter
        }
        return added;
    }

    @Override
    public void add(int index, E element) {
        // TODO
        super.add(index, element); // Adding element at the specified index
        bf.add(element); // Adding element to the BloomFilter
    }

    @Override
    public E set(int i, E e) {
        // TODO
        E replaced = super.set(i, e); // Replacing element at index i
        bf.add(e); // Adding the new element to the BloomFilter
        resetBloomFilter(); // Rebuilding the BloomFilter to handle possible changes
        return replaced;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        // TODO
        boolean added = super.addAll(index, c); // Adding all elements starting at the specified index
        if (added) {
            for (E element : c) {
                bf.add(element); // Adding each element to the BloomFilter
            }
        }
        return added;
    }

    @Override
    public boolean contains(Object e) {
        // TODO
        @SuppressWarnings("unchecked")
        E element = (E) e;
        return bf.containsMaybe(element) && super.contains(e); // Checking BloomFilter first, then list
    }

    @Override
    public void clear() {
        // TODO
        super.clear(); // Clearing the list
        bf.reset(); // Resetting the BloomFilter
    }
}
