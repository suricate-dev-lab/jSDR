package com.suricatedevlab.jsdr.rtl;

import com.suricatedevlab.jsdr.SampleSet;
import com.suricatedevlab.jsdr.TunerStatement;

class RtlTunerStatement implements TunerStatement {

    private final RtlDevice device;

    public RtlTunerStatement(RtlDevice device) {
        this.device = device;
    }

    public RtlDevice getDevice() {
        return device;
    }

    @Override
    public void setCorrectionFrequency(int correctionFrequency) {
        int result = device.getNativeLibrary().rtlsdr_set_freq_correction(device.getHandle().getValue(), correctionFrequency);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid correction frequency (PPM)");
        }
    }

    @Override
    public void setCenterFrequency(long frequency) {
        int result = device.getNativeLibrary().rtlsdr_set_center_freq(device.getHandle().getValue(), frequency);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid center frequency");
        }
    }

    @Override
    public void setGain(int gain) {
        /*
                int result = device.getNativeLibrary().rtlsdr_set_tuner_gain(device.getHandle().getValue(), gain);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid gain");
        }
         */
    }

    @Override
    public void setSampleRate(int rate) {
        int result = device.getNativeLibrary().rtlsdr_set_sample_rate(device.getHandle().getValue(), rate);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid sample rate");
        }
    }

    @Override
    public void setAgcMode(boolean activate) {
        int result = device.getNativeLibrary().rtlsdr_set_agc_mode(device.getHandle().getValue(), activate ? 1 : 0);
        if (result < 0) {
            throw new IllegalArgumentException("Failed to set in AGC mode");
        }
    }

    @Override
    public void setDirectSampling(boolean activate) {
        int result = device.getNativeLibrary().rtlsdr_set_direct_sampling(device.getHandle().getValue(), activate ? 1 : 0);
        if (result < 0) {
            throw new IllegalArgumentException("Failed to set in direct sampling");
        }
    }

    @Override
    public void setOffsetTuning(boolean activate) {
        int result = device.getNativeLibrary().rtlsdr_set_offset_tuning(device.getHandle().getValue(), activate ? 1 : 0);
        if (result < 0) {
            throw new IllegalArgumentException("Failed to set in offset tuning mode");
        }
    }


    @Override
    public SampleSet tune() {
        return new RtlSampleSet(this);
    }

    @Override
    public void close() throws Exception {
        device.close();
    }
}
