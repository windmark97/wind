package com.wind.route.netty;

import com.alibaba.fastjson.JSONObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.net.InetSocketAddress;

/**
 * @ClassName: HttpServerInboundHandler
 * @Description: <br>
 * @DATE: 2019/8/28 14:43
 * @Author: hyj
 * @Version: 1.0
 */
@Slf4j
public class HttpServerInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {


    private MockMvc mockMvc;

    public HttpServerInboundHandler(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) {
        String requestData = request.content().toString(CharsetUtil.UTF_8);
        try {
            if (StringUtils.isNotBlank(requestData)) {
                JSONObject.parseObject(requestData);
            }
        } catch (Exception e) {
            log.error("JSON 格式非法：{}", requestData);
        }
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        String requestUri = request.uri().length() <= 1 ? "/private" : request.uri();
        ctx.executor().execute(new HttpServerDispatchHandler(mockMvc, ctx, clientIp, requestUri, requestData));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
