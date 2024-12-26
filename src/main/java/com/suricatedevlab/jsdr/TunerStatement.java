package com.suricatedevlab.jsdr;

public interface TunerStatement extends AutoCloseable {
    void setCorrectionFrequency(int correctionFrequency);
    void setCenterFrequency(long centerFrequency);
    void setGain(int gain);
    SampleSet tune();
}
