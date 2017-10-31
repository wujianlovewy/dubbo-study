package cn.edu.wj.netty.day1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class DemoServer {

	private int port;
	
	public DemoServer(int port){this.port = port;}
	
	public void start(){
		 EventLoopGroup bossGroup = new NioEventLoopGroup(1);  
	        EventLoopGroup workerGroup = new NioEventLoopGroup();  
	        try {  
	            ServerBootstrap sbs = new ServerBootstrap().group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
	                    .childHandler(new ChannelInitializer<SocketChannel>() {  
	                          
	                        protected void initChannel(SocketChannel ch) throws Exception {  
	                            ch.pipeline().addLast("decoder", new StringDecoder());  
	                            ch.pipeline().addLast("encoder", new StringEncoder());  
	                            ch.pipeline().addLast(new DemoServerHandler());  
	                        };  
	                          
	                    }).option(ChannelOption.SO_BACKLOG, 128)     
	                    .childOption(ChannelOption.SO_KEEPALIVE, true);  
	             // 绑定端口，开始接收进来的连接  
	             ChannelFuture future = sbs.bind("127.0.0.1",port).sync();    
	               
	             System.out.println("Server start listen at " + port );  
	             future.channel().closeFuture().sync();  
	        } catch (Exception e) {  
	            bossGroup.shutdownGracefully();  
	            workerGroup.shutdownGracefully();  
	        }  
	}
	
	public static void main(String[] args) throws Exception {  
        new DemoServer(9999).start();  
    }  
	
}
