package com.suricatedevlab.jsdr;

public interface SampleSet extends AutoCloseable {
    boolean next();
    byte[] getBytes();
}
