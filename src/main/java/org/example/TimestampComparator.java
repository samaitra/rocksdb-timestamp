package org.example;

import org.rocksdb.AbstractComparator;
import org.rocksdb.ComparatorOptions;

import java.nio.ByteBuffer;

public class TimestampComparator extends AbstractComparator {

    private final int timeStampSize = 8;

    protected TimestampComparator(ComparatorOptions copt) {
        super(copt);
    }
    @Override
    public String name() {
        return "TimestampComparator";
    }

    @Override
    public int compare(ByteBuffer byteBuffer1, ByteBuffer byteBuffer2) {
        // need to figure out to extract timestamp from ByteBuffer
        System.out.println("byte buffer 1 "+byteBuffer1.toString());
        System.out.println("byte buffer 2 "+byteBuffer2.toString());

        return byteBuffer1.compareTo(byteBuffer2);
    }
}
