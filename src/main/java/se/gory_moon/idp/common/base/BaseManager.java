package se.gory_moon.idp.common.base;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiFunction;

public abstract class BaseManager<T extends BaseData> extends JsonReloadListener {
    public static final Logger LOGGER = LogManager.getLogger();
    private static final Gson GSON = (new GsonBuilder()).create();

    private List<T> dataList = ImmutableList.of();
    private final Object2BooleanArrayMap<UUID> dirtyMap = new Object2BooleanArrayMap<>();
    private final BiFunction<List<ResourceLocation>, List<ITextComponent>, T> dataCreator;

    public BaseManager(String folder, BiFunction<List<ResourceLocation>, List<ITextComponent>, T> dataCreator) {
        super(GSON, folder);
        this.dataCreator = dataCreator;
    }

    protected abstract String getDataName();

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        ImmutableList.Builder<T> builder = ImmutableList.builder();

        objectIn.forEach((resourceName, tooltip) -> {
            try {
                JsonObject jsonobject = JSONUtils.getJsonObject(tooltip, "data");
                JsonArray items = JSONUtils.getJsonArray(jsonobject, "items", new JsonArray());
                JsonArray data = JSONUtils.getJsonArray(jsonobject, getDataName(), new JsonArray());

                ArrayList<ResourceLocation> resourceLocations = new ArrayList<>();
                items.forEach(entry -> resourceLocations.add(new ResourceLocation(JSONUtils.getString(entry, "item id"))));

                ArrayList<ITextComponent> textComponents = new ArrayList<>();
                for (JsonElement entry : data) {
                    ITextComponent itextcomponent = null;
                    if (entry.isJsonPrimitive()) {
                        itextcomponent = parseTextComponent(JSONUtils.getString(entry, getDataName()));
                    } else {
                        itextcomponent = ITextComponent.Serializer.getComponentFromJson(entry);
                    }
                    textComponents.add(itextcomponent);
                }

                builder.add(dataCreator.apply(resourceLocations, textComponents));
            } catch (IllegalArgumentException | JsonParseException e) {
                LOGGER.error("Parsing error loading data {}: {}", resourceName, e.getMessage());
            }
        });
        dirtyMap.replaceAll((uuid, aBoolean) -> true);
        dataList = builder.build();
    }

    private ITextComponent parseTextComponent(String s) {
        if (s.charAt(0) == '{' && s.charAt(s.length() - 1) == '}' || s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
            ITextComponent itextcomponent = null;
            try {
                itextcomponent = ITextComponent.Serializer.getComponentFromJson(s);
            } catch (JsonParseException ignored) {}

            if (itextcomponent == null) {
                try {
                    itextcomponent = ITextComponent.Serializer.getComponentFromJsonLenient(s);
                } catch (JsonParseException ignored) {}
            }

            if (itextcomponent == null) {
                itextcomponent = new StringTextComponent(s);
            }
            return itextcomponent;
        } else {
            return new StringTextComponent(s);
        }
    }

    public boolean isDirty(UUID id) {
        return dirtyMap.computeBooleanIfAbsent(id, uuid -> true);
    }

    public List<T> getData() {
        return dataList;
    }

    public void clearDirty(UUID id) {
        dirtyMap.put(id, false);
    }
}
