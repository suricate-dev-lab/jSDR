package com.suricatedevlab.jsdr;

public class Main {
    public static void main(String[] args) {
        Driver driver = DriverManager.getDriver("RTL-SDR");
        try(Device device = driver.getDevice(0)){
            String name = device.getName();
            TunerStatement statement = device.createTunerStatement();
            //ADSB settings
            statement.setCenterFrequency(1090000000);
            statement.setOffsetTuning(false);
            statement.setDirectSampling(false);
            statement.setAgcMode(true);
            statement.setSampleRate(2000000);
            statement.setCorrectionFrequency(5);
            SampleSet sampleSet = statement.tune();
            byte[] buffer = null;
            while(sampleSet.next()) {
                buffer = sampleSet.getBytes();
                System.out.println(buffer);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}