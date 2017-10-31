package cn.edu.wj.netty.day1;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class DemoClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("DemoClientHandler Active");  
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		 System.out.println("DemoClientHandler read Message:"+msg);  
	}
	
	 @Override  
     public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {  
         cause.printStackTrace();  
         ctx.close();  
     }  

}
