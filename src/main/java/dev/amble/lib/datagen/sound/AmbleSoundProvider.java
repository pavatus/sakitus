package dev.amble.lib.datagen.sound;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundEvent;

/**
 * Datagen Provider for sounds, this class is used to generate the sounds.json file for the mod
 */
public class AmbleSoundProvider implements DataProvider {
    protected final FabricDataOutput dataOutput;
    private final HashMap<String, SoundEvent[]> sounds = new HashMap<>();

    public AmbleSoundProvider(FabricDataOutput dataOutput) {
        this.dataOutput = dataOutput;
    }

    public void generateSoundsData(SoundBuilder builder) {
        sounds.forEach(builder::add);
    }
    public void addSound(String name, SoundEvent event) {
        sounds.put(name, new SoundEvent[]{event});
    }
    public void addSound(String name, SoundEvent... events) {
        sounds.put(name, events);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        for (SoundEvent sound : getSoundsFromMod(dataOutput.getModId())) {
            addSound(sound.getId().getPath(), sound);
        }

        HashMap<String, SoundEvent[]> soundEventsHashMap = new HashMap<>();

        generateSoundsData(((soundName, soundEvents) -> {
            if (soundEventsHashMap.containsKey(soundName)) {
                throw new RuntimeException("Duplicate sound event: " + soundName + " - Duplicate will be ignored!");
            } else if (soundName.contains(" ")) {
                throw new RuntimeException("Sound event name cannot contain spaces: " + soundName);
            } else {
                for (Character character : soundName.toCharArray()) {
                    if (Character.isTitleCase(character)) {
                        throw new RuntimeException("Sound event name cannot contain capital letters: " + soundName);
                    } else if (Character.isUpperCase(character)) {
                        throw new RuntimeException("Sound event name cannot contain capital letters: " + soundName);
                    }
                }
                soundEventsHashMap.put(soundName, soundEvents);
            }
        }));
        JsonObject soundJsonObject = new JsonObject();

        soundEventsHashMap.forEach((soundName, soundEvents) -> {
            soundJsonObject.add(soundName, createJsonObjectForSoundEvent(soundEvents));
        });

        return DataProvider.writeToPath(writer, soundJsonObject, getOutputPath());
    }

    public Path getOutputPath() {
        return dataOutput.resolvePath(DataOutput.OutputType.RESOURCE_PACK).resolve(dataOutput.getModId())
                .resolve("sounds.json");
    }

    @Override
    public String getName() {
        return "Sound Definitions";
    }

    public JsonObject createJsonObjectForSoundEvent(SoundEvent[] soundEvents) {
        JsonObject soundEventJsonObject = new JsonObject();
        JsonArray soundsJsonObject = new JsonArray();

        for (SoundEvent soundEvent : soundEvents) {
            soundsJsonObject.add(soundEvent.getId().toString());
        }

        soundEventJsonObject.add("sounds", soundsJsonObject);
        return soundEventJsonObject;
    }

    public static List<SoundEvent> getSoundsFromMod(String modid) {
        return Registries.SOUND_EVENT.stream().filter(sound -> sound.getId().getNamespace().equals(modid)).toList();
    }
}
