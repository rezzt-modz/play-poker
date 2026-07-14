package dev.rezzt.playpoker.blackjack.visual;

import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.lang.reflect.Field;

/**
 * Helper for modifying private Display and Interaction entity fields in Minecraft 1.20.1.
 *
 * <p>The public Display API is private in this version, so we access the synced entity data
 * accessors directly. The accessors and helper methods are looked up by their Mojang-mapped
 * names first (works in a development environment) and, if that fails, by their signature
 * (works in Forge's obfuscated production environment where names differ).</p>
 */
public final class DisplayHelper {

    private static final EntityDataAccessor<Byte> DISPLAY_BILLBOARD;
    private static final EntityDataAccessor<Vector3f> DISPLAY_TRANSLATION;
    private static final EntityDataAccessor<Vector3f> DISPLAY_SCALE;
    private static final EntityDataAccessor<Quaternionf> DISPLAY_LEFT_ROTATION;
    private static final EntityDataAccessor<Quaternionf> DISPLAY_RIGHT_ROTATION;
    private static final EntityDataAccessor<Integer> DISPLAY_INTERPOLATION_DURATION;
    private static final EntityDataAccessor<Integer> DISPLAY_INTERPOLATION_START;

    private static final EntityDataAccessor<Component> TEXT_TEXT;
    private static final EntityDataAccessor<Integer> TEXT_LINE_WIDTH;
    private static final EntityDataAccessor<Integer> TEXT_BACKGROUND;
    private static final EntityDataAccessor<Byte> TEXT_OPACITY;
    private static final EntityDataAccessor<Byte> TEXT_FLAGS;

    private static final EntityDataAccessor<ItemStack> ITEM_STACK;
    private static final EntityDataAccessor<Byte> ITEM_TRANSFORM;

    private static final EntityDataAccessor<Float> INTERACTION_WIDTH;
    private static final EntityDataAccessor<Float> INTERACTION_HEIGHT;
    private static final EntityDataAccessor<Boolean> INTERACTION_RESPONSE;

    static {
        try {
            Class<?> display = Display.class;
            DISPLAY_BILLBOARD = resolveAccessor(display, "DATA_BILLBOARD_RENDER_CONSTRAINTS_ID", 6);
            DISPLAY_TRANSLATION = resolveAccessor(display, "DATA_TRANSLATION_ID", 2);
            DISPLAY_SCALE = resolveAccessor(display, "DATA_SCALE_ID", 3);
            DISPLAY_LEFT_ROTATION = resolveAccessor(display, "DATA_LEFT_ROTATION_ID", 4);
            DISPLAY_RIGHT_ROTATION = resolveAccessor(display, "DATA_RIGHT_ROTATION_ID", 5);
            DISPLAY_INTERPOLATION_DURATION = resolveAccessor(display, "DATA_INTERPOLATION_DURATION_ID", 1);
            DISPLAY_INTERPOLATION_START = resolveAccessor(display, "DATA_INTERPOLATION_START_DELTA_TICKS_ID", 0);

            Class<?> text = Display.TextDisplay.class;
            TEXT_TEXT = resolveAccessor(text, "DATA_TEXT_ID", 0);
            TEXT_LINE_WIDTH = resolveAccessor(text, "DATA_LINE_WIDTH_ID", 1);
            TEXT_BACKGROUND = resolveAccessor(text, "DATA_BACKGROUND_COLOR_ID", 2);
            TEXT_OPACITY = resolveAccessor(text, "DATA_TEXT_OPACITY_ID", 3);
            TEXT_FLAGS = resolveAccessor(text, "DATA_STYLE_FLAGS_ID", 4);

            Class<?> item = Display.ItemDisplay.class;
            ITEM_STACK = resolveAccessor(item, "DATA_ITEM_STACK_ID", 0);
            ITEM_TRANSFORM = resolveAccessor(item, "DATA_ITEM_DISPLAY_ID", 1);

            Class<?> interaction = Interaction.class;
            INTERACTION_WIDTH = resolveAccessor(interaction, "DATA_WIDTH_ID", 0);
            INTERACTION_HEIGHT = resolveAccessor(interaction, "DATA_HEIGHT_ID", 1);
            INTERACTION_RESPONSE = resolveAccessor(interaction, "DATA_RESPONSE_ID", 2);
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize DisplayHelper", e);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> EntityDataAccessor<T> resolveAccessor(Class<?> clazz, String name, int index) throws Exception {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return (EntityDataAccessor<T>) field.get(null);
        } catch (NoSuchFieldException e) {
            return findAccessorByIndex(clazz, index);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> EntityDataAccessor<T> findAccessorByIndex(Class<?> clazz, int index) throws Exception {
        int count = 0;
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType() == EntityDataAccessor.class) {
                if (count == index) {
                    field.setAccessible(true);
                    return (EntityDataAccessor<T>) field.get(null);
                }
                count++;
            }
        }
        throw new NoSuchFieldException("No EntityDataAccessor at index " + index + " in " + clazz.getName());
    }

