package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DirectionArgument implements ArgumentType<Direction> {
    private static final DynamicCommandExceptionType INVALID_DIRECTION = new DynamicCommandExceptionType(o -> new LiteralMessage("Invalid Direction: " + o));

    public static <S> Direction getDirection(CommandContext<S> context, String name) {
        return context.getArgument(name, Direction.class);
    }

    public Direction parse(StringReader stringReader) throws CommandSyntaxException {
        var directionString = stringReader.readUnquotedString();
        return Direction.byName(directionString);
    }


    private static final Collection<String> EXAMPLES = List.of(
            "NORTH",
            "SOUTH"
    );

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());
        var directionString = reader.readUnquotedString();

        Arrays.stream(Direction.values()).forEach(direction -> {

            if(directionString.isEmpty()){
                builder.suggest(direction.toString());
                return;
            }

            if(direction.toString().startsWith(directionString)) {
                builder.suggest(direction.toString());
            }
        });
        return builder.buildFuture();
    }
}
