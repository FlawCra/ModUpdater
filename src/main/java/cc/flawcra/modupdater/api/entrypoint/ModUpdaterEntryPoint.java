package cc.flawcra.modupdater.api.entrypoint;

public interface ModUpdaterEntryPoint {
    boolean isVersionCompatible(String version);
}
