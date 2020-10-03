package net.azzy.forgotten.entity

import net.azzy.forgotten.registry.EntityRegistry
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.AbstractFireballEntity
import net.minecraft.particle.ParticleType
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import net.minecraft.world.explosion.ExplosionBehavior

class ProjectileSpellEntity : AbstractFireballEntity, SpellEntity {


    constructor(entityType: EntityType<out AbstractFireballEntity>, world: World) : super(entityType, world)

    constructor(x: Double, y: Double, z: Double, speedX: Double, speedY: Double, speedZ: Double, world: World) : super(EntityRegistry.PROJECTILE_SPELL_ENTITY, x, y, z, speedX, speedY, speedZ, world)

    constructor(owner: LivingEntity, speedX: Double, speedY: Double, speedZ: Double, world: World): this(owner.x, owner.y, owner.z, speedX, speedY, speedZ, world) {
        this.owner = owner
        this.setRotation(owner.yaw, owner.pitch)
    }

    override fun tick() {
        if(!world.isClient()){
            if(world.time % 2 == 0L)
                (world as ServerWorld).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, world.random.nextInt(3), 0.2, 0.2, 0.2, 0.0)
            if(age >= 100)
                kill()
        }
        super.tick()
    }

    override fun onCollision(hitResult: HitResult?) {
        world.createExplosion(this, DamageSource.DRAGON_BREATH, null, x, y, z, 2f, false, Explosion.DestructionType.NONE)
        if (!world.isClient) {
            (world as ServerWorld).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, world.random.nextInt(40) + 40, 0.5, 0.5, 0.5, 0.3)
        }
        kill()
        super.onCollision(hitResult)
    }
}