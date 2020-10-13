package net.azzy.forgotten.render.entity

import net.azzy.forgotten.Forgotten
import net.azzy.forgotten.entity.ProjectileSpellEntity
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.*
import net.minecraft.client.render.entity.DragonFireballEntityRenderer
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.util.Identifier
import net.minecraft.util.math.Matrix3f
import net.minecraft.util.math.Matrix4f
import net.minecraft.world.World

class ProjectileSpellRenderer(dispatcher: EntityRenderDispatcher) : EntityRenderer<ProjectileSpellEntity>(dispatcher) {

    private val TEXTURE = Identifier("textures/entity/enderdragon/dragon_fireball.png")

    override fun getTexture(entity: ProjectileSpellEntity?): Identifier {
        return TEXTURE
    }

    override fun render(entity: ProjectileSpellEntity, yaw: Float, tickDelta: Float, matrices: MatrixStack, vertexConsumers: VertexConsumerProvider, light: Int) {
        val world: World = entity.world
        Forgotten.WTSLog.error("I am alive! is nice")
        matrices.push()
        matrices.scale(2.0f, 2.0f, 2.0f)
        matrices.multiply(dispatcher.rotation)
        matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0f))
        val entry: MatrixStack.Entry = matrices.peek()
        val matrix4f = entry.model
        val matrix3f = entry.normal
        val vertexConsumer: VertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE))
        produceVertex(vertexConsumer, matrix4f, matrix3f, light, 0.0f, 0, 0, 1)
        produceVertex(vertexConsumer, matrix4f, matrix3f, light, 1.0f, 0, 1, 1)
        produceVertex(vertexConsumer, matrix4f, matrix3f, light, 1.0f, 1, 1, 0)
        produceVertex(vertexConsumer, matrix4f, matrix3f, light, 0.0f, 1, 0, 0)
        matrices.pop()

        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light)
    }

    private fun produceVertex(vertexConsumer: VertexConsumer, modelMatrix: Matrix4f, normalMatrix: Matrix3f, light: Int, x: Float, y: Int, textureU: Int, textureV: Int) {
        vertexConsumer.vertex(modelMatrix, x - 0.5f, y.toFloat() - 0.25f, 0.0f).color(255, 255, 255, 255).texture(textureU.toFloat(), textureV.toFloat()).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(normalMatrix, 0.0f, 1.0f, 0.0f).next()
    }
}