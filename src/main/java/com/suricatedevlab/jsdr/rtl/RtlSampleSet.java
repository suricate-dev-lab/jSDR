package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Pointer;
import com.suricatedevlab.jsdr.SampleSet;

class RtlSampleSet implements SampleSet {

    private final RtlTunerStatement statement;

    public RtlSampleSet(RtlTunerStatement statement) {
        this.statement = statement;
    }

    @Override
    public void readAsync(ReadAsyncCallback callback) {

        if (callback == null) {
            throw new IllegalArgumentException("Callback is null");
        }

        statement.getDevice().getNativeLibrary().rtlsdr_reset_buffer(statement.getDevice().getHandle().getValue());

        RtlNativeLibrary.RTLSDRReadAsyncCallback nativeCallback = new RtlNativeLibrary.RTLSDRReadAsyncCallback() {
            @Override
            public void invoke(Pointer buf, int length, Pointer ctx) {

                byte[] data = buf.getByteArray(0, length);
                callback.onReceive(data);
            }
        };

        // Call the async read function
        int result = statement.getDevice().getNativeLibrary().rtlsdr_read_async(statement.getDevice().getHandle().getValue(),
                nativeCallback, null, 64, 64);
        System.out.println(result);
    }

    @Override
    public void close() throws Exception {
        statement.close();
    }
}
