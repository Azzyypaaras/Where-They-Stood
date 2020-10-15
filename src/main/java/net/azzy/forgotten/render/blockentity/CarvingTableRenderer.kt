package net.azzy.forgotten.render.blockentity

import net.azzy.forgotten.blockentity.CarvingTableEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.state.property.Properties
import net.minecraft.util.math.Direction

class CarvingTableRenderer(dispatcher: BlockEntityRenderDispatcher) : BlockEntityRenderer<CarvingTableEntity>(dispatcher) {

    override fun render(entity: CarvingTableEntity, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int, overlay: Int) {

        fun performPermutations() {
            when(entity.cachedState[Properties.FACING]) {
                Direction.SOUTH -> matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180F))
                Direction.EAST -> matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(270F))
                Direction.WEST -> matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90F))
                else -> {}
            }
        }

        val inv = entity.items
        matrices.push()
        performPermutations()
        matrices.translate(0.225, 15 / 16.0, 0.35)
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F))
        matrices.scale(0.25F, 0.25F, 1F)
        MinecraftClient.getInstance().itemRenderer.renderItem(inv[0], ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers)
        matrices.pop()
        matrices.push()
        performPermutations()
        matrices.translate(0.0, 15 / 16.0, 0.76)
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90F))
        matrices.scale(0.15F, 0.15F, 0.15F)
        if(!inv[1].isEmpty)
            for(i in 0..inv[4].count / 4) {
                matrices.scale(0.999F, 0.999F, 0.999F)
                matrices.translate(0.0, 0.0, i * -0.025)
                MinecraftClient.getInstance().itemRenderer.renderItem(inv[4], ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers)
                matrices.translate(0.0, 0.0, i * 0.025)
            }
        matrices.translate(1.26, 0.0, 0.0)
        if(!inv[2].isEmpty)
            for(i in 0..inv[3].count / 4) {
                matrices.scale(0.999F, 0.999F, 0.999F)
                matrices.translate(0.0, 0.0, i * -0.025)
                MinecraftClient.getInstance().itemRenderer.renderItem(inv[3], ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers)
                matrices.translate(0.0, 0.0, i * 0.025)
            }
        matrices.translate(2.175, 0.0, 0.0)
        if(!inv[3].isEmpty)
            for(i in 0..inv[2].count / 4) {
                matrices.scale(0.999F, 0.999F, 0.999F)
                matrices.translate(0.0, 0.0, i * -0.025)
                MinecraftClient.getInstance().itemRenderer.renderItem(inv[2], ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers)
                matrices.translate(0.0, 0.0, i * 0.025)
            }
        matrices.translate(1.3125, 0.0, 0.0)
        if(!inv[4].isEmpty)
            for(i in 0..inv[1].count / 4) {
                matrices.scale(0.999F, 0.999F, 0.999F)
                matrices.translate(0.0, 0.0, i * -0.025)
                MinecraftClient.getInstance().itemRenderer.renderItem(inv[1], ModelTransformation.Mode.NONE, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers)
                matrices.translate(0.0, 0.0, i * 0.025)
            }
        matrices.pop()
    }
}