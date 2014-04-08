/* @java.file.header */

/*  _________        _____ __________________        _____
 *  __  ____/___________(_)______  /__  ____/______ ____(_)_______
 *  _  / __  __  ___/__  / _  __  / _  / __  _  __ `/__  / __  __ \
 *  / /_/ /  _  /    _  /  / /_/ /  / /_/ /  / /_/ / _  /  _  / / /
 *  \____/   /_/     /_/   \_,__/   \____/   \__,_/  /_/   /_/ /_/
 */

package org.gridgain.grid.marshaller;

import org.gridgain.grid.util.*;
import org.gridgain.grid.util.typedef.internal.*;

import java.util.*;

/**
 * Grid marshaller test bean.
 */
public class GridMarshallerTestBean extends GridMarshallerResourceBean {
    /** */
    private Object objField;

    /** */
    private String strField;

    /** */
    private long longField = -1;

    /** */
    private GridByteArrayList buf;

    /** Array of classes. */
    private Class<?>[] clss;

    /** */
    private GridMarshallerExternalizableBean extBean = new GridMarshallerExternalizableBean();

    /**
     * @param objField Object field.
     * @param strField String field.
     * @param longField Long field.
     * @param buf Nested byte buffer.
     * @param clss Array of classes.
     */
    public GridMarshallerTestBean(Object objField, String strField, long longField, GridByteArrayList buf,
        Class<?>... clss) {
        this.objField = objField;
        this.strField = strField;
        this.longField = longField;
        this.buf = buf;
        this.clss = clss;
    }

    /**
     * Gets object field.
     *
     * @return Object field.
     */
    Object getObjectField() {
        return objField;
    }

    /**
     * Gets string field.
     *
     * @return String field.
     */
    String getStringField() {
        return strField;
    }

    /**
     * Gets long field.
     *
     * @return Long field.
     */
    long getLongField() {
        return longField;
    }

    /**
     * Gets nested byte buffer.
     *
     * @return Nested byte buffer.
     */
    GridByteArrayList getBuffer() {
        return buf;
    }

    /**
     * Gets externalizable object.
     *
     * @return Externalizable object.
     */
    GridMarshallerExternalizableBean getExternalizableBean() {
        return extBean;
    }

    /**
     * @return Array of classes.
     */
    public Class<?>[] getClasses() {
        return clss;
    }

    /** {@inheritDoc} */
    @Override public int hashCode() {
        int prime = 31;

        int res = 1;

        res = prime * res + ((buf == null) ? 0 : buf.hashCode());
        res = prime * res + (int) (longField ^ (longField >>> 32));
        res = prime * res + ((objField == null) ? 0 : objField.hashCode());
        res = prime * res + ((strField == null) ? 0 : strField.hashCode());

        return res;
    }

    /** {@inheritDoc} */
    @Override public boolean equals(Object obj) {
        assert obj instanceof GridMarshallerTestBean;

        GridMarshallerTestBean other = (GridMarshallerTestBean)obj;

        return
            other.strField.equals(strField) &&
            other.longField == longField &&
            other.extBean.equals(extBean) &&
            Arrays.equals(other.buf.array(), buf.array()) &&
            other.buf.size() == buf.size() &&
            Arrays.equals(other.clss, clss);
    }

    /** {@inheritDoc} */
    @Override public String toString() {
        return S.toString(GridMarshallerTestBean.class, this);
    }
}