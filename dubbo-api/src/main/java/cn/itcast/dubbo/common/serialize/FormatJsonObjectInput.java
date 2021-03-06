package cn.itcast.dubbo.common.serialize;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.utils.PojoUtils;
import com.alibaba.fastjson.JSON;

/**
 * JsonObjectInput
 *
 * @author william.liangf
 */
public class FormatJsonObjectInput implements ObjectInput {

    private final BufferedReader reader;

    public FormatJsonObjectInput(InputStream in) {
        this(new InputStreamReader(in));
    }

    public FormatJsonObjectInput(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public boolean readBool() throws IOException {
        try {
            return readObject(boolean.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte readByte() throws IOException {
        try {
            return readObject(byte.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public short readShort() throws IOException {
        try {
            return readObject(short.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public int readInt() throws IOException {
        try {
            return readObject(int.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public long readLong() throws IOException {
        try {
            return readObject(long.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public float readFloat() throws IOException {
        try {
            return readObject(float.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public double readDouble() throws IOException {
        try {
            return readObject(double.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public String readUTF() throws IOException {
        try {
            return readObject(String.class);
        } catch (ClassNotFoundException e) {
            throw new IOException(e.getMessage());
        }
    }

    public byte[] readBytes() throws IOException {
        return readLine().getBytes();
    }

    public Object readObject() throws IOException, ClassNotFoundException {
        String json = readLine();
        return JSON.parse(json);
    }

    public <T> T readObject(Class<T> cls) throws IOException, ClassNotFoundException {
        String json = readLine();
        return JSON.parseObject(json, cls);
    }

    @SuppressWarnings("unchecked")
    public <T> T readObject(Class<T> cls, Type type) throws IOException, ClassNotFoundException {
        Object value = readObject(cls);
        return (T) PojoUtils.realize(value, cls, type);
    }

    private String readLine() throws IOException, EOFException {
        String line = reader.readLine();
        if (line == null || line.trim().length() == 0) throw new EOFException();
        return line;
    }

}