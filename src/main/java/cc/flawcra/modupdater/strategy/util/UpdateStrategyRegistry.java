package cc.flawcra.modupdater.strategy.util;

import cc.flawcra.modupdater.api.UpdateStrategy;
import cc.flawcra.modupdater.strategy.CurseForgeStrategy;
import cc.flawcra.modupdater.strategy.GitHubReleasesStrategy;
import cc.flawcra.modupdater.strategy.JSONStrategy;
import cc.flawcra.modupdater.strategy.MavenStrategy;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class UpdateStrategyRegistry {
    private static final Map<String, UpdateStrategy> data = new HashMap<>();

    @Nullable
    static UpdateStrategy get(String name) {
        return data.get(name);
    }

    static {
        data.put("curseforge", new CurseForgeStrategy());
        data.put("maven", new MavenStrategy());
        data.put("github", new GitHubReleasesStrategy());
        data.put("json", new JSONStrategy());
    }
}
