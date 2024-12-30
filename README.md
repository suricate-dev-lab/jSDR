
# jSDR: Java for Software Defined Radio

jSDR allows you to build your Software Defined Radio (SDR) using a type-safe Java API. You can perform radio signal processing and manipulation while enjoying a familiar Java programming experience.


## How to use

```java
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
            //definition.setTunerIfGain(4, 12);
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
                //System.out.println("data receive "+ data);
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
```

## Testing devices
- [RTL2832U](https://www.amazon.ca/dp/B06Y1D7P48?ref=cm_sw_r_cso_cp_apin_dp_71DBRV5DJZEFNQYQDFP4&ref_=cm_sw_r_cso_cp_apin_dp_71DBRV5DJZEFNQYQDFP4&social_share=cm_sw_r_cso_cp_apin_dp_71DBRV5DJZEFNQYQDFP4&starsLeft=1&skipTwisterOG=1)

## TO DO
- Complete integration
- Add support for Windows
- Add support for Linux
- Add Unit tests

## Acknowledgements

 - [Rtl-sdr Wiki](https://osmocom.org/projects/rtl-sdr/wiki/Rtl-sdr)
 - [Rtl-sdr GIT repository](https://github.com/osmocom/rtl-sdr)

## License

[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0/)