    private static byte billboardId(Display.BillboardConstraints constraints) {
        return switch (constraints) {
            case FIXED -> 0;
            case VERTICAL -> 1;
            case HORIZONTAL -> 2;
            case CENTER -> 3;
        };
    }

    private static <T> void set(net.minecraft.world.entity.Entity entity, EntityDataAccessor<T> accessor, T value) {
        SynchedEntityData data = entity.getEntityData();
        data.set(accessor, value);
    }

    public static void setBillboard(Display display, Display.BillboardConstraints constraints) {
        set(display, DISPLAY_BILLBOARD, billboardId(constraints));
    }

    public static void setTranslation(Display display, Vector3f translation) {
        set(display, DISPLAY_TRANSLATION, translation);
    }

    public static void setScale(Display display, Vector3f scale) {
        set(display, DISPLAY_SCALE, scale);
    }

    public static void setLeftRotation(Display display, Quaternionf rotation) {
        set(display, DISPLAY_LEFT_ROTATION, rotation);
    }

    public static void setRightRotation(Display display, Quaternionf rotation) {
        set(display, DISPLAY_RIGHT_ROTATION, rotation);
    }

    public static void setTransformation(Display display, Vector3f translation, Quaternionf leftRotation,
                                          Vector3f scale, Quaternionf rightRotation) {
        setTranslation(display, translation);
        setLeftRotation(display, leftRotation);
        setScale(display, scale);
        setRightRotation(display, rightRotation);
    }

    public static void startInterpolation(Display display, int durationTicks) {
        set(display, DISPLAY_INTERPOLATION_DURATION, durationTicks);
        set(display, DISPLAY_INTERPOLATION_START, 0);
    }

    public static void setText(Display.TextDisplay display, Component text) {
        set(display, TEXT_TEXT, text);
    }

    public static void setLineWidth(Display.TextDisplay display, int lineWidth) {
        set(display, TEXT_LINE_WIDTH, lineWidth);
    }

    public static void setBackgroundColor(Display.TextDisplay display, int argb) {
        set(display, TEXT_BACKGROUND, argb);
    }

    public static void setTextOpacity(Display.TextDisplay display, byte opacity) {
        set(display, TEXT_OPACITY, opacity);
    }

    public static void setTextFlags(Display.TextDisplay display, byte flags) {
        set(display, TEXT_FLAGS, flags);
    }

    public static void setItem(Display.ItemDisplay display, ItemStack stack) {
        set(display, ITEM_STACK, stack);
    }

    public static void setItemTransform(Display.ItemDisplay display, ItemDisplayContext context) {
        set(display, ITEM_TRANSFORM, (byte) context.getId());
    }

    public static void setInteractionSize(Interaction interaction, float width, float height) {
        set(interaction, INTERACTION_WIDTH, width);
        set(interaction, INTERACTION_HEIGHT, height);
    }

    public static void setInteractionResponse(Interaction interaction, boolean response) {
        set(interaction, INTERACTION_RESPONSE, response);
    }

    /**
     * Mounts {@code passenger} onto {@code vehicle} without playing sounds.
     */
    public static void mount(net.minecraft.world.entity.Entity passenger, net.minecraft.world.entity.Entity vehicle) {
        if (passenger == null || vehicle == null || passenger.getVehicle() == vehicle) {
            return;
        }
        passenger.startRiding(vehicle, true);
    }

    private DisplayHelper() {
    }
}
