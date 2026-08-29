package com.zenyte.game.records;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.zenyte.game.world.World;
import com.zenyte.logger.NearRealityLogger;
import com.zenyte.logger.NearRealityPrintStream;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Glabay | Glabay-Studios <-- IDIOT!
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-20
 */
public class RecordManager {
    private static final List<SpeedRecord> serverSpeedRecords = new ArrayList<>();
    private static final Path DATA_FILE = Paths.get("./data/global_records.json");

    private static RecordManager instance;

    public static RecordManager getInstance() {
        if (instance == null) instance = new RecordManager();
        return instance;
    }

    public RecordManager() {
        clearAndReloadFromFile();
    }

    // public in the case we want to be able to reload on the fly via a command for ex.
    public void clearAndReloadFromFile() {
        serverSpeedRecords.clear();
        loadFromFile();
    }

    public boolean isEventRecorded(String eventName) {
        return serverSpeedRecords.stream()
            .anyMatch(record -> record.eventName().equals(eventName));
    }

    public Optional<SpeedRecord> getRecordByEventName(String eventName) {
        return serverSpeedRecords.stream()
            .filter(record -> record.eventName().equals(eventName))
            .findFirst();
    }

    public void addRecord(SpeedRecord speedRecord) {
        // check if the event we're passing in is already recorded
        if (isEventRecorded(speedRecord.eventName())) {
            // now we get the recorded event and replace it
            getRecordByEventName(speedRecord.eventName())
                .ifPresentOrElse(
                    r -> replaceRecord(r, speedRecord),
                    // If for some reason we got here and there is no record, make a new one
                    () -> addNewRecord(speedRecord));
        }
        // we do not have a recorded event
        else addNewRecord(speedRecord);
    }

    private void addNewRecord(SpeedRecord speedRecord) {
        serverSpeedRecords.add(speedRecord);
        saveToFile(serverSpeedRecords);
        World.getPlayers().forEach(player -> player.sendMessage(speedRecord.toString()));
    }
    private void replaceRecord(SpeedRecord oldRecord, SpeedRecord newRecord) {
        serverSpeedRecords.remove(oldRecord);
        serverSpeedRecords.add(newRecord);
        saveToFile(serverSpeedRecords);
        World.getPlayers().forEach(player -> player.sendMessage("[New Record!] ".concat(newRecord.toString())));
    }

    public static void saveToFile(List<SpeedRecord> speedRecordsList) {
        try {
            // 1. Ensure parent directories exist
            Files.createDirectories(DATA_FILE.getParent());

            // 2. Serialize and write, creating or truncating the file
            String json = new Gson().toJson(speedRecordsList);
            Files.writeString(
                    DATA_FILE,
                    json,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            e.printStackTrace(NearRealityPrintStream.getErrorStream());
        }
    }

    private void loadFromFile() {
        try {
            // If the file doesn't even exist yet, nothing to load
            if (!Files.exists(DATA_FILE)) {
                return;
            }

            // Read the entire JSON
            String json = Files.readString(DATA_FILE);
            if (!json.isBlank()) {
                List<SpeedRecord> list = new Gson().fromJson(
                        json,
                        new TypeToken<List<SpeedRecord>>() {}.getType()
                );
                serverSpeedRecords.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace(NearRealityPrintStream.getErrorStream());
        }
    }

}
