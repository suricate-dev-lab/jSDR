package com.suricatedevlab.jsdr;

import java.util.Map;

public interface TunerDefinition extends AutoCloseable {

    record CrystalFrequency(int rtlFrequency, int tunerFrequency) {
    }

    String getTunerType();
    int getCorrectionFrequency();
    void setCorrectionFrequency(int correctionFrequency);
    long getCenterFrequency();
    void setCenterFrequency(long centerFrequency);
    int[] getSupportedTunerGains();
    void setTunerGainMode(boolean automatic);
    void setTunerGain(int gain);
    int getTunerGain();
    int getSampleRate();
    void setSampleRate(int rate);
    void setAgcMode(boolean activate);
    void setDirectSampling(boolean activate);
    void setCrystalFrequency(CrystalFrequency crystalFrequency);
    CrystalFrequency getCrystalFrequency();
    void setBandwidth(int bandwidth);
    void setBiasTee(boolean activate);
    void setExtraProperties(Map<String, Object> properties);
    TunerSample tune();
}
