package com.frogrilla.echoes.gui.screen;

import com.frogrilla.echoes.common.screen.CrystalisationTableScreenHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CrystalisationTableScreen extends HandledScreen<CrystalisationTableScreenHandler> {
    // A path to the gui texture. In this example we use the texture from the dispenser

    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/gui/container/dispenser.png");
    // For versions before 1.21:
    // private static final Identifier TEXTURE = new Identifier("minecraft", "textures/gui/container/dispenser.png");

    public CrystalisationTableScreen(CrystalisationTableScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void init() {
        super.init();
        // Center the title
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }
}
