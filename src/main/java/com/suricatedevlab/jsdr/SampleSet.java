package com.suricatedevlab.jsdr;

public interface SampleSet extends AutoCloseable {

   interface ReadAsyncCallback {
        void onReceive(byte[] data);
   }

   void readAsync(ReadAsyncCallback callback);
}
