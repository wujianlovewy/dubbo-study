package cn.edu.wj.rpc.demo;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * netty传输decoder
 * @author jwu
 *
 */
public class RpcDecoder extends ByteToMessageDecoder {

	private Class<?> genericClass;

    public RpcDecoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }
	
	@Override
	protected void decode(ChannelHandlerContext channel, ByteBuf in,
			List<Object> out) throws Exception {
		 if (in.readableBytes() < 4) {
	            return;
	        }
	        in.markReaderIndex();
	        int dataLength = in.readInt();
	        if (dataLength < 0) {
	        	channel.close();
	        }
	        if (in.readableBytes() < dataLength) {
	            in.resetReaderIndex();
	            return;
	        }
	        byte[] data = new byte[dataLength];
	        in.readBytes(data);
	        Object obj = SerializationUtil.deserialize(data, genericClass);
	        out.add(obj);
	}

}
