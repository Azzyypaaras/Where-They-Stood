package net.azzy.forgotten.entity

import net.azzy.forgotten.registry.EntityRegistry
import net.azzy.forgotten.util.context.ContextConsumer
import net.azzy.forgotten.util.context.ContextMap
import net.azzy.forgotten.util.context.SpellContext
import net.azzy.forgotten.util.context.construct
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.AbstractFireballEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion
import java.util.function.Consumer

class ProjectileSpellEntity : AbstractFireballEntity, SpellEntity {

    var contextMap: ContextMap<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>
    var lifespan = 100
    var piercing = false

    init {
        contextMap = construct(arrayOf<ContextConsumer<Consumer<SpellContext.SpellPackage<ProjectileSpellEntity>>, SpellContext>>()) as ContextMap<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>
    }

    constructor(entityType: EntityType<out AbstractFireballEntity>, world: World) : super(entityType, world)

    constructor(x: Double, y: Double, z: Double, speedX: Double, speedY: Double, speedZ: Double, world: World, consumers: Array<ContextConsumer<Consumer<SpellContext.SpellPackage<ProjectileSpellEntity>>, SpellContext>>) : super(EntityRegistry.PROJECTILE_SPELL_ENTITY, x, y, z, speedX, speedY, speedZ, world){
        contextMap = construct(consumers) as ContextMap<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>
    }

    @Environment(EnvType.CLIENT)
    constructor(world: World?, x: Double, y: Double, z: Double, velocityX: Double, velocityY: Double, velocityZ: Double, consumers: Array<ContextConsumer<Consumer<SpellContext.SpellPackage<ProjectileSpellEntity>>, SpellContext>>) : super(EntityRegistry.PROJECTILE_SPELL_ENTITY, x, y, z, velocityX, velocityY, velocityZ, world)

    constructor(owner: LivingEntity, speedX: Double, speedY: Double, speedZ: Double, world: World, consumers: Array<ContextConsumer<Consumer<SpellContext.SpellPackage<ProjectileSpellEntity>>, SpellContext>>): this(owner.x, owner.eyeY, owner.z, speedX, speedY, speedZ, world, consumers) {
        this.owner = owner
        this.setRotation(owner.yaw, owner.pitch)
    }

    constructor(owner: LivingEntity, speedX: Double, speedY: Double, speedZ: Double, world: World, lifespan: Int, piercing: Boolean, consumers: Array<ContextConsumer<Consumer<SpellContext.SpellPackage<ProjectileSpellEntity>>, SpellContext>>): this(owner, speedX, speedY, speedZ, world, consumers) {
        this.lifespan = lifespan
        this.piercing = piercing
    }

    override fun tick() {
        baseTick()
        if(!world.isClient()){
            setFlag(6, this.isGlowing)
            if(world.time % 2 == 0L)
                (world as ServerWorld).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, world.random.nextInt(3), 0.2, 0.2, 0.2, 0.0)
            if(age >= lifespan)
                kill()
            contextMap.executeWithFallback(SpellContext.SpellPackage(this, world, blockPos), SpellContext.MOVE, this::fallbackMove)
        }
    }

    override fun onCollision(hitResult: HitResult?) {
        world.createExplosion(this, DamageSource.DRAGON_BREATH, null, x, y, z, 2f, false, Explosion.DestructionType.NONE)
        if (!world.isClient) {
            (world as ServerWorld).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, world.random.nextInt(40) + 40, 0.5, 0.5, 0.5, 0.3)
        }
        kill()
        super.onCollision(hitResult)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult?) {
        super.onEntityHit(entityHitResult)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        super.onBlockHit(blockHitResult)
    }

    override fun kill() {
        super.kill()
    }

    override fun dealDamage(attacker: LivingEntity?, target: Entity?) {
        super.dealDamage(attacker, target)
    }

    override fun damage(source: DamageSource?, amount: Float): Boolean {
        return super.damage(source, amount)
    }

    override fun collides(): Boolean {
        return super.collides()
    }

    private fun fallbackMove(spellPackage: SpellContext.SpellPackage<ProjectileSpellEntity>) {
        val deltaX = x + velocity.x
        val deltaY = y + velocity.y
        val deltaZ = z + velocity.z
        updatePosition(deltaX, deltaY, deltaZ);
    }
}