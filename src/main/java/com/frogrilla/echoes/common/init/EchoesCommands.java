package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.signal.*;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

import java.util.concurrent.atomic.AtomicInteger;

public class EchoesCommands {

    public static void registerCommands() {
        Echoes.LOGGER.info("Registering commands for " + Echoes.MOD_ID);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(Commands.literal("echoes")
                    .requires(source -> source.hasPermission(1))
                    // Create
                    .then(Commands.literal("create")
                        .then(Commands.argument("type", new SignalTypeArgument())
                            .then(Commands.argument("pos", new BlockPosArgument())
                                .then(Commands.argument("dir", new DirectionArgument())
                                    .then(Commands.argument("strength", IntegerArgumentType.integer(1, 15))
                                        .executes(context ->
                                            EchoesCreate(
                                                    context,
                                                    SignalTypeArgument.getSignalType(context, "type"),
                                                    BlockPosArgument.getBlockPos(context, "pos"),
                                                    DirectionArgument.getDirection(context, "dir"),
                                                    IntegerArgumentType.getInteger(context, "strength")
                                            )
                                        )
                                    )
                                )
                            )
                    ))

                    // List
                    .then(Commands.literal("list")
                            .executes(EchoesCommands::EchoesList)
                    )

                    // Clear
                    .then(Commands.literal("clear")
                            .executes(EchoesCommands::EchoesClear)
                    )
            );//
        });
    }

    private static int EchoesCreate(CommandContext<CommandSourceStack> context, SignalType<? extends AbstractSignal> signalType, BlockPos blockPos, Direction direction, int strength) {
        if(signalType != null) {
            var signal = AbstractSignal.fromSignalType(signalType, new SignalProperties(
                    blockPos,
                    direction,
                    strength
            ));
            SignalManager manager = SignalManager.getSignalManager(context.getSource().getLevel());
            manager.addSignal(signal);
            return 1;
        }
        else {
            context.getSource().sendFailure(Component.literal("Invalid location"));
            return 0;
        }
    }

    private static int EchoesList(CommandContext<CommandSourceStack> context) {
        SignalManager signalManager = SignalManager.getSignalManager(context.getSource().getLevel());

        if(signalManager.getSignals().isEmpty()){
            context.getSource().sendSuccess(() -> Component.literal("No signals in this level"), false);
        }
        else {
            AtomicInteger count = new AtomicInteger();

            signalManager.getSignals().forEach(signal -> {
                context.getSource().sendSuccess(() -> Component.literal(count.toString() + ": " + signal.toString()), false);
                count.addAndGet(1);
            });
        }

        return 1;
    }

    private static int EchoesClear(CommandContext<CommandSourceStack> context) {
        SignalManager signalManager = SignalManager.getSignalManager(context.getSource().getLevel());

        if(signalManager.getSignals().isEmpty()){
            context.getSource().sendSuccess(() -> Component.literal("No signals in level to clear"), false);
        }
        else {
            context.getSource().sendSuccess(() -> Component.literal(String.format("Clearing %d signals", signalManager.getSignals().size())), false);
            signalManager.getSignals().clear();
        }

        return 1;
    }

}
