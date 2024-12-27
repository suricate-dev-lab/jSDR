package com.suricatedevlab.jsdr;

public interface Device extends AutoCloseable{
    int getIndex();
    String getName();
    String getManufacturer();
    String getProduct();
    String getSerial();
    TunerDefinition getTunerDefinition() throws SdrException;
}
