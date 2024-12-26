package com.suricatedevlab.jsdr.rtl;

import com.sun.jna.Pointer;
import com.suricatedevlab.jsdr.SampleSet;

class RtlSampleSet implements SampleSet {

    private static final int BUFFER_SIZE = 64;

    private final RtlTunerStatement statement;
    private byte[] buffer;
    public RtlSampleSet(RtlTunerStatement statement) {
        this.statement = statement;

    }

    @Override
    public boolean next() {
/*

RtlNativeLibrary.RTLSDRReadAsyncCallback callback = new RtlNativeLibrary.RTLSDRReadAsyncCallback() {
            @Override
            public void invoke(Pointer buf, int length, Pointer ctx) {
                System.out.println(buf);
            }
        };

        // Call the async read function
        int result = statement.getDevice().getNativeLibrary().rtlsdr_read_async(statement.getDevice().getHandle().getValue(), callback, null, 10, 16384);
        System.out.println(result);
 */

        byte[] buff = new byte[BUFFER_SIZE];
        int result = statement.getDevice().getNativeLibrary().rtlsdr_read_sync(statement.getDevice().getHandle().getValue(),
                buff, BUFFER_SIZE, 0);
        buffer = ByteUtils.trim(buff);
        return buffer.length > 0;
    }

    @Override
    public byte[] getBytes() {
        return buffer;
    }

    @Override
    public void close() throws Exception {
        statement.close();
    }
}
