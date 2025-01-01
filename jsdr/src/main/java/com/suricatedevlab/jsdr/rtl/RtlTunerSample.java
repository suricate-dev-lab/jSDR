package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.suricatedevlab.jsdr.SdrException;
import com.suricatedevlab.jsdr.TunerSample;

class RtlTunerSample implements TunerSample {

    private final RtlTunerDefinition definition;
    private PointerByReference nRead;

    public RtlTunerSample(RtlTunerDefinition definition) {
        this.definition = definition;
    }

    @Override
    public byte[] readSync(int bufferSize) throws SdrException {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("buffer size <= 0");
        }
        byte[] result = new byte[bufferSize];
        resetBuffer();
        int operationResult = definition.getDevice().getNativeLibrary().rtlsdr_read_sync(definition.getDevice().getHandle().getValue(),
                                result, bufferSize, nRead);
        if (operationResult < 0) {
            throw new SdrException("Failed to perform read sync");
        }
        result = ByteUtils.trim(result);
        if (result.length == 0) {
            result = null;
        }
        return result;
    }

    @Override
    public void readAsync(ReadAsyncCallback callback, int bufferNumber, int bufferSize) throws SdrException {

        if (callback == null) {
            throw new IllegalArgumentException("Callback is null");
        }

        resetBuffer();

        RtlNativeLibrary.RTLSDRReadAsyncCallback nativeCallback = new RtlNativeLibrary.RTLSDRReadAsyncCallback() {
            @Override
            public void invoke(Pointer buf, int length, Pointer ctx) {
                byte[] data = buf.getByteArray(0, length);
                callback.onReceive(data);
            }
        };

        // Call the async read function
        int result = definition.getDevice().getNativeLibrary().rtlsdr_read_async(definition.getDevice().getHandle().getValue(),
                nativeCallback, null, bufferNumber, bufferSize);

        if (result < 0) {
            throw new SdrException("Failed to perform read async");
        }
    }

    @Override
    public void close() throws Exception {
        definition.close();
    }

    private void resetBuffer() {
        definition.getDevice().getNativeLibrary().rtlsdr_reset_buffer(definition.getDevice().getHandle().getValue());
    }
}
