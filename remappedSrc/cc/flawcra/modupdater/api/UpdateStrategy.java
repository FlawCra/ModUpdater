package cc.flawcra.modupdater.api;

import cc.flawcra.modupdater.data.ModUpdate;

import javax.annotation.Nullable;

public interface UpdateStrategy {
    @Nullable
    ModUpdate run(ConfigObject obj, String oldVersion, String name, String id);

    default boolean isStrict(ConfigObject obj) {
        try {
            return obj.getBoolean("strict");
        } catch (ConfigObject.MissingValueException e) {
            return true;
        }
    }
}

