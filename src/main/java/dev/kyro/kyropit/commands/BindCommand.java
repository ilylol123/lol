package dev.kyro.kyropit.commands;

import dev.kyro.kyropit.Misc;
import dev.kyro.kyropit.controllers.ConfigManager;
import dev.kyro.kyropit.controllers.objects.Module;
import dev.kyro.kyropit.controllers.ModuleManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraftforge.common.config.Property;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

public class BindCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "bind";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "bind <module> <key>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if(args.length < 2) {

            Misc.sendPlayerMessage("Usage: /bind <module> <key>");
            return;
        }

        Module module = ModuleManager.getModule(args[0]);
        if(module == null) {

            Misc.sendPlayerMessage("That module does not exist");
            return;
        }

        int key = Keyboard.getKeyIndex(args[1].toUpperCase());
		module.keyBind = new KeyBinding("", key, "");

        Property keybindProperty = ConfigManager.config.get("keybinds", module.getRefName(), -1);
        keybindProperty.set(key);
        ConfigManager.saveConfig();

        Misc.sendPlayerMessage("&fBound " + module.getDisplayName() + "&f to the key " + Keyboard.getKeyName(key));
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return null;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return true;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
