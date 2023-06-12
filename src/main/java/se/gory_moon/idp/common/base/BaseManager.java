package se.gory_moon.idp.common.base;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.gory_moon.idp.common.ItemPredicate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class BaseManager extends SimpleJsonResourceReloadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).create();

    private List<BaseData> dataList = ImmutableList.of();
    private final Object2BooleanArrayMap<UUID> dirtyMap = new Object2BooleanArrayMap<>();
    private final BiFunction<List<ItemPredicate>, List<Component>, BaseData> dataCreator;

    public BaseManager(String folder, BiFunction<List<ItemPredicate>, List<Component>, BaseData> dataCreator) {
        super(GSON, folder);
        this.dataCreator = dataCreator;
    }

    protected abstract String getDataName();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> elementMap, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
        ImmutableList.Builder<BaseData> builder = ImmutableList.builder();

        elementMap.forEach((resourceName, tooltip) -> {
            try {
                JsonObject jsonobject = GsonHelper.convertToJsonObject(tooltip, "data");
                JsonArray items = GsonHelper.getAsJsonArray(jsonobject, "items", new JsonArray());
                JsonArray data = GsonHelper.getAsJsonArray(jsonobject, getDataName(), new JsonArray());

                ArrayList<ItemPredicate> resourceLocations = new ArrayList<>();
                items.forEach(entry -> {
                    if (entry.isJsonObject()) {
                        JsonObject item = GsonHelper.convertToJsonObject(entry, "item");
                        ResourceLocation id = new ResourceLocation(GsonHelper.getAsString(item, "item"));
                        CompoundTag nbt = null;
                        if (item.has("nbt")) {
                            nbt = CraftingHelper.getNBT(item.get("nbt"));
                        }
                        resourceLocations.add(new ItemPredicate(id, nbt));
                    } else {
                        ResourceLocation id = new ResourceLocation(GsonHelper.convertToString(entry, "item id"));
                        resourceLocations.add(new ItemPredicate(id, null));
                    }
                });

                ArrayList<Component> textComponents = new ArrayList<>();
                for (JsonElement entry : data) {
                    Component component;
                    if (entry.isJsonPrimitive()) {
                        component = parseTextComponent(GsonHelper.convertToString(entry, getDataName()));
                    } else {
                        component = Component.Serializer.fromJson(entry);
                    }
                    textComponents.add(component);
                }

                builder.add(dataCreator.apply(resourceLocations, textComponents));
            } catch (IllegalArgumentException | JsonParseException e) {
                LOGGER.error("Parsing error loading data {}: {}", resourceName, e.getMessage());
            }
        });
        dirtyMap.replaceAll((uuid, aBoolean) -> true);
        dataList = builder.build();
    }

    private Component parseTextComponent(String s) {
        if (s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}' || s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
            Component component = null;
            try {
                component = Component.Serializer.fromJson(s);
            } catch (JsonParseException ignored) {}

            if (component == null) {
                try {
                    component = Component.Serializer.fromJsonLenient(s);
                } catch (JsonParseException ignored) {}
            }

            if (component == null) {
                component = Component.literal(s);
            }
            return component;
        } else {
            return Component.literal(s);
        }
    }

    public boolean isDirty(UUID id) {
        return dirtyMap.computeIfAbsent(id, uuid -> true);
    }

    public List<BaseData> getData() {
        return dataList;
    }

    public void clearDirty(UUID id) {
        dirtyMap.put(id, false);
    }
}
