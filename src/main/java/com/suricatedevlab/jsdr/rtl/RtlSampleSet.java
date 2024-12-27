package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;
import com.suricatedevlab.jsdr.SampleSet;

class RtlSampleSet implements SampleSet {

    private final RtlTunerStatement statement;
    private PointerByReference nRead;

    public RtlSampleSet(RtlTunerStatement statement) {
        this.statement = statement;
    }

    @Override
    public byte[] readSync(int bufferSize) {
        statement.getDevice().getNativeLibrary().rtlsdr_reset_buffer(statement.getDevice().getHandle().getValue());
        byte[] result = new byte[bufferSize];
        int operationResult = statement.getDevice().getNativeLibrary().rtlsdr_read_sync(statement.getDevice().getHandle().getValue(),
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
