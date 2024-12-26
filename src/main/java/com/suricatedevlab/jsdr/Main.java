package com.suricatedevlab.jsdr;

public class Main {
    public static void main(String[] args) {
        Driver driver = DriverManager.getDriver("RTL-SDR");
        try(Device device = driver.getDevice(0)){
            String name = device.getName();
            TunerStatement statement = device.createTunerStatement();
            statement.setCenterFrequency(1090000000);
            statement.setCorrectionFrequency(5);
            //statement.setRate(2000000);
            /*
                        SampleSet sampleSet = statement.tune();
            while(sampleSet.next()) {

            }
             */
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello, World!");
    }
}