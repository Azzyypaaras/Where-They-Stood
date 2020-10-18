package net.azzy.forgotten.entity

import io.netty.buffer.Unpooled
import net.azzy.forgotten.registry.EntityPacketRegistry
import net.azzy.forgotten.registry.EntityRegistry
import net.azzy.forgotten.util.context.ContextConsumer
import net.azzy.forgotten.util.context.ContextMap
import net.azzy.forgotten.util.context.SpellContext
import net.azzy.forgotten.util.context.construct
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.projectile.AbstractFireballEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.network.Packet
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.function.Consumer

private typealias SpellContextConsumer = ContextConsumer<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>
private typealias SpellContextMap = ContextMap<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>

class ProjectileSpellEntity : AbstractFireballEntity, SpellEntity {

    var contextMap: ContextMap<SpellContext.SpellPackage<ProjectileSpellEntity>, SpellContext>
    val modMap = mutableMapOf<SpellContext, Any>()
    var lifespan = 160
    var piercing = false
    val pkg = SpellContext.SpellPackage(this, world, blockPos, null)

    init {
        contextMap = construct(arrayOf<SpellContextConsumer>()) as SpellContextMap
    }

    constructor(entityType: EntityType<out AbstractFireballEntity>, world: World) : super(entityType, world)

    constructor(x: Double, y: Double, z: Double, speedX: Double, speedY: Double, speedZ: Double, world: World, consumers: Array<SpellContextConsumer>) : super(EntityRegistry.PROJECTILE_SPELL_ENTITY, x, y, z, speedX, speedY, speedZ, world){
        contextMap = construct(consumers) as SpellContextMap
    }

    @Environment(EnvType.CLIENT)
    constructor(world: World?, x: Double, y: Double, z: Double, velocityX: Double, velocityY: Double, velocityZ: Double, consumers: Array<SpellContextConsumer>) : super(EntityRegistry.PROJECTILE_SPELL_ENTITY, x, y, z, velocityX, velocityY, velocityZ, world)

    constructor(owner: LivingEntity, speedX: Double, speedY: Double, speedZ: Double, world: World, consumers: Array<SpellContextConsumer>): this(owner.x, owner.eyeY, owner.z, speedX, speedY, speedZ, world, consumers) {
        this.owner = owner
        this.setRotation(owner.yaw, owner.pitch)
    }

    constructor(owner: LivingEntity, speedX: Double, speedY: Double, speedZ: Double, world: World, lifespan: Int, piercing: Boolean, consumers: Array<SpellContextConsumer>): this(owner, speedX, speedY, speedZ, world, consumers) {
        this.lifespan = lifespan
        this.piercing = piercing
    }

    override fun tick() {
        baseTick()
        if(!world.isClient()){
            pkg.pos = blockPos
            if(age == 1)
                contextMap.execute(pkg, SpellContext.INIT)
            else if(age >= lifespan)
                contextMap.executeWithFallback(pkg, SpellContext.DESPAWN) { superKill() }
            setFlag(6, this.isGlowing)
            if(world.time % 2 == 0L)
                (world as ServerWorld).spawnParticles(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, world.random.nextInt(3), 0.2, 0.2, 0.2, 0.0)
            contextMap.executeWithFallback(pkg, SpellContext.POSCHECK, this::fallbackCheck)
            contextMap.executeWithFallback(pkg, SpellContext.MOVE, this::fallbackMove)
            contextMap.execute(pkg, SpellContext.TICK)
        }
    }

    fun map(values: Array<Pair<SpellContext, Any>>) = values.forEach { value -> modMap[value.first] = value.second }

    override fun onCollision(hitResult: HitResult?) {
        if(hitResult != null)
            contextMap.executeWithFallback(SpellContext.SpellPackage(this, world, blockPos, hitResult), SpellContext.COLLIDE, this::fallbackCollision)
    }

    override fun createSpawnPacket(): Packet<*> {
        val buffer = PacketByteBuf(Unpooled.buffer())
        EntitySpawnS2CPacket(this).write(buffer)
        return ServerSidePacketRegistry.INSTANCE.toPacket(EntityPacketRegistry.CLIENT_ENTITY_SPAWN, buffer)
    }

    override fun onEntityHit(entityHitResult: EntityHitResult?) {
        super.onEntityHit(entityHitResult)
        if(entityHitResult != null)
            contextMap.executeWithFallback(SpellContext.SpellPackage(this, world, blockPos, entityHitResult), SpellContext.ENTITYHIT, this::fallbackEntityHit)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        if(blockHitResult != null)
            contextMap.executeWithFallback(SpellContext.SpellPackage(this, world, blockPos, blockHitResult), SpellContext.BLOCKHIT, this::fallbackBlockHit)
    }

    override fun kill() {
        contextMap.executeWithFallback(pkg, SpellContext.DEATH) { super.kill() }
    }

    private fun superKill() {
        super.kill()
    }

    override fun dealDamage(attacker: LivingEntity?, target: Entity?) {
        contextMap.execute(SpellContext.SpellPackage(this, world, blockPos, null, target), SpellContext.DAMAGE)
        super.dealDamage(attacker, target)
    }

    override fun damage(source: DamageSource?, amount: Float): Boolean {
        if (this.isInvulnerableTo(source) || source?.attacker == null) {
            return false
        }
        return true
    }

    private fun fallbackMove(spellPackage: SpellContext.SpellPackage<ProjectileSpellEntity>) {
        val viscosity = if(isInLava) 0.45f else if(isSubmergedInWater) 0.8f else 1f
        val deltaX = x + (velocity.x * viscosity)
        val deltaY = y + (velocity.y * viscosity)
        val deltaZ = z + (velocity.z * viscosity)
        updatePosition(deltaX, deltaY, deltaZ);
    }

    private fun fallbackCheck(spellPackage: SpellContext.SpellPackage<ProjectileSpellEntity>) {
        val hitResult = ProjectileUtil.getCollision(this) { entity: Entity? -> method_26958(entity) }
        if (hitResult.type != HitResult.Type.MISS) {
            onCollision(hitResult)
        }
        checkBlockCollision()
    }

    override fun checkBlockCollision() {
        if(world.getBlockState(blockPos).isOpaque)
            onCollision(BlockHitResult(pos, Direction.NORTH, blockPos, true))
        super.checkBlockCollision()
    }

    private fun fallbackCollision(spellPackage: SpellContext.SpellPackage<ProjectileSpellEntity>) {
        val hitResult = spellPackage.hitResult
        if(hitResult != null){
            val type: HitResult.Type = hitResult.type
            if (type == HitResult.Type.ENTITY) {
                onEntityHit(hitResult as EntityHitResult?)
            } else if (type == HitResult.Type.BLOCK) {
                onBlockHit(hitResult as BlockHitResult?)
            }
        }
    }

    private fun fallbackEntityHit(spellPackage: SpellContext.SpellPackage<ProjectileSpellEntity>) {
        val hitResult = spellPackage.hitResult as EntityHitResult
        hitResult.entity.kill()
        kill()
    }

    private fun fallbackBlockHit(spellPackage: SpellContext.SpellPackage<ProjectileSpellEntity>) {
        val hitResult = spellPackage.hitResult as BlockHitResult
        val blockState = world.getBlockState(hitResult.blockPos)
        blockState.onProjectileHit(world, blockState, hitResult, this)
        kill()
    }
}
