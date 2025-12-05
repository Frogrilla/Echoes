package com.frogrilla.echoes.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.init.EchoesSignalTypes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class SignalTypeArgument implements ArgumentType<SignalType<? extends AbstractSignal>> {


    private static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(o -> new LiteralMessage("Invalid SigntalType: " + o));

    public static <S> SignalType<? extends AbstractSignal> getSignalType(CommandContext<S> context, String name) {
        return context.getArgument(name, SignalType.class);
    }

    public SignalType<? extends AbstractSignal> parse(StringReader stringReader) throws CommandSyntaxException {
        int i = stringReader.getCursor();
        while(stringReader.canRead() && ResourceLocation.isAllowedInResourceLocation(stringReader.peek())) {
            stringReader.skip();
        }
        var locationString = stringReader.getString().substring(i, stringReader.getCursor());

        int separator = locationString.indexOf(':');
        var location = separator >= 0 ? ResourceLocation.parse(locationString) : ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, locationString);

        var type = SignalType.REGISTRY.get(location);
        if(type.isEmpty()){
            throw INVALID_TYPE.createWithContext(stringReader, location);
        }
        return type.get().value();
    }


    private static final Collection<String> EXAMPLES = List.of(
            "echoes:echo",
            "echo"
    );

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader reader = new StringReader(builder.getInput());
        reader.setCursor(builder.getStart());

        int i = reader.getCursor();
        while(reader.canRead() && ResourceLocation.isAllowedInResourceLocation(reader.peek())) {
            reader.skip();
        }
        var typeString = reader.getString().substring(i, reader.getCursor());

        SignalType.REGISTRY.keySet().forEach(key -> {
            if(typeString.isEmpty()){
                builder.suggest(key.toString());
                return;
            }
            int separator = typeString.indexOf(':');
            if(separator >= 0){
                String namespace = typeString.substring(0, separator);
                if(!key.getNamespace().equals(namespace)) return;
            }
            else if(key.getNamespace().startsWith(typeString)){
                builder.suggest(key.toString());
                return;
            }

            String path = typeString.substring(separator+1);
            if(key.getPath().startsWith(path)) {
                builder.suggest(key.toString());
            }
        });
        return builder.buildFuture();
    }
}
