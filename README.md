
# jSDR: Java for Software Defined Radio

jSDR allows you to build your Software Defined Radio (SDR) using a type-safe Java API. You can perform radio signal processing and manipulation while enjoying a familiar Java programming experience.


## How to use

```java
package com.suricatedevlab.jsdr;

public class Main {
    public static void main(String[] args) {
        Driver driver = DriverManager.getDriver("RTL-SDR");
        try(Device device = driver.getDevice(0)){
            String name = device.getName();
            TunerStatement statement = device.createTunerStatement();
            statement.setCenterFrequency(1090000000);
            statement.setCorrectionFrequency(5);
            statement.setRate(2000000);
            SampleSet sampleSet = statement.tune();
            while(sampleSet.next()) {

            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello, World!");
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
- Add documentation

## Acknowledgements

 - [Rtl-sdr Wiki](https://osmocom.org/projects/rtl-sdr/wiki/Rtl-sdr)
 - [Rtl-sdr GIT repository](https://github.com/osmocom/rtl-sdr)

## License

[GNU AGPLv3](https://choosealicense.com/licenses/agpl-3.0/)

