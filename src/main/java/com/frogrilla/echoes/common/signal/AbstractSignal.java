package com.frogrilla.echoes.common.signal;

import com.frogrilla.echoes.signal.SignalManager;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public abstract class AbstractSignal {
    public static HashMap<String, Class<? extends AbstractSignal>> SIGNAL_TYPES = new HashMap<>();

    public BlockPos blockPos;
    public Direction direction;
    public byte frequency;
    public boolean removalFlag = false;
    public AbstractSignal(BlockPos blockPos, Direction direction, byte frequency) {
        this.blockPos = blockPos;
        this.direction = direction;
        this.frequency = frequency;
    }

    public AbstractSignal(NbtCompound compound){
        this.blockPos = BlockPos.fromLong(compound.getLong("pos"));
        this.direction = Direction.byId(compound.getByte("direction"));
        this.frequency = compound.getByte("frequency");
    }

    public NbtCompound asCompound(){
        NbtCompound compound = new NbtCompound();
        compound.putLong("pos", blockPos.asLong());
        compound.putByte("direction", (byte) direction.getId());
        compound.putByte("frequency", frequency);
        return compound;
    }

    public String toString() {
        return String.format("%s, %s, %s", blockPos.toShortString(), frequency, direction);
    }

    /**
     * Called before the signal manager checks if the signal will be ticked
     */
    public void preTick() {};

    /**
     * Called after preTick. Do modifications to the signal in preTick and not here.
     * @return Whether this signal should go through the tick routine of it's manager
     */
    public boolean shouldTick()
    {
        return true;
    }

    /**
     * Does nothing by default, but other signal types may use it to reset their properties.
     * Use as an alternative to creating a new instance.
     */
    public void refreshProperties() {};

    /**
     * Method for how the signal should move by default.
     * Signal interactors should use this method instead of calculating positions themselves.
     */
    public abstract void step();

    /**
     * Called by the signal manager if shouldTick returns true
     * and if the signal isn't handled by a signal interactor.
     */
    public abstract void tick();

    /**
     * Do effects such as particles and sounds at the given position.
     * @param world
     * @param pos
     */
    public abstract void effects(ServerWorld world, Vec3d pos);

}
