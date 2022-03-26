package net.mehvahdjukaar.randomium.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.mehvahdjukaar.randomium.entity.MovingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ForgeHooksClient;

import java.util.Random;

public class MovingBlockEntityRenderer extends EntityRenderer<MovingBlockEntity> {


    public MovingBlockEntityRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Override
    public void render(MovingBlockEntity entity, float val, float partialTicks, PoseStack matrixStack, MultiBufferSource buffer, int light) {
        BlockState blockstate = entity.getBlockState();

        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level world = entity.level;
            if (blockstate != world.getBlockState(entity.blockPosition())) {
                matrixStack.pushPose();
                BlockPos blockpos = new BlockPos(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
                matrixStack.translate(-0.5D, 0.0D, -0.5D);
                BlockRenderDispatcher modelRenderer = Minecraft.getInstance().getBlockRenderer();
                for (RenderType type : RenderType.chunkBufferLayers()) {
                    //TODO. move to lib
                    if (ItemBlockRenderTypes.canRenderInLayer(blockstate, type)) {
                        ForgeHooksClient.setRenderType(type);
                        modelRenderer.getModelRenderer().tesselateBlock(world, modelRenderer.getBlockModel(blockstate), blockstate, blockpos, matrixStack,
                                buffer.getBuffer(type), false, new Random(), blockstate.getSeed(entity.getStartPos()), OverlayTexture.NO_OVERLAY);
                    }
                }
                ForgeHooksClient.setRenderType(null);
                matrixStack.popPose();
                super.render(entity, val, partialTicks, matrixStack, buffer, light);
            }
        }
    }

    @Override
    public ResourceLocation getTextureLocation(MovingBlockEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

}
