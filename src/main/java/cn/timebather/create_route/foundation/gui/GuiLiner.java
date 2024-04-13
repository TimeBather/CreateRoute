package cn.timebather.create_route.foundation.gui;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import org.joml.Math;
import org.joml.Matrix4f;

import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class GuiLiner {
    public static Function<Color,BiConsumer<VertexConsumer,Matrix4f>> line(float x1, float y1, float x2, float y2, float width){
        float k = - (x1 - x2) / (y1 - y2); // k1 = -1/k
        float offsetX = width * (float) Mth.fastInvSqrt(1f+Math.abs(k)); // sqrt(1+k1^2) * oX = width , oX = width / sqrt(1+k1^2)
        float offsetY = k * offsetX;
        if(y1 == y2 || !Math.isFinite(offsetX)){
            offsetX = 0;
            offsetY = - width;
        }

        float vertexX1 = x1 - offsetX,
              vertexX2 = x1 + offsetX,
              vertexX3 = x2 - offsetX,
              vertexX4 = x2 + offsetX;
        float vertexY1 = y1 - offsetY,
              vertexY2 = y1 + offsetY,
              vertexY3 = y2 - offsetY,
              vertexY4 = y2 + offsetY;

        Function<Color,BiConsumer<VertexConsumer,Matrix4f>> colorConsumer = (color)->{
            int r = color.getRed(),g=color.getGreen(),b=color.getBlue(),a=color.getAlpha();
            return (consumer,matrix4f)->{
                consumer.vertex(matrix4f,vertexX1,vertexY1,0).color(r,g,b,a).endVertex();
                consumer.vertex(matrix4f,vertexX3,vertexY3,0).color(r,g,b,a).endVertex();
                consumer.vertex(matrix4f,vertexX4,vertexY4,0).color(r,g,b,a).endVertex();
                consumer.vertex(matrix4f,vertexX2,vertexY2,0).color(r,g,b,a).endVertex();
            };
        };

        return colorConsumer;
    }

    public static BiConsumer<VertexConsumer,Matrix4f> line(float x1, float y1, float x2, float y2, float width, Color color){
        if(y1 > y2){
            float temp;
            temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }
        return line(x1, y1, x2, y2, width).apply(color);
    }

    public static BiConsumer<VertexConsumer,Matrix4f> line(float x1, float y1, float x2, float y2, float width, Supplier<Color> colorSupplier){
        return (vertex,matrix) -> line(x1, y1, x2, y2, width).apply(colorSupplier.get()).accept(vertex,matrix);
    }

    public static void batchDraw(MultiBufferSource bufferSource, Matrix4f pose, Collection<BiConsumer<VertexConsumer,Matrix4f>> lines){
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.gui());
        for (BiConsumer<VertexConsumer, Matrix4f> line : lines) {
            line.accept(consumer,pose);
        }
    }
}
