package com.suricatedevlab.jsdr;

public interface TunerStatement extends AutoCloseable {
    void setCorrectionFrequency(int correctionFrequency);
    void setCenterFrequency(long centerFrequency);
    void setGain(int gain);
    void setSampleRate(int rate);
    void setAgcMode(boolean activate);
    void setDirectSampling(boolean activate);
    void setOffsetTuning(boolean activate);
    SampleSet tune();
}
