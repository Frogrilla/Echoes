package com.frogrilla.echoes.common.init;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.signal.SignalManager;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.resources.ResourceLocation;

public class EchoesAttatchmentTypes {

    public static final AttachmentType<SignalManager> SIGNAL_MANAGER_ATTACHMENT_TYPE = AttachmentRegistry.create(
            ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "signal_manager_attachment"),
            builder -> builder
                    .initializer(SignalManager::new) // The default value of the Attachment, if one has not been set.
                    .persistent(SignalManager.CODEC) // Dictates how this Attachment's data should be saved and loaded.
                    .syncWith(
                            SignalManager.STREAM_CODEC,
                            AttachmentSyncPredicate.all()
                    )
    );

    public static void registerAttatchmentTypes(){
        Echoes.LOGGER.info("Registering attatchment types for " + Echoes.MOD_ID);
    }
}
