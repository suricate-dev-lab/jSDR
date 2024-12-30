package com.suricatedevlab.jsdr;

import java.util.Map;

/**
 * The {@code TunerDefinition} interface defines the configuration and control methods
 * for a Software Defined Radio (SDR) tuner. It provides methods to manage various aspects
 * of the tuner, including frequency, gain, sample rate, automatic gain control (AGC),
 * and more. Implementations of this interface allow access to the tuner's capabilities
 * and settings, enabling users to fine-tune their SDR setup.
 *
 * <p>This interface extends {@link AutoCloseable}, meaning implementations must ensure
 * proper resource management by closing any associated hardware or memory resources
 * when no longer in use.</p>
 *
 * <h2>Key Features</h2>
 * <ul>
 *     <li><b>Tuner Type:</b> Retrieve the type of tuner and its associated settings.</li>
 *     <li><b>Frequency Control:</b> Set and get center frequency, correction frequency, and crystal frequency.</li>
 *     <li><b>Gain Control:</b> Control tuner gain (automatic or manual) and adjust specific gain stages.</li>
 *     <li><b>Sample Rate and Bandwidth:</b> Configure the sample rate and bandwidth for signal capture.</li>
 *     <li><b>AGC and Direct Sampling:</b> Enable or disable Automatic Gain Control (AGC) and direct sampling mode.</li>
 *     <li><b>Extra Properties:</b> Set additional, device-specific properties.</li>
 * </ul>
 *
 * @see AutoCloseable
 * @see TunerSample
 */
public interface TunerDefinition extends AutoCloseable {

    /**
     * A record representing the crystal frequency pairing for the tuner. The crystal frequency
     * is used to calibrate the local oscillator and ensure accurate frequency tuning.
     */
    record CrystalFrequency(int rtlFrequency, int tunerFrequency) {
    }

    /**
     * Returns the type of the tuner.
     *
     * <p>The tuner type may represent the underlying hardware or configuration of the tuner.
     * It could include information such as model type, manufacturer, or specific features of the
     * tuner, allowing the application to adapt its usage based on the device's capabilities.</p>
     *
     * @return the type of the tuner as a string (e.g., "RTL-SDR", "Airspy", etc.)
     */
    String getTunerType();

    /**
     * Returns the current correction frequency used to calibrate the tuner.
     *
     * <p>The correction frequency is used to adjust the frequency of the tuner to match
     * the intended target frequency more precisely. It helps compensate for hardware inaccuracies
     * in the tuner’s frequency output.</p>
     *
     * @return the correction frequency, in Hz
     */
    int getCorrectionFrequency();

    /**
     * Sets the correction frequency to the specified value.
     *
     * <p>This method adjusts the correction frequency used to fine-tune the tuner’s output.
     * A correct value helps the tuner produce more accurate frequency outputs, ensuring precision
     * in signal capture.</p>
     *
     * @param correctionFrequency the new correction frequency in Hz
     */
    void setCorrectionFrequency(int correctionFrequency);

    /**
     * Returns the current center frequency of the tuner.
     *
     * <p>The center frequency represents the central frequency around which the tuner is tuned.
     * This value determines which part of the spectrum the tuner is currently focusing on.</p>
     *
     * @return the center frequency, in Hz
     */
    long getCenterFrequency();

    /**
     * Sets the center frequency of the tuner to the specified value.
     *
     * <p>The center frequency defines the focal point of the signal being tuned by the SDR device.
     * Adjusting this value allows the user to tune into different parts of the spectrum.</p>
     *
     * @param centerFrequency the new center frequency in Hz
     */
    void setCenterFrequency(long centerFrequency);

    /**
     * Returns an array of supported tuner gain values.
     *
     * <p>This method returns the available gain levels supported by the tuner. It is useful for
     * determining the range of gain values that can be configured for signal amplification.</p>
     *
     * @return an array of supported tuner gain values
     */
    int[] getSupportedTunerGains();

    /**
     * Sets the tuner gain mode to automatic or manual.
     *
     * <p>If set to {@code true}, the tuner will automatically adjust its gain based on signal strength.
     * If set to {@code false}, the user can manually set the gain value.</p>
     *
     * @param automatic {@code true} to enable automatic gain control, {@code false} for manual gain control
     */
    void setTunerGainMode(boolean automatic);

    /**
     * Sets the tuner gain to the specified value.
     *
     * <p>This method adjusts the signal amplification of the tuner. The gain value should be chosen
     * based on the strength of the incoming signal and the desired output level.</p>
     *
     * @param gain the gain value to set (usually in dB)
     */
    void setTunerGain(int gain);

