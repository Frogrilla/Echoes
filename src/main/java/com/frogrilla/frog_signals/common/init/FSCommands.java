package com.frogrilla.frog_signals.common.init;

import com.frogrilla.frog_signals.FrogSignals;
import com.frogrilla.frog_signals.signals.Signal;
import com.frogrilla.frog_signals.signals.SignalManager;
import com.frogrilla.frog_signals.signals.persistentManagerState;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FSCommands {
    public static void registerCommands() {
        FrogSignals.LOGGER.info("Registering commands for " + FrogSignals.MOD_ID);
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) ->
            dispatcher.register(literal("signal")
                .requires(source -> source.hasPermissionLevel(2))
                    .then(literal("new")
                        .then(argument("pos", BlockPosArgumentType.blockPos())
                                .executes(context -> {
                                    final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                    executeNewSignal(pos, Direction.UP, 16, context);
                                    return 1;
                                })
                                .then(argument("power", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.UP, power, context);
                                            return 1;
                                        })
                                    .then(literal("up")
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.UP, power, context);
                                            return 1;
                                        }))
                                    .then(literal("down")
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.DOWN, power, context);
                                            return 1;
                                        }))
                                    .then(literal("north")
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.NORTH, power, context);
                                            return 1;
                                        }))
                                    .then(literal("east")
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.WEST, power, context);
                                            return 1;
                                        }))
                                    .then(literal("south")
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.SOUTH, power, context);
                                            return 1;
                                        }))
                                    .then(literal("west")
                                        .executes(context -> {
                                            final BlockPos pos = BlockPosArgumentType.getBlockPos(context, "pos");
                                            final int power = IntegerArgumentType.getInteger(context, "power");
                                            executeNewSignal(pos, Direction.WEST, power, context);
                                            return 1;
                                        })))))


                        .then(literal("clear")
                            .executes(context -> {
                                executeClearSignals(context);
                                return 1;
                            }))

                        .then(literal("count")
                            .executes(context -> {
                                executeCountSignals(context);
                                return 1;
                            }))
                )
        ));
    }

    private static void executeNewSignal(BlockPos blockPos, Direction direction, int power, CommandContext<ServerCommandSource> context) {
        SignalManager manager = persistentManagerState.getServerWorldState(context.getSource().getWorld()).signalManager;
        manager.addSignal(new Signal(
                blockPos, direction, power
        ));
        context.getSource().sendFeedback(() -> Text.literal("Created signal in %s:\nAt %s\nPower: %s\nDirection: %s".formatted(context.getSource().getWorld().getRegistryKey().getValue(), blockPos.toShortString(), power, direction)), true);
    }

    private static void executeClearSignals(CommandContext<ServerCommandSource> context){
        SignalManager manager = persistentManagerState.getServerWorldState(context.getSource().getWorld()).signalManager;
        manager.signals.clear();
        manager.signalBuffer.clear();
        manager.signalBin.clear();
        context.getSource().sendFeedback(() -> Text.literal("Cleared signals in %s".formatted(context.getSource().getWorld().getRegistryKey().getValue())), true);
    }

    private static void executeCountSignals(CommandContext<ServerCommandSource> context){
        SignalManager manager = persistentManagerState.getServerWorldState(context.getSource().getWorld()).signalManager;
        context.getSource().sendFeedback(() -> Text.literal("Signal count for %s:\nActive: %s\nBuffer: %s\nBin: %s".formatted(context.getSource().getWorld().getRegistryKey().getValue(), manager.signals.size(), manager.signalBuffer.size(), manager.signalBin.size())), true);
    }

}
