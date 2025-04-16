package com.frogrilla.echoes.render.blockentity;

import com.frogrilla.echoes.common.block.entity.CrystalisationTableBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.AxisAngle4d;
import org.joml.Vector3d;

@Environment(EnvType.CLIENT)
public class CrystalisationTableRenderer implements BlockEntityRenderer<CrystalisationTableBlockEntity> {

    public CrystalisationTableRenderer(BlockEntityRendererFactory.Context ctx) {}
    @Override
    public void render(CrystalisationTableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        matrices.push();

        matrices.translate(0.5, 0.825, 0.5);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + tickDelta) * 5));
        matrices.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(90));
        matrices.translate(0, -0.125f, 0);

        MinecraftClient.getInstance().getItemRenderer().renderItem(entity.heldItem, ModelTransformationMode.GROUND, light, overlay, matrices, vertexConsumers, entity.getWorld(), 0);
        matrices.pop();
    }
}
