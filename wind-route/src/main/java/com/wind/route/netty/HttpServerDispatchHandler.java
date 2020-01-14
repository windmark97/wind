package com.wind.route.netty;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @ClassName: HttpServerDispatchHandler
 * @Description: <br>
 * @DATE: 2019/8/28 14:47
 * @Author: hyj
 * @Version: 1.0
 */
public class HttpServerDispatchHandler implements Runnable {
    private MockMvc mockMvc;
    private ChannelHandlerContext ctx;
    private String clientIp;
    private String requestUri;
    private String requestData;

    private FullHttpResponse nettyResponse;

    public HttpServerDispatchHandler(MockMvc mockMvc, ChannelHandlerContext ctx, String clientIp, String requestUri, String requestData) {
        this.mockMvc = mockMvc;
        this.ctx = ctx;
        this.clientIp = clientIp;
        this.requestUri = requestUri;
        this.requestData = requestData;
        this.nettyResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
    }

    @Override
    public void run() {
        try {
            MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post(requestUri)
                    .header("Orig-Ip", clientIp)
                    .header("Content-Type", "application/json")
                    .accept("application/json;charset=UTF-8").content(requestData);

            MockHttpServletResponse mockResponse = mockMvc.perform(mockRequest).andReturn().getResponse();
            nettyResponse.setStatus(HttpResponseStatus.valueOf(mockResponse.getStatus()));
            nettyResponse.content().writeBytes(mockResponse.getContentAsByteArray());
            for (String name : mockResponse.getHeaderNames()) {
                nettyResponse.headers().set(name, mockResponse.getHeaderValue(name));
            }
        } catch (Exception e) {
            nettyResponse.setStatus(HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
        ctx.writeAndFlush(nettyResponse).addListener(ChannelFutureListener.CLOSE);
    }

}
