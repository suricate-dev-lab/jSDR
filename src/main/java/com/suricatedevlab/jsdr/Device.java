package com.suricatedevlab.jsdr;

public interface Device extends AutoCloseable{
    int getIndex();
    String getName();
    String getManufacturer();
    String getProduct();
    String getSerial();
    TunerStatement createTunerStatement() throws SdrException;
}
