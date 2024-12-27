package com.suricatedevlab.jsdr;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        Driver driver = DriverManager.getDriver("RTL-SDR");
        try(Device device = driver.getDevice(0)){
            String name = device.getName();
            TunerStatement statement = device.createTunerStatement();
            //ADSB settings
            statement.setCenterFrequency(1090000000);
            statement.setOffsetTuning(false);
            statement.setDirectSampling(true);
            statement.setAgcMode(true);
            statement.setSampleRate(2000000);
            statement.setCorrectionFrequency(5);
            SampleSet sampleSet = statement.tune();
/*
            SampleSet.ReadAsyncCallback callback = new SampleSet.ReadAsyncCallback() {
                @Override
                public void onReceive(byte[] data) {
                    System.out.println("data receive "+ data);
                }
            };
            sampleSet.readAsync(callback);
 */

            byte[] data;
            while((data = sampleSet.readSync(1024)) != null) {
                System.out.println("data receive "+ data);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}