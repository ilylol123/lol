package dev.kyro.kyropit.commands;

import dev.kyro.kyropit.Misc;
import dev.kyro.kyropit.controllers.objects.Module;
import dev.kyro.kyropit.controllers.ModuleManager;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class ToggleCommand implements ICommand {

    @Override
    public String getCommandName() {
        return "toggle";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "toggle <module>";
    }

    @Override
    public List<String> getCommandAliases() {
        return new ArrayList<>();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if(args.length == 0) {

            Misc.sendPlayerMessage("Usage: /toggle <module>");
            return;
        }

        Module module = ModuleManager.getModule(args[0]);
        if(module == null) {

            String[] modules = new String[ModuleManager.modules.size()];
            for(int i = 0; i < ModuleManager.modules.size(); i++) {
                Module tempModule = ModuleManager.modules.get(i);
                modules[i] = tempModule.getRefName();
            }
            Misc.sendPlayerMessage("That module does not exist. Available modules:");
            Misc.sendPlayerMessage(String.join(", ", modules));
            return;
        }

        module.toggle();
        if(module.isEnabled()) {
            Misc.sendPlayerMessage(module.getDisplayName() + "&f has been enabled");
        } else {
            Misc.sendPlayerMessage(module.getDisplayName() + "&f has been disabled");
        }
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
