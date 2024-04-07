package cn.timebather.create_route.content.http;

import cn.timebather.create_route.infrastructure.http.JsonWsHttpHandler;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class RouteModHttpTransformer implements JsonWsHttpHandler.Transformer {
    @Override
    public HttpResponse handleGet(FullHttpRequest request, String path) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,Unpooled.copiedBuffer("Hello", CharsetUtil.UTF_8));
    }

    @Override
    public HttpResponse handlePost(FullHttpRequest request, String path, JsonObject object) {
        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,HttpResponseStatus.OK,Unpooled.copiedBuffer(object.toString(), CharsetUtil.UTF_8));
    }
}
