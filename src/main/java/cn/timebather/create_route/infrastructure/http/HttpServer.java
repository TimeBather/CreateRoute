package cn.timebather.create_route.infrastructure.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.util.function.Supplier;

public class HttpServer {

    public final Supplier<JsonWsHttpHandler> handlerConstructor;
    public EventLoopGroup leader;

    public EventLoopGroup worker;

    public Channel channel;

    public HttpServer(Supplier<JsonWsHttpHandler> handlerConstructor){
        this.handlerConstructor = handlerConstructor;
    }

    public void up(){
        this.leader = new NioEventLoopGroup(1);
        this.worker = new NioEventLoopGroup();
        ServerBootstrap bs = new ServerBootstrap();
        bs.group(leader,worker)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new HttpRequestDecoder());
                        pipeline.addLast(new HttpResponseEncoder());
                        pipeline.addLast(new HttpObjectAggregator(1024*1024));
                        pipeline.addLast(handlerConstructor.get());
                    }
                });

        try{
            this.channel = bs.bind(8111).sync().channel();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    boolean isUp(){
        return this.leader != null && this.worker != null;
    }

    public void down(){
        if(!isUp())
            return;
        try{
            this.leader.shutdownGracefully().sync();
            this.worker.shutdownGracefully().sync();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }


}
