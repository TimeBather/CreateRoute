package cn.timebather.create_route.content.station.screen.board_port;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class BoardPortScreenRenderer extends GeoBlockRenderer<BoardPortScreenBlockEntity>{
    private final Font font;

    public BoardPortScreenRenderer(BlockEntityRendererProvider.Context context){
        super(new BoardPortScreenModel());
        font = context.getFont();
    }


    @Override
    public void defaultRender(PoseStack poseStack, BoardPortScreenBlockEntity boardPortScreenBlockEntity, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack,boardPortScreenBlockEntity,bufferSource,renderType,buffer,yaw,partialTick,packedLight);
        poseStack.pushPose();
        poseStack.translate(0.5,0.5,0.5);
        poseStack.mulPose(boardPortScreenBlockEntity.getBlockState().getValue(BoardPortScreen.FACING).getRotation());
        poseStack.mulPose(new Quaternionf().rotationXYZ(0,1.5707964F,1.5707964F));
        poseStack.translate(-0.5,-0.5,-0.08);
        poseStack.pushPose();
        poseStack.scale(0.08f,0.08f,0.08f);
        Matrix4f pose = poseStack.last().pose();
        int lightLevel = getLightLevel(boardPortScreenBlockEntity.getLevel(),boardPortScreenBlockEntity.getBlockPos().north());
        renderTitleText(pose,10.5f, boardPortScreenBlockEntity.leftPortId, bufferSource,lightLevel);
        renderTitleText(pose,91,boardPortScreenBlockEntity.rightPortId, bufferSource,lightLevel);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(1.5f,0.06f,0);
        poseStack.scale(0.02f,0.02f,0.02f);
        pose = poseStack.last().pose();
        renderScreenText(pose,10,3,"  车次    开点    站台    车厢号   地标颜色    状态",bufferSource,0xFFFF0000);

        renderTrainRecord(pose,bufferSource,"G25","7:19","13","1-14",HintColor.WHITE,TrainStatus.STOPPED,1);
        renderTrainRecord(pose,bufferSource,"G1","7:30","12","1-8",HintColor.RED,TrainStatus.CHECKING,2);
        renderTrainRecord(pose,bufferSource,"G1145","12:30","13","1-14",HintColor.BLUE,TrainStatus.WAITING,3);

        renderScreenText(pose,120 - font.width("列车开车前15分钟开始检票，开车前4分钟停止检票") * 0.5f,57,"列车开车前15分钟开始检票，开车前4分钟停止检票",bufferSource,0xFFFF0000);
        poseStack.popPose();

        poseStack.popPose();
    }

    private void renderEmptyText(){

    }

    private void renderTitleText(Matrix4f pose,float left, String text, MultiBufferSource bufferSource,int lightLevel){
        float f2 = left - (float)(font.width(text) * 0.5);
        float h2 = 10f - (float)(font.lineHeight * 0.5);
        font.drawInBatch(Component.literal(text),f2,h2,-1,false,pose,bufferSource, Font.DisplayMode.NORMAL,0,lightLevel);
    }

    private void renderScreenText(Matrix4f pose,float left,float top, String text, MultiBufferSource bufferSource,int color){
        font.drawInBatch(Component.literal(text),left,top,color,false,pose,bufferSource, Font.DisplayMode.NORMAL,0,0xF000F0);
    }
    private static int getLightLevel(Level level, BlockPos pos){
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight,sLight);
    }

    enum HintColor{
        RED("红色",0xFFFF0000),
        GREEN("绿色",0xFF00FF00),
        BLUE("蓝色",0xFF0000FF),
        WHITE("白色", 0xFFFFFFFF);
        private final String text;
        private final int color;

        HintColor(String text, int color){
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return text;
        }

        public int getColor() {
            return color;
        }
    }

    enum TrainStatus{
        WAITING("正在候车",0xFFFFFF00),
        CHECKING("正在检票",0xFF00FF00),
        STOPPED("停止检票",0xFFFF0000);

        private final String text;
        private final int color;

        TrainStatus(String text,int color){
            this.text = text;
            this.color = color;
        }

        public String getText() {
            return text;
        }

        public int getColor() {
            return color;
        }
    }

    private void renderCenteredScreenText(Matrix4f pose,float left,float top, String text, MultiBufferSource bufferSource,int color){
        renderScreenText(pose, left - font.width(text) * 0.5f, top, text, bufferSource, color);
    }
    private void renderTrainRecord(Matrix4f pose, MultiBufferSource bufferSource, String trainId, String dispatchTime, String platform, String contraptionNumbers, HintColor color,TrainStatus status,int i){
        renderCenteredScreenText(pose,26,12 * i+3,trainId,bufferSource,0xFFFFFF00);
        renderCenteredScreenText(pose,60,12 * i+3,dispatchTime,bufferSource,0xFFFFFF00);
        renderCenteredScreenText(pose,94,12 * i+3,platform,bufferSource,0xFFFFFF00);
        renderCenteredScreenText(pose,133,12 * i+3,contraptionNumbers,bufferSource,0xFFFFFF00);
        renderCenteredScreenText(pose,178,12 * i+3,color.getText(),bufferSource, color.getColor());
        renderCenteredScreenText(pose,220,12 * i+3,status.getText(),bufferSource, status.getColor());
    }
}
