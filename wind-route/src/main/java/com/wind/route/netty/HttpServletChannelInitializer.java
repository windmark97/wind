package com.wind.route.netty;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.wind.route.netty.controller.SpringMvcController;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

/**
 * @ClassName: HttpServletChannelInitializer
 * @Description: <br>
 * @DATE: 2019/8/28 15:55
 * @Author: hyj
 * @Version: 1.0
 */
public class HttpServletChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * 线程数量
     */
    private static Integer nThread = 5;

    private static EventExecutorGroup handlersExecutor = new DefaultEventExecutorGroup(nThread);

    private static MockMvc mockMvc;

    public HttpServletChannelInitializer() {


        List<Object> controllerObjectList = SpringMvcController.getInstance().getControllerObjectList();

        FastJsonHttpMessageConverter messageConverter = new FastJsonHttpMessageConverter();
        messageConverter.getFastJsonConfig().setSerializerFeatures(SerializerFeature.WriteMapNullValue);
        mockMvc = MockMvcBuilders.standaloneSetup(controllerObjectList.toArray())
                .setMessageConverters(messageConverter).build();

    }

    @Override
    public void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();


        /* pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("encoder", new HttpResponseEncoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(2147483647));
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
        pipeline.addLast("deflater", new HttpContentCompressor());
        pipeline.addLast("handler", new HttpRequestHandlers(servlet));*/


        pipeline.addLast(new HttpServerCodec());
        //1M
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(handlersExecutor, new HttpServerInboundHandler(mockMvc));
    }

}