    /**
     * Returns the current tuner gain value.
     *
     * <p>This method returns the current gain level applied to the tuner. The gain is typically used to
     * control the sensitivity of the tuner and its ability to detect weak signals.</p>
     *
     * @return the current tuner gain value
     */
    int getTunerGain();

    /**
     * Sets the gain value for a specific tuner stage.
     *
     * <p>The tuner may have multiple stages where gain can be adjusted (e.g., front-end amplifier,
     * intermediate frequency (IF) stages). This method allows setting the gain for specific stages
     * in the signal chain.</p>
     *
     * @param stage the stage of the tuner (e.g., front-end, IF, etc.)
     * @param gain the gain value to set for the specified stage
     */
    void setTunerIfGain(int stage, int gain);

    /**
     * Returns the current sample rate of the tuner.
     *
     * <p>The sample rate defines how frequently the tuner samples the radio spectrum. Higher sample
     * rates allow capturing more data, but may also require more processing power and bandwidth.</p>
     *
     * @return the current sample rate, in samples per second (S/s)
     */
    int getSampleRate();

    /**
     * Sets the sample rate of the tuner.
     *
     * <p>This method adjusts how frequently the tuner samples the radio spectrum. Higher sample rates
     * provide more detail and capture higher-frequency signals, but may require more resources.</p>
     *
     * @param rate the new sample rate, in samples per second (S/s)
     */
    void setSampleRate(int rate);

    /**
     * Enables or disables Automatic Gain Control (AGC) mode.
     *
     * <p>AGC automatically adjusts the gain of the tuner to compensate for changes in signal strength.
     * Enabling this mode allows the tuner to adapt to varying signal levels without manual intervention.</p>
     *
     * @param activate {@code true} to enable AGC, {@code false} to disable it
     */
    void setAutomaticGainControl(boolean activate);

    /**
     * Enables or disables direct sampling mode.
     *
     * <p>Direct sampling mode allows the tuner to directly sample the radio signal without using
     * an intermediate frequency (IF). This is useful for certain types of signal processing.</p>
     *
     * @param activate {@code true} to enable direct sampling, {@code false} to disable it
     */
    void setDirectSampling(boolean activate);

    /**
     * Sets the crystal frequency used for tuning calibration.
     *
     * <p>The crystal frequency is used to calibrate the local oscillator and ensure accurate frequency
     * tuning. It may be adjusted to compensate for inaccuracies in the hardware.</p>
     *
     * @param crystalFrequency the crystal frequency, including both the RTL and tuner frequencies
     */
    void setCrystalFrequency(CrystalFrequency crystalFrequency);

    /**
     * Returns the current crystal frequency used for calibration.
     *
     * <p>This method returns the current crystal frequency settings for the tuner, including both
     * the RTL frequency and the tuner frequency. It helps in ensuring precise frequency calibration.</p>
     *
     * @return the current crystal frequency
     */
    CrystalFrequency getCrystalFrequency();

    /**
     * Sets the tuner bandwidth.
     *
     * <p>The bandwidth determines the width of the frequency range the tuner can process. Larger
     * bandwidths capture a wider range of frequencies, but require more resources to process.</p>
     *
     * @param bandwidth the new bandwidth in Hz
     */
    void setBandwidth(int bandwidth);

    /**
     * Enables or disables the bias tee functionality.
     *
     * <p>The bias tee is used to provide power to external components such as antennas or preamps.
     * This method allows the user to turn the bias tee on or off depending on the needs of the system.</p>
     *
     * @param activate {@code true} to enable the bias tee, {@code false} to disable it
     */
    void setBiasTee(boolean activate);

    /**
     * Sets additional properties for the tuner.
     *
     * <p>This method allows for setting extra properties that may be specific to the tuner or hardware
     * configuration. These properties could include custom settings not covered by the standard API.</p>
     *
     * @param properties a map of property names to values
     */
    void setExtraProperties(Map<String, Object> properties);

    /**
     * Tunes the device to the specified parameters.
     *
     * <p>This method performs the actual tuning process, adjusting all necessary parameters such
     * as frequency, gain, and bandwidth to match the user’s configuration. It returns a {@link TunerSample}
     * that can be used for sampling the tuned frequency.</p>
     *
     * @return a {@link TunerSample} object for the tuned frequency
     */
    TunerSample tune();
}
