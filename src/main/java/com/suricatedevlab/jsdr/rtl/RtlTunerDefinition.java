package com.suricatedevlab.jsdr.rtl;

import com.suricatedevlab.jsdr.TunerSample;
import com.suricatedevlab.jsdr.TunerDefinition;

import java.util.Arrays;
import java.util.stream.Collectors;

class RtlTunerDefinition implements TunerDefinition {

    private final RtlDevice device;

    public RtlTunerDefinition(RtlDevice device) {
        this.device = device;
    }

    public RtlDevice getDevice() {
        return device;
    }

    @Override
    public int getCorrectionFrequency() {
        return device.getNativeLibrary().rtlsdr_get_freq_correction(device.getHandle().getValue());
    }

    @Override
    public void setCorrectionFrequency(int correctionFrequency) {
        int result = device.getNativeLibrary().rtlsdr_set_freq_correction(device.getHandle().getValue(), correctionFrequency);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid correction frequency (PPM)");
        }
    }

    @Override
    public long getCenterFrequency() {
        return device.getNativeLibrary().rtlsdr_get_center_freq(device.getHandle().getValue());
    }

    @Override
    public void setCenterFrequency(long frequency) {
        int result = device.getNativeLibrary().rtlsdr_set_center_freq(device.getHandle().getValue(), frequency);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid center frequency");
        }
    }

    @Override
    public int[] getSupportedTunerGains() {
        int[] gains = new int[256];
        int result = device.getNativeLibrary().rtlsdr_get_tuner_gains(device.getHandle().getValue(), gains);
        if (result < 0) {
            throw new IllegalStateException("Can not fetch supported tuner gains");
        }
        return ByteUtils.trim(gains);
    }

    @Override
    public void setTunerGain(int gain) {
        int[] supportedTunerGains = getSupportedTunerGains();
        if (Arrays.stream(supportedTunerGains).noneMatch(g -> g == gain)) {
            String formattedTunerGains = getFormattedIntValues(supportedTunerGains);
            throw new IllegalArgumentException(String
                                        .format("Invalid tuner gain = %d - only tuner gains: %s are supported",
                                                gain, formattedTunerGains));
        }
        int result = device.getNativeLibrary().rtlsdr_set_tuner_gain(device.getHandle().getValue(), gain);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid tuner gain");
        }
    }

    @Override
    public int getTunerGain() {
        return device.getNativeLibrary().rtlsdr_get_tuner_gain(device.getHandle().getValue());
    }

    @Override
    public int getSampleRate() {
        return device.getNativeLibrary().rtlsdr_get_sample_rate(device.getHandle().getValue());
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
    public TunerSample tune() {
        return new RtlTunerSample(this);
    }

    @Override
    public void close() throws Exception {
        device.close();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(String.format("FREQUENCY CORRECTION = %d\n", getCorrectionFrequency()));
        result.append(String.format("CENTER FREQUENCY = %d\n", getCenterFrequency()));
        result.append(String.format("SAMPLE RATE = %d\n", getSampleRate()));
        result.append(String.format("SUPPORTED TUNER GAIN = %s\n", getFormattedIntValues(getSupportedTunerGains())));
        return result.toString();
    }

    private String getFormattedIntValues(int[] values) {
        return Arrays.stream(values)
                .mapToObj(String::valueOf) // Convert each int to String
                .collect(Collectors.joining(", "));
    }
}
