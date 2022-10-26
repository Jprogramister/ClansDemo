package org.example.versionablecache;

public class OptimisticLockException extends Exception {
    public OptimisticLockException(String message) {
        super(message);
    }
}
