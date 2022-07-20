package com.github.idimabr.commands;

import com.github.idimabr.RaphaChat;
import com.github.idimabr.controller.ChannelController;
import com.github.idimabr.controller.DataController;
import com.github.idimabr.model.Channel;
import com.github.idimabr.model.ChannelType;
import com.github.idimabr.model.PlayerData;
import com.github.idimabr.storage.dao.StorageRepository;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@AllArgsConstructor
public class ChatAdminCommand implements CommandExecutor {

    private RaphaChat plugin;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cOnly players can execute this.");
            return false;
        }

        if(!sender.hasPermission("raphachat.admin")){
            sender.sendMessage("§cDon't have permission");
            return false;
        }

        final Player player = (Player) sender;
        final ChannelController channelController = plugin.getChannelController();
        final StorageRepository repository = plugin.getRepository();
        final DataController dataController = plugin.getDataController();

        if(args.length == 2 && args[0].equalsIgnoreCase("delete")){
            final Channel channel = channelController.getChannel(args[1].toLowerCase());
            if(channel == null){
                player.sendMessage("§cNot found '" + args[1] + "' channel.");
                player.sendMessage("§cChannels: §7" + channelController.getChannelList());
                return false;
            }

            channelController.removeChannel(args[1]);
            return true;
        }else if(args.length == 7 && args[0].equalsIgnoreCase("create")){
            try {
                final String name = args[1];
                final String command = args[2];
                final int radius = Integer.parseInt(args[3]);
                if(!validType(args[4])){
                    player.sendMessage("§cType invalid..");
                    player.sendMessage("§cTypes: §fALL, LOCAL");
                    return false;
                }

                final ChannelType type = ChannelType.valueOf(args[4].toUpperCase());
                final String tag = args[5];
                final String color = args[6];
                final String format = "{channel} {group}{sender}: {color}{message}";

                channelController.addChannel(
                        name.toLowerCase(),
                        new Channel(
                                name,
                                command,
                                radius,
                                type,
                                format,
                                tag,
                                color
                        )
                );
                player.sendMessage("§aChannel '" + name.toLowerCase() + "' created.");
            }catch(NumberFormatException ex){
                player.sendMessage("§cInvalid usage, try again.");
                return false;
            }
        }else if(args.length == 3 && args[0].equalsIgnoreCase("set")){
            final Player target = Bukkit.getPlayer(args[1]);
            if(target == null){
                player.sendMessage("§cPlayer not found.");
                return false;
            }

            final PlayerData data = dataController.getData(target);
            if(data == null){
                player.sendMessage("§cPlayer not storage yet...");
                return false;
            }

            final Channel channel = channelController.getChannel(args[2]);
            if(channel == null){
                player.sendMessage("§cChannel not found.");
                player.sendMessage("§cChannels: §7" + channelController.getChannelList());
                return false;
            }

            data.setChannel(channel);
            repository.changeChannel(player, channel.getName().toLowerCase());
            player.sendMessage("§aYou changed channel of '" + target.getName() + "' to '" + channel.getName().toLowerCase() + "'.");
            return true;
        } else{
            player.sendMessage("§aCommands: ");
            player.sendMessage("");
            player.sendMessage("        /chatadmin delete <name>");
            player.sendMessage("        /chatadmin create <name> <command> <radius> <type> <tag> <color>");
            player.sendMessage("        /chatadmin set <player> <channel>");
            player.sendMessage("");
            return false;
        }
        return false;
    }

    private boolean validType(String type){
        return Arrays.stream(ChannelType.values()).anyMatch($ -> $.name().equalsIgnoreCase(type));
    }
}
