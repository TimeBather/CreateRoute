package cn.timebather.create_route.infrastructure.http;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.concurrent.GenericFutureListener;

public class JsonWsHttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    public static interface Transformer{
        HttpResponse handleGet(FullHttpRequest request,String path);
        HttpResponse handlePost(FullHttpRequest request,String path,JsonObject object);
    }

    Transformer transformer;

    JsonWsHttpHandler(Transformer transformer){
        this.transformer = transformer;
    }

    public static JsonWsHttpHandler of(Transformer transformer){
        return new JsonWsHttpHandler(transformer);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, FullHttpRequest request) throws Exception {
        if (!request.decoderResult().isSuccess()) {
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST,
                            context.alloc().buffer(0));
            context.write(response);
            return;
        }

        if (request.headers().contains(HttpHeaderNames.UPGRADE, HttpHeaderValues.WEBSOCKET, true)) {
            context.fireChannelRead(request.retain());
            return;
        }

        if(request.method() == HttpMethod.GET){
            context.writeAndFlush(transformer.handleGet(request,request.uri()))
                    .addListener(ChannelFutureListener.CLOSE);
            return;
        }

        JsonElement jsonElement = JsonParser.parseString(request.content().toString());

        if(!(jsonElement instanceof JsonObject jsonObject)){
            FullHttpResponse response =
                    new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST);
            context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            return;
        }

        context.writeAndFlush(this.transformer.handlePost(request,request.uri(),jsonObject))
                .addListener(ChannelFutureListener.CLOSE);

    }
}
