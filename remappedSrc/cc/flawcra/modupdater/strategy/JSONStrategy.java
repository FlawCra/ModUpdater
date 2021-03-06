package cc.flawcra.modupdater.strategy;

import cc.flawcra.modupdater.ModUpdater;
import cc.flawcra.modupdater.api.ConfigObject;
import cc.flawcra.modupdater.api.UpdateStrategy;
import cc.flawcra.modupdater.data.ModUpdate;
import cc.flawcra.modupdater.util.Util;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonDataException;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

public class JSONStrategy implements UpdateStrategy {
    @SuppressWarnings("unused")
    private static class LatestVersionEntry {
        private String version;
        private String downloadUrl;
    }

    private final JsonAdapter<Map<String, LatestVersionEntry>> jsonAdapter;

    public JSONStrategy() {
        Moshi moshi = new Moshi.Builder().build();
        Type map = Types.newParameterizedType(Map.class, String.class, LatestVersionEntry.class);
        jsonAdapter = moshi.adapter(map);
    }

    @Override
    @Nullable
    public ModUpdate run(ConfigObject obj, String oldVersion, String name, String id) {
        String url;
        try {
            url = obj.getString("url");
        } catch (ConfigObject.MissingValueException e) {
            ModUpdater.logWarn(name, e.getMessage());
            return null;
        }

        String data;
        try {
            data = Util.urlToString(url);
        } catch (IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        Map<String, LatestVersionEntry> map;
        try {
            map = jsonAdapter.fromJson(data);
        } catch (JsonDataException | IOException e) {
            ModUpdater.logWarn(name, e.toString());
            return null;
        }

        if (map == null) {
            return null;
        }

        String version = Util.getMinecraftVersion().getId();
        if (map.containsKey(version)) {
            LatestVersionEntry entry = map.get(version);
            if (!oldVersion.equals(entry.version)) {
                return new ModUpdate(oldVersion, entry.version, entry.downloadUrl, name);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}

