package com.suricatedevlab.jsdr;

public class Main {
    public static void main(String[] args) {
        Driver driver = DriverManager.getDriver("RTL-SDR");
        try(Device device = driver.getDevice(0)){
            String name = device.getName();
            TunerDefinition definition = device.getTunerDefinition();
            //ADSB settings
            definition.setTunerGain(-10);
            definition.setCenterFrequency(1090000000);
            definition.setOffsetTuning(false);
            definition.setDirectSampling(true);
            definition.setAgcMode(true);
            definition.setSampleRate(2000000);
            definition.setCorrectionFrequency(5);
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