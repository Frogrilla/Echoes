package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.common.signal.AbstractSignal;
import com.frogrilla.echoes.common.signal.CorruptedSignal;
import com.frogrilla.echoes.common.signal.Signal;

public class EchoesSignals {
    public static void registerSignalType(Class<? extends AbstractSignal> type)
    {
        AbstractSignal.SIGNAL_TYPES.put(type.getName(), type);
    }

    public static void registerTypes(){
        Echoes.LOGGER.info("Registereing signal types for " + Echoes.MOD_ID);
        registerSignalType(Signal.class);
        registerSignalType(CorruptedSignal.class);
    }
}
