package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.suricatedevlab.jsdr.TunerSample;

class RtlTunerSample implements TunerSample {

    private final RtlTunerDefinition definition;
    private PointerByReference nRead;

    public RtlTunerSample(RtlTunerDefinition definition) {
        this.definition = definition;
    }

    @Override
    public byte[] readSync(int bufferSize) {
        definition.getDevice().getNativeLibrary().rtlsdr_reset_buffer(definition.getDevice().getHandle().getValue());
        byte[] result = new byte[bufferSize];
        int operationResult = definition.getDevice().getNativeLibrary().rtlsdr_read_sync(definition.getDevice().getHandle().getValue(),
                                result, bufferSize, nRead);
        if (operationResult < 0) {
            return null;
        }
        result = ByteUtils.trim(result);
        if (result.length == 0) {
            result = null;
        }
        return result;
    }

    @Override
    public void readAsync(ReadAsyncCallback callback) {

        if (callback == null) {
            throw new IllegalArgumentException("Callback is null");
        }

        definition.getDevice().getNativeLibrary().rtlsdr_reset_buffer(definition.getDevice().getHandle().getValue());

        RtlNativeLibrary.RTLSDRReadAsyncCallback nativeCallback = new RtlNativeLibrary.RTLSDRReadAsyncCallback() {
            @Override
            public void invoke(Pointer buf, int length, Pointer ctx) {
                byte[] data = buf.getByteArray(0, length);
                callback.onReceive(data);
            }
        };

        // Call the async read function
        int result = definition.getDevice().getNativeLibrary().rtlsdr_read_async(definition.getDevice().getHandle().getValue(),
                nativeCallback, null, 64, 64);
        System.out.println(result);
    }

    @Override
    public void close() throws Exception {
        definition.close();
    }
}
