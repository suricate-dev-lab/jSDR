package com.suricatedevlab.jsdr;

public interface TunerDefinition extends AutoCloseable {
    int getCorrectionFrequency();
    void setCorrectionFrequency(int correctionFrequency);
    long getCenterFrequency();
    void setCenterFrequency(long centerFrequency);
    int[] getSupportedTunerGains();
    void setTunerGain(int gain);
    int getTunerGain();
    int getSampleRate();
    void setSampleRate(int rate);
    void setAgcMode(boolean activate);
    void setDirectSampling(boolean activate);
    void setOffsetTuning(boolean activate);
    /*
        result.append(String.format("SAMPLE RATE = %d\n",
                device.getNativeLibrary().rtlsdr_get_sample_rate(device.getHandle().getValue())));
     */
    TunerSample tune();
}
