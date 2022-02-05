package org.example;

import org.rocksdb.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class MyTimeStampApp {
    public static void main(String[] args) {

        RocksDB.loadLibrary();

        ComparatorOptions comparatorOptions = new ComparatorOptions();

        try (final Options options = new Options().setCreateIfMissing(true)) {
            options.setComparator(new TimestampComparator(comparatorOptions));
            try (final RocksDB db = RocksDB.open(options, "/tmp/db")) {
                byte[] key1 = "1".getBytes(StandardCharsets.UTF_8);
                byte[] key2 = "2".getBytes(StandardCharsets.UTF_8);
                byte[] key3 = "3".getBytes(StandardCharsets.UTF_8);
                byte[] key4 = "4".getBytes(StandardCharsets.UTF_8);

                byte[] value1 = "old value1".getBytes(StandardCharsets.UTF_8);
                byte[] value2 = "old value2".getBytes(StandardCharsets.UTF_8);
                byte[] value3 = "new value3".getBytes(StandardCharsets.UTF_8);
                byte[] value4 = "new value4".getBytes(StandardCharsets.UTF_8);

                WriteOptions writeOptions = new WriteOptions();

                Long now = Instant.now().toEpochMilli();

                writeOptions.setTimestamp(new Slice(String.valueOf(now)));
                ReadOptions readOptions = new ReadOptions();
                readOptions.setIterStartTs(new Slice(String.valueOf(now)));
                readOptions.setTimestamp(new Slice(String.valueOf(now+30000L)));


                db.put(writeOptions, key1, value1);
                db.put(writeOptions, key2,value2);

                Thread.sleep(60000);

                writeOptions.setTimestamp(new Slice(String.valueOf(Instant.now().toEpochMilli())));

                db.put(writeOptions, key3, value3);
                db.put(writeOptions, key4,value4);

                RocksIterator itr = db.newIterator(readOptions);
                itr.seekToFirst();

                System.out.println("New values should not print here");

                while (itr.isValid()) {
                    System.out.println("Value "+new String(itr.value()));
                    itr.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
