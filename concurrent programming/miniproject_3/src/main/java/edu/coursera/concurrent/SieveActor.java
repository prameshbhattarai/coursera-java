package edu.coursera.concurrent;

import edu.rice.pcdp.Actor;

import java.util.HashMap;
import java.util.Map;

import static edu.rice.pcdp.PCDP.async;
import static edu.rice.pcdp.PCDP.finish;

/**
 * An actor-based implementation of the Sieve of Eratosthenes.
 * <p>
 * TODO Fill in the empty SieveActorActor actor class below and use it from
 * countPrimes to determin the number of primes <= limit.
 */
public final class SieveActor extends Sieve {
    /**
     * {@inheritDoc}
     * <p>
     * TODO Use the SieveActorActor class to calculate the number of primes <=
     * limit in parallel. You might consider how you can model the Sieve of
     * Eratosthenes as a pipeline of actors, each corresponding to a single
     * prime number.
     */
    @Override
    public int countPrimes(final int limit) {
        final SieveActorActor sieveActorActor = new SieveActorActor(2);

        finish(() -> {
            async(() -> {
                for (int i = 3; i <= limit; i += 2) {
                    sieveActorActor.send(i);
                }
//                sieveActorActor.send(0);
            });
        });

        return sieveActorActor.getNumLocalPrime();

//        SieveActorActor loopActor = sieveActorActor;
//        int numPrimes = 0;
//        while (loopActor != null) {
//            numPrimes += loopActor.getNumLocalPrime();
//            loopActor = loopActor.getNextActor();
//        }
//        return numPrimes;
    }

    /**
     * An actor class that helps implement the Sieve of Eratosthenes in
     * parallel.
     */
    public static final class SieveActorActor extends Actor {

        private final Map<Integer, Integer> localPrimes = new HashMap<>();
        private SieveActorActor nextActor;
        private int numLocalPrime;

        SieveActorActor(int val) {
//            System.out.println("initializing object..");
            localPrimes.put(0, val);
            nextActor = null;
            numLocalPrime = 1;
        }

        public int getNumLocalPrime() {
            return numLocalPrime;
        }

        public SieveActorActor getNextActor() {
            return nextActor;
        }

        boolean isLocallyPrime(int isPrime) {
            for (int i = 0; i < getNumLocalPrime(); i++) {
                if (isPrime % localPrimes.get(i) == 0)
                    return false;
            }
            return true;
        }

        /**
         * Process a single message sent to this actor.
         * <p>
         * TODO complete this method.
         *
         * @param msg Received message
         */
        @Override
        public void process(final Object msg) {
            final int checkIsPrime = (Integer) msg;
//            System.out.println("check prime for " + checkIsPrime);
//                System.out.println("num local prime " + getNumLocalPrime());
            final boolean locallyPrime = isLocallyPrime(checkIsPrime);
            if (locallyPrime) {
//                System.out.println("it is prime" + checkIsPrime);

                localPrimes.put(getNumLocalPrime(), checkIsPrime);
                numLocalPrime += 1;
            } else if (nextActor == null) {
                nextActor = new SieveActorActor(checkIsPrime);
            } else {
                nextActor.send(msg);
            }
        }
    }

    public static void main(String... args) {
        SieveActor seq = new SieveActor();
        System.out.println(seq.countPrimes(12));
    }
}
