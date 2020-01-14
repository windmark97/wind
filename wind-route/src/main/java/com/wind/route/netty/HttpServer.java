package com.wind.route.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: HttpServer
 * @Description: <br>
 * @DATE: 2019/8/28 10:22
 * @Author: hyj
 * @Version: 1.0
 */
@Slf4j
public class HttpServer {
    private int port;

    public HttpServer(Integer port) {
        this.port = port;
    }

    public void start(){
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServletChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 绑定端口，开始接收进来的连接
            ChannelFuture f = b.bind(port).sync();
            log.info("ROUTE启动成功,端口:{}", port);
            // 等待服务器 socket 关闭 。
            f.channel().closeFuture().sync();
        } catch (Exception e){
            log.error("ROUTE启动失败!",e);
        }finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }


}
