package com.suricatedevlab.jsdr;

public interface TunerSample extends AutoCloseable {

   interface ReadAsyncCallback {
        void onReceive(byte[] data);
   }

    byte[] readSync(int bufferSize);

   void readAsync(ReadAsyncCallback callback);
}
