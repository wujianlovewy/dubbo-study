package cn.edu.wj.rpc.demo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RpcServer implements ApplicationContextAware, InitializingBean {
	
	private ZookeeperRegister serviceRegistry;
	private String serverAddress;
	ApplicationContext context;
	
	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(RpcServer.class);
	private Map<String, Object> servicesMap = new HashMap<>(); // 存放接口名与服务对象之间的映射关系
	
	 public RpcServer(String serverAddress, ZookeeperRegister serviceRegistry) {
	        this.serverAddress = serverAddress;
	        this.serviceRegistry = serviceRegistry;
	    }

	@Override
	public void afterPropertiesSet() throws Exception {
		//启动netty服务端
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	    EventLoopGroup workerGroup = new NioEventLoopGroup();
	    try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel channel) throws Exception {
                        channel.pipeline()
                            .addLast(new RpcDecoder(RpcRequest.class)) // 将 RPC 请求进行解码（为了处理请求）
                            .addLast(new RpcEncoder(RpcResponse.class)) // 将 RPC 响应进行编码（为了返回响应）
                            .addLast(new RpcRequestHandler(servicesMap)); // 处理 RPC 请求
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

            String[] array = serverAddress.split(":");
            String host = array[0];
            int port = Integer.parseInt(array[1]);

            ChannelFuture future = bootstrap.bind(host, port).sync();
            logger.debug("server started on port {}", port);

            if (serviceRegistry != null) {
                serviceRegistry.register(serverAddress); // 注册服务地址
            }

           future.channel().closeFuture().sync();   // 关闭 RPC 服务器
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
	}

	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
		Map<String, Object> serviceBeanMap = context.getBeansWithAnnotation(RpcService.class); //获取RpcService注解的全部bean
		//服务接口存放
		if(MapUtils.isNotEmpty(serviceBeanMap)){
			for(Object serviceBean : serviceBeanMap.values()){
				String serviceName = serviceBean.getClass().getAnnotation(RpcService.class).value();
				servicesMap.put(serviceName, serviceBean);
			}
		}
	}

}
