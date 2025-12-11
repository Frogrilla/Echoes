package com.frogrilla.echoes.render.signal;

import com.frogrilla.echoes.Echoes;
import com.frogrilla.echoes.EchoesClient;
import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MappableRingBuffer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShapeRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryUtil;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class DebugSignalRenderer {

    private static DebugSignalRenderer instance;

    private static final RenderPipeline DEBUG_SIGNAL = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
            .withLocation(ResourceLocation.fromNamespaceAndPath(Echoes.MOD_ID, "pipeline/debug_signal"))
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
            .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
            .build()
    );

    private static final ByteBufferBuilder allocator = new ByteBufferBuilder(RenderType.BIG_BUFFER_SIZE);
    private BufferBuilder buffer;

    private void renderSignals(WorldRenderContext context) {
        PoseStack matrices = context.matrices();
        Vec3 camera = context.worldState().cameraRenderState.pos;

        assert matrices != null;
        matrices.pushPose();
        matrices.translate(-camera.x, -camera.y, -camera.z);

        if (buffer == null) {
            buffer = new BufferBuilder(allocator, DEBUG_SIGNAL.getVertexFormatMode(), DEBUG_SIGNAL.getVertexFormat());
        }

        EchoesClient.signalMap.forEach((uuid, blockPos) -> {
            Vec3 pos = blockPos.getCenter();
            ShapeRenderer.addChainedFilledBoxVertices(matrices, buffer, pos.x - 0.1f, pos.y - 0.1f, pos.z -0.1f, pos.x + 0.1f, pos.y + 0.1f, pos.z + 0.1f, 0f, 1f, 0f, 1f);
        });


        matrices.popPose();
    }

    private static final Vector4f COLOR_MODULATOR = new Vector4f(1f, 1f, 1f, 1f);
    private MappableRingBuffer vertexBuffer;

    private void drawFilledThroughWalls(Minecraft client, @SuppressWarnings("SameParameterValue") RenderPipeline pipeline) {
        // Build the buffer
        try {
            MeshData builtBuffer = buffer.buildOrThrow();
            MeshData.DrawState drawParameters = builtBuffer.drawState();
            VertexFormat format = drawParameters.format();

            GpuBuffer vertices = upload(drawParameters, format, builtBuffer);

            draw(client, pipeline, builtBuffer, drawParameters, vertices, format);

            // Rotate the vertex buffer so we are less likely to use buffers that the GPU is using
            vertexBuffer.rotate();
        } catch (Exception e) {
            //throw new RuntimeException(e);
        }
        buffer = null;
    }

    private GpuBuffer upload(MeshData.DrawState drawParameters, VertexFormat format, MeshData builtBuffer) {
        // Calculate the size needed for the vertex buffer
        int vertexBufferSize = drawParameters.vertexCount() * format.getVertexSize();

        // Initialize or resize the vertex buffer as needed
        if (vertexBuffer == null || vertexBuffer.size() < vertexBufferSize) {
            vertexBuffer = new MappableRingBuffer(() -> Echoes.MOD_ID + " example render pipeline", GpuBuffer.USAGE_VERTEX | GpuBuffer.USAGE_MAP_WRITE, vertexBufferSize);
        }

        // Copy vertex data into the vertex buffer
        CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();

        try (GpuBuffer.MappedView mappedView = commandEncoder.mapBuffer(vertexBuffer.currentBuffer().slice(0, builtBuffer.vertexBuffer().remaining()), false, true)) {
            MemoryUtil.memCopy(builtBuffer.vertexBuffer(), mappedView.data());
        }

        return vertexBuffer.currentBuffer();
    }

    private static void draw(Minecraft client, RenderPipeline pipeline, MeshData builtBuffer, MeshData.DrawState drawParameters, GpuBuffer vertices, VertexFormat format) {
        GpuBuffer indices;
        VertexFormat.IndexType indexType;

        if (pipeline.getVertexFormatMode() == VertexFormat.Mode.QUADS) {
            // Sort the quads if there is translucency
            builtBuffer.sortQuads(allocator, RenderSystem.getProjectionType().vertexSorting());
            // Upload the index buffer
            indices = pipeline.getVertexFormat().uploadImmediateIndexBuffer(builtBuffer.indexBuffer());
            indexType = builtBuffer.drawState().indexType();
        } else {
            // Use the general shape index buffer for non-quad draw modes
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(pipeline.getVertexFormatMode());
            indices = shapeIndexBuffer.getBuffer(drawParameters.indexCount());
            indexType = shapeIndexBuffer.type();
        }

        // Actually execute the draw
        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrix(), COLOR_MODULATOR, new Vector3f(), RenderSystem.getTextureMatrix(), 1f);
        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> Echoes.MOD_ID + " example render pipeline rendering", client.getMainRenderTarget().getColorTextureView(), OptionalInt.empty(), client.getMainRenderTarget().getDepthTextureView(), OptionalDouble.empty())) {
            renderPass.setPipeline(pipeline);

            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);

            // Bind texture if applicable:
            // Sampler0 is used for texture inputs in vertices
            // renderPass.bindSampler("Sampler0", textureView);

            renderPass.setVertexBuffer(0, vertices);
            renderPass.setIndexBuffer(indices, indexType);

            // The base vertex is the starting index when we copied the data into the vertex buffer divided by vertex size
            //noinspection ConstantValue
            renderPass.drawIndexed(0 / format.getVertexSize(), 0, drawParameters.indexCount(), 1);
        }

        builtBuffer.close();
    }

    public void close() {
        allocator.close();

        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
    }

    public static DebugSignalRenderer getInstance() {
        return instance;
    }

    public static void createInstance() {
        instance = new DebugSignalRenderer();
    }

    public void extractAndDraw(WorldRenderContext context) {
        renderSignals(context);
        drawFilledThroughWalls(Minecraft.getInstance(), DEBUG_SIGNAL);
    }
}
