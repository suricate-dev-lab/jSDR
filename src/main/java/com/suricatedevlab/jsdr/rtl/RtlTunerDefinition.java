package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.ptr.IntByReference;
import com.suricatedevlab.jsdr.TunerSample;
import com.suricatedevlab.jsdr.TunerDefinition;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

class RtlTunerDefinition implements TunerDefinition {

    private static final String PROPERTY_TUNER_OFFSET = "TUNER_OFFSET";
    private static final String PROPERTY_TUNER_DITHERING = "TUNER_DITHERING";

    private final RtlDevice device;

    public RtlTunerDefinition(RtlDevice device) {
        this.device = device;
    }

    public RtlDevice getDevice() {
        return device;
    }

    @Override
    public String getTunerType() {
        int tunerTypeCode = device.getNativeLibrary().rtlsdr_get_tuner_type(device.getHandle().getValue());
        RtlSdrTunerType result = RtlSdrTunerType.fromCode(tunerTypeCode);
        return result.getDisplayName();
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
    public void setTunerGainMode(boolean automatic) {
        int result = device.getNativeLibrary().rtlsdr_set_tuner_gain_mode(device.getHandle().getValue(), automatic ? 1 : 0);
        if (result < 0) {
            throw new IllegalStateException("Failed to set tuner gain mode");
        }
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
    public void setTunerIfGain(int stage, int gain) {
        int result = device.getNativeLibrary().rtlsdr_set_tuner_if_gain(device.getHandle().getValue(), stage, gain);
        if (result < 0) {
            throw new IllegalArgumentException("Invalid tuner if gain");
        }
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
    public void setCrystalFrequency(CrystalFrequency crystalFrequency) {
        int result = device.getNativeLibrary().rtlsdr_set_xtal_freq(device.getHandle().getValue(),
                crystalFrequency.rtlFrequency(), crystalFrequency.tunerFrequency());
        if (result < 0) {
            throw new IllegalArgumentException("Failed to set crystal frequency");
        }
    }

    @Override
    public CrystalFrequency getCrystalFrequency() {
        IntByReference rtlFrequency = new IntByReference();
        IntByReference tunerFrequency = new IntByReference();
        int result = device.getNativeLibrary().rtlsdr_get_xtal_freq(device.getHandle().getValue(),
                rtlFrequency, tunerFrequency);
        if (result < 0) {
            throw new IllegalArgumentException("Failed to get crystal frequency");
        }
        return new CrystalFrequency(rtlFrequency.getValue(), tunerFrequency.getValue());
    }

    @Override
    public void setBandwidth(int bandwidth) {
        int result = device.getNativeLibrary().rtlsdr_set_tuner_bandwidth(device.getHandle().getValue(), bandwidth);
        if (result < 0) {
            throw new IllegalStateException("Failed to set bandwidth");
        }
    }

    @Override
    public void setBiasTee(boolean activate) {
        int result = device.getNativeLibrary().rtlsdr_set_bias_tee(device.getHandle().getValue(), activate ? 1 : 0);
        if (result < 0) {
            throw new IllegalArgumentException("Failed to set in direct sampling");
        }
    }

    @Override
    public void setExtraProperties(Map<String, Object> properties) {
        if (properties == null) {
            return;
        }
        if (properties.containsKey(PROPERTY_TUNER_OFFSET)) {
            Object value = properties.get(PROPERTY_TUNER_OFFSET);
            if (value instanceof Boolean castedValue) {
                int result = device.getNativeLibrary().rtlsdr_set_offset_tuning(device.getHandle().getValue(), castedValue ? 1 : 0);
                if (result == -1) {
                    throw new IllegalStateException("Uninitialized device handle");
                }
                else if (result == -2) {
                    throw new UnsupportedOperationException("Failed to set in offset tuning mode since direct sampling = true");
                }
                else if (result == -3) {
                    throw new IllegalStateException("Failed to set in offset tuning mode since direct sampling = true");
                }
                else if (result < 0) {
                    throw new IllegalStateException("Failed to set in offset tuning mode");
                }
            }
            else {
                throw new IllegalArgumentException("Failed to set in offset tuning mode expecting boolean");
            }
        }

        if (properties.containsKey(PROPERTY_TUNER_DITHERING)) {

            RtlSdrTunerType tunerType = RtlSdrTunerType.fromDisplayName(getTunerType());

            if (!RtlSdrTunerType.R820T.equals(tunerType)) {
                throw new UnsupportedOperationException(String.format("Tuner dithering is only supported by  %s device type",
                        RtlSdrTunerType.R820T));
            }

            Object value = properties.get(PROPERTY_TUNER_DITHERING);
            if (value instanceof Boolean castedValue) {
                int result = device.getNativeLibrary().rtlsdr_set_dithering(device.getHandle().getValue(), castedValue ? 1 : 0);
                if (result == -1) {
                    throw new IllegalStateException("Uninitialized device handle");
                }
                else if (result == 1) {
                    throw new UnsupportedOperationException("Can not set tuner dithering for this device");
                }
                else if (result < 0) {
                    throw new IllegalStateException("Failed to set tuner dithering");
                }
            }
            else {
                throw new IllegalArgumentException("Failed to set in offset tuning mode expecting boolean");
            }
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

    public enum RtlSdrTunerType {

        E4000(RtlNativeLibrary.RTLSDR_TUNER_TYPE_E4000, "RTLSDR_TUNER_E4000"),
        FC0012(RtlNativeLibrary.RTLSDR_TUNER_TYPE_FC0012, "RTLSDR_TUNER_FC0012"),
        FC0013(RtlNativeLibrary.RTLSDR_TUNER_TYPE_FC0013, "RTLSDR_TUNER_FC0013"),
        FC2580(RtlNativeLibrary.RTLSDR_TUNER_TYPE_FC2580, "RTLSDR_TUNER_FC2580"),
        R820T(RtlNativeLibrary.RTLSDR_TUNER_TYPE_R820T, "RTLSDR_TUNER_R820T"),
        R828D(RtlNativeLibrary.RTLSDR_TUNER_TYPE_R828D, "RTLSDR_TUNER_R828D"),
        UNKNOWN(RtlNativeLibrary.RTLSDR_TUNER_TYPE_UNKNOWN, "RTLSDR_TUNER_UNKNOWN");

        private final int code;
        private final String displayName;
        RtlSdrTunerType(int code, String displayName) {
            this.code = code;
            this.displayName = displayName;
        }

        public int getCode() {
            return code;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static RtlSdrTunerType fromCode(int tunerTypeCode) {
            RtlSdrTunerType result = UNKNOWN;
            for(RtlSdrTunerType entry : values()) {
                if (entry.code == tunerTypeCode) {
                    result = entry;
                    break;
                }
            }
            return result;
        }

        public static RtlSdrTunerType fromDisplayName(String displayName) {
            RtlSdrTunerType result = UNKNOWN;
            for(RtlSdrTunerType entry : values()) {
                if (entry.getDisplayName().equals(displayName.toUpperCase(Locale.ENGLISH))) {
                    result = entry;
                    break;
                }
            }
            return result;
        }
    }

    private String getFormattedIntValues(int[] values) {
        return Arrays.stream(values)
                .mapToObj(String::valueOf) // Convert each int to String
                .collect(Collectors.joining(", "));
    }
}
