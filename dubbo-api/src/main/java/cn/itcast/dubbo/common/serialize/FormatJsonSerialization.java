package cn.itcast.dubbo.common.serialize;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.serialize.ObjectInput;
import com.alibaba.dubbo.common.serialize.ObjectOutput;
import com.alibaba.dubbo.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FormatJsonSerialization implements Serialization {

    public byte getContentTypeId() {
        return 8;
    }

    public String getContentType() {
        return "text/json";
    }

    public ObjectOutput serialize(URL url, OutputStream output) throws IOException {
        return new FormatJsonObjectOutput(output);
    }

    public ObjectInput deserialize(URL url, InputStream input) throws IOException {
        return new FormatJsonObjectInput(input);
    }

}