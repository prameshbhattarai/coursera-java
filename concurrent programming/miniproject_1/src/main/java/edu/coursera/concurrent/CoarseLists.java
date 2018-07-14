package edu.coursera.concurrent;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Wrapper class for two lock-based concurrent list implementations.
 */
public final class CoarseLists {
    /**
     * An implementation of the ListSet interface that uses Java locks to
     * protect against concurrent accesses.
     * <p>
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java ReentrantLock object
     * to protect against those concurrent accesses. You may refer to
     * SyncList.java for help understanding the list management logic, and for
     * guidance in understanding where to place lock-based synchronization.
     */
    public static final class CoarseList extends ListSet {
        /*
         * TODO Declare a lock for this class to be used in implementing the
         * concurrent add, remove, and contains methods below.
         */
        private final ReentrantLock reentrantLock = new ReentrantLock();

        /**
         * Default constructor.
         */
        public CoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {
            reentrantLock.lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            if (object.equals(curr.object)) {
                reentrantLock.unlock();
                return false;
            } else {
                final Entry entry = new Entry(object);
                entry.next = curr;
                pred.next = entry;
                reentrantLock.unlock();
                return true;
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            reentrantLock.lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            if (object.equals(curr.object)) {
                pred.next = curr.next;
                reentrantLock.unlock();
                return true;
            } else {
                reentrantLock.unlock();
                return false;
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {
            reentrantLock.lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }
            reentrantLock.unlock();
            return object.equals(curr.object);
        }
    }

    /**
     * An implementation of the ListSet interface that uses Java read-write
     * locks to protect against concurrent accesses.
     * <p>
     * TODO Implement the add, remove, and contains methods below to support
     * correct, concurrent access to this list. Use a Java
     * ReentrantReadWriteLock object to protect against those concurrent
     * accesses. You may refer to SyncList.java for help understanding the list
     * management logic, and for guidance in understanding where to place
     * lock-based synchronization.
     */
    public static final class RWCoarseList extends ListSet {
        /*
         * TODO Declare a read-write lock for this class to be used in
         * implementing the concurrent add, remove, and contains methods below.
         */
        private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        /**
         * Default constructor.
         */
        public RWCoarseList() {
            super();
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean add(final Integer object) {
            reentrantReadWriteLock.writeLock().lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            if (object.equals(curr.object)) {
                reentrantReadWriteLock.writeLock().unlock();
                return false;
            } else {
                final Entry entry = new Entry(object);
                entry.next = curr;
                pred.next = entry;
                reentrantReadWriteLock.writeLock().unlock();
                return true;
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean remove(final Integer object) {
            reentrantReadWriteLock.writeLock().lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }

            if (object.equals(curr.object)) {
                pred.next = curr.next;
                reentrantReadWriteLock.writeLock().unlock();
                return true;
            } else {
                reentrantReadWriteLock.writeLock().unlock();
                return false;
            }
        }

        /**
         * {@inheritDoc}
         * <p>
         * TODO Use a read-write lock to protect against concurrent access.
         */
        @Override
        boolean contains(final Integer object) {
            reentrantReadWriteLock.readLock().lock();
            Entry pred = this.head;
            Entry curr = pred.next;

            while (curr.object.compareTo(object) < 0) {
                pred = curr;
                curr = curr.next;
            }
            reentrantReadWriteLock.readLock().unlock();
            return object.equals(curr.object);
        }
    }
}
