/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop;

import com.google.common.collect.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.gridgain.grid.*;
import org.gridgain.grid.hadoop.*;
import org.gridgain.grid.kernal.processors.hadoop.v2.*;
import org.gridgain.grid.kernal.processors.hadoop.shuffle.*;
import org.gridgain.grid.util.io.*;
import org.gridgain.grid.util.offheap.unsafe.*;
import org.gridgain.grid.util.typedef.*;
import org.gridgain.testframework.junits.common.*;

import java.io.*;
import java.util.*;

import static org.gridgain.grid.util.offheap.unsafe.GridUnsafeMemory.*;

/**
 *
 */
public class GridHadoopMultimapSelftest extends GridCommonAbstractTest {
    /** */
    public void testMapSimple() throws GridException, IOException {
        GridUnsafeMemory mem = new GridUnsafeMemory(0);

//        mem.listen(new GridOffHeapEventListener() {
//            @Override public void onEvent(GridOffHeapEvent evt) {
//                if (evt == GridOffHeapEvent.ALLOCATE)
//                    U.dumpStack();
//            }
//        });

        Random rnd = new Random();

        int mapSize = 16 << rnd.nextInt(6);

        Job job = Job.getInstance();

        job.setMapOutputKeyClass(IntWritable.class);
        job.setMapOutputValueClass(IntWritable.class);

        GridHadoopMultimap m = new GridHadoopMultimap(new GridHadoopV2Job(new GridHadoopJobId(UUID.randomUUID(), 10),
            new GridHadoopDefaultJobInfo(job.getConfiguration())), mem, mapSize);

        GridHadoopMultimap.Adder a = m.startAdding();

        Multimap<Integer, Integer> mm = ArrayListMultimap.create();
        Multimap<Integer, Integer> vis = ArrayListMultimap.create();

//        for (int i = 0, vals = 4 * mapSize + rnd.nextInt(25); i < vals; i++) {
//            int key = rnd.nextInt(mapSize);
//            int val = rnd.nextInt();
//
//            a.add(new IntWritable(key), new IntWritable(val));
//            mm.put(key, val);
//
//            X.println("k: " + key + " v: " + val);
//
//            check(m, mm, vis);
//        }

        a.add(new IntWritable(10), new IntWritable(2));
        mm.put(10, 2);
        check(m, mm, vis);

        a.close();

        X.println("Alloc: " + mem.allocatedSize());

        m.close();

        assertEquals(0, mem.allocatedSize());
    }

    private void check(GridHadoopMultimap m, Multimap<Integer, Integer> mm,
        final Multimap<Integer, Integer> vis) throws GridException {
        final GridHadoopTaskInput in = m.input();

        Map<Integer, Collection<Integer>> mmm = mm.asMap();

        int keys = 0;

        while (in.next()) {
            keys++;

            IntWritable k = (IntWritable)in.key();

            assertNotNull(k);

            LinkedList<Integer> vs = new LinkedList<>();

            Iterator<?> it = in.values();

            while (it.hasNext())
                vs.addFirst(((IntWritable) it.next()).get());

            Collection<Integer> exp = mmm.get(k.get());

            assertEquals(exp, vs);
        }

        assertEquals(mmm.size(), keys);

        X.println("keys: " + keys);

        // Check visitor.

        final byte[] buf = new byte[4];

        final GridUnsafeDataInput dataInput = new GridUnsafeDataInput();

        m.visit(false, new GridHadoopMultimap.Visitor() {
            /** */
            IntWritable key = new IntWritable();

            /** */
            IntWritable val = new IntWritable();

            @Override public void onKey(long keyPtr, int keySize) {
                read(keyPtr, keySize, key);
            }

            @Override public void onValue(long valPtr, int valSize) {
                read(valPtr, valSize, val);

                vis.put(key.get(), val.get());
            }

            private void read(long ptr, int size, IntWritable w) {
                assert size == 4 : size;

                UNSAFE.copyMemory(null, ptr, buf, BYTE_ARR_OFF, size);

                dataInput.bytes(buf, size);

                try {
                    w.readFields(dataInput);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

//        X.println("vis: " + vis);

        assertEquals(mm, vis);
    }
}
