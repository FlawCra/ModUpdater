package cc.flawcra.modupdater.command;

import cc.flawcra.modupdater.ModUpdater;
import cc.flawcra.modupdater.data.ModUpdate;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.CommandException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class ModUpdaterCommand {
    private static void checkLoaded() throws CommandException {
        if (ModUpdater.getUpdates() == null) {
            throw new CommandException(Text.translatable("commands." + ModUpdater.NAMESPACE + ".not_loaded"));
        }
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, registrationEnvironment) -> dispatcher.register(CommandManager.literal(ModUpdater.NAMESPACE)
                .then(CommandManager.literal("list").executes(context -> {
                    checkLoaded();
                    context.getSource().sendFeedback(Text.translatable("commands." + ModUpdater.NAMESPACE + ".list_title").formatted(Formatting.YELLOW), false);
                    ModUpdate[] updates = ModUpdater.getUpdates();
                    assert updates != null;
                    for (ModUpdate update : updates) {
                        context.getSource().sendFeedback(Text.literal(update.text).styled(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, update.downloadURL)).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.translatable("commands." + ModUpdater.NAMESPACE + ".hover")))), false);
                    }
                    return updates.length;
                }))
                .then(CommandManager.literal("refresh").requires(source -> source.hasPermissionLevel(3)).executes(context -> {
                    checkLoaded();
                    ModUpdater.findUpdates();
                    context.getSource().sendFeedback(Text.translatable("commands." + ModUpdater.NAMESPACE + ".refresh_start"), true);
                    return 1;
                }))
        ));
    }
}

