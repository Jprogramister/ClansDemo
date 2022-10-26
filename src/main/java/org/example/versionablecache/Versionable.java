package org.example.versionablecache;

public interface Versionable {
    Long getVersion();
    Versionable incrementVersion();
}
