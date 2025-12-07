package com.frogrilla.echoes.common.item;

import com.frogrilla.echoes.common.signal.CorruptedEcho;
import com.frogrilla.echoes.common.signal.Echo;
import com.frogrilla.echoes.common.signal.ItemEcho;
import com.frogrilla.echoes.common.signal.PulseEcho;
import com.frogrilla.echoes.signal.SignalManager;
import com.frogrilla.echoes.signal.SignalProperties;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class SignalSpawner extends Item {
    public SignalSpawner(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext useOnContext) {

        Level level = useOnContext.getLevel();
        if(!level.isClientSide()) {
            SignalManager signalManager = SignalManager.getSignalManager((ServerLevel) level);
            PulseEcho echo = new PulseEcho(
                    new SignalProperties(
                        useOnContext.getClickedPos(),
                        useOnContext.getClickedFace(),
                        15
                    )
            );
            signalManager.addSignal(echo);
        }
        return InteractionResult.SUCCESS;
    }
}
