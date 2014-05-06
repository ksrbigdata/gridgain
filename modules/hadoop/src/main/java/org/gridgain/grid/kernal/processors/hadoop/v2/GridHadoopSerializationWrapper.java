/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.kernal.processors.hadoop.v2;

import org.apache.hadoop.io.serializer.*;
import org.gridgain.grid.*;
import org.gridgain.grid.hadoop.*;
import java.io.*;

import org.jetbrains.annotations.*;

/**
 * The wrapper around external serializer.
 */
public class GridHadoopSerializationWrapper<T> implements GridHadoopSerialization {
    /** External serializer - writer */
    private final Serializer<T> serializer;

    /** External serializer - reader */
    private final Deserializer<T> deserializer;

    /** */
    private DataOutput currOut;

    /** */
    private DataInput currIn;

    /**
     * @param serialization External serializer to wrap.
     * @param cls The class to serialize.
     */
    public GridHadoopSerializationWrapper(Serialization<T> serialization, Class<T> cls) throws GridException {
        serializer = serialization.getSerializer(cls);
        deserializer = serialization.getDeserializer(cls);

        try {
            OutputStream outStream = new OutputStream() {
                /** {@inheritDoc} */
                @Override public void write(int b) throws IOException {
                    currOut.write(b);
                }

                /** {@inheritDoc} */
                @Override public void write(byte[] b, int off, int len) throws IOException {
                    currOut.write(b, off, len);
                }
            };

            serializer.open(outStream);

            InputStream inStream = new InputStream() {
                /** {@inheritDoc} */
                @Override public int read() throws IOException {
                    return currIn.readUnsignedByte();
                }
            };

            deserializer.open(inStream);
        }
        catch (IOException e) {
            throw new GridException(e);
        }
    }

    /** {@inheritDoc} */
    @Override public void write(DataOutput out, Object obj) throws GridException {
        try {
            currOut = out;

            serializer.serialize((T)obj);
        }
        catch (IOException e) {
            throw new GridException(e);
        }
    }

    /** {@inheritDoc} */
    @Override public Object read(DataInput in, @Nullable Object obj) throws GridException {
        try {
            currIn = in;

            return deserializer.deserialize((T)obj);
        }
        catch (IOException e) {
            throw new GridException(e);
        }
    }

    /** {@inheritDoc} */
    @Override public void close() throws GridException {
//        try {
//            serializer.close();
//            deserializer.close();
//        }
//        catch (IOException e) {
//            throw new GridException(e);
//        }
    }
}
