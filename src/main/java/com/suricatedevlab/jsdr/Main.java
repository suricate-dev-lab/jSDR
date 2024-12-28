package com.suricatedevlab.jsdr;

import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Driver driver = DriverManager.getDriver("RTL-SDR");
        try(Device device = driver.getDevice(0)){
            System.out.println(device);
            TunerDefinition definition = device.getTunerDefinition();
            int[] supportedGains = definition.getSupportedTunerGains();

            definition.setTunerGain(supportedGains[0]);
            definition.setTunerGainMode(true);
            definition.setCenterFrequency(1090000000);
            definition.setDirectSampling(false);
            definition.setAgcMode(true);
            definition.setSampleRate(2000000);
            definition.setCorrectionFrequency(5);
            definition.setBandwidth(2000000); //2Mhz
            definition.setBiasTee(true);
            TunerDefinition.CrystalFrequency xtal = new TunerDefinition.CrystalFrequency(28800144, 28800144);
            definition.setCrystalFrequency(xtal);

            //Device specific settings
            Map<String, Object> properties = new HashMap<>();
            properties.put("TUNER_OFFSET", true);
            //properties.put("TUNER_DITHERING", true); for R820T device type
            definition.setExtraProperties(properties);

            TunerSample tunerSample = definition.tune();
/*
            Calling async
            SampleSet.ReadAsyncCallback callback = new SampleSet.ReadAsyncCallback() {
                @Override
                public void onReceive(byte[] data) {
                    System.out.println("data receive "+ data);
                }
            };
            sampleSet.readAsync(callback);
 */

            byte[] data;
            while((data = tunerSample.readSync(1024)) != null) {
                System.out.println("data receive "+ data);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}