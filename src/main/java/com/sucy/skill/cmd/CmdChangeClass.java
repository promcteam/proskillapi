package com.sucy.skill.cmd;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.classes.RPGClass;
import com.sucy.skill.api.player.PlayerClass;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkillBar;
import com.sucy.skill.api.util.BuffManager;
import com.sucy.skill.api.util.Combat;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.dynamic.DynamicSkill;
import com.sucy.skill.hook.CitizensHook;
import com.sucy.skill.language.RPGFilter;
import mc.promcteam.engine.mccore.commands.ConfigurableCommand;
import mc.promcteam.engine.mccore.commands.IFunction;
import mc.promcteam.engine.mccore.config.Filter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ProSkillAPI © 2023
 * com.sucy.skill.cmd.CmdChangeClass
 */
public class CmdChangeClass implements IFunction, TabCompleter {
    private static final String INVALID_GROUP  = "invalid-group";
    private static final String INVALID_PLAYER = "invalid-player";
    private static final String INVALID_TARGET = "invalid-class";
    private static final String SUCCESS        = "success";
    private static final String NOTIFICATION   = "notification";

    public static void unload(Player player) {
        if (CitizensHook.isNPC(player))
            return;

        PlayerData data = SkillAPI.getPlayerData(player);
        if (SkillAPI.getSettings().isWorldEnabled(player.getWorld())) {
            data.record(player);
            data.stopPassives(player);
        }

        FlagManager.clearFlags(player);
        BuffManager.clearData(player);
        Combat.clearData(player);
        DynamicSkill.clearCastData(player);

        player.setDisplayName(player.getName());
        player.setWalkSpeed(0.2f);
    }

    /**
     * Executes the command
     *
     * @param cmd    owning command
     * @param plugin plugin reference
     * @param sender sender of the command
     * @param args   arguments
     */
    @Override
    public void execute(ConfigurableCommand cmd, Plugin plugin, CommandSender sender, String[] args) {
        if (args.length >= 3) {
            final String playerName = args[0];
            final String groupName  = args[1];
            String       className  = args[2];
            for (int i = 3; i < args.length; i++) className += ' ' + args[i];

            final Player player = Bukkit.getPlayer(playerName);
            if (player == null) {
                cmd.sendMessage(sender, INVALID_PLAYER, ChatColor.DARK_RED + "{player} is not online",
                        Filter.PLAYER.setReplacement(playerName));
                return;
            }

            final PlayerData  data  = SkillAPI.getPlayerData(player);
            final PlayerClass clazz = data.getClass(groupName);
            if (clazz == null) {
                cmd.sendMessage(sender, INVALID_GROUP, "{player} does not have a {class}",
                        Filter.PLAYER.setReplacement(player.getName()),
                        RPGFilter.GROUP.setReplacement(groupName),
                        RPGFilter.CLASS.setReplacement(className));
                return;
            }

            final String   original = clazz.getData().getName();
            final RPGClass target   = SkillAPI.getClass(className);
            if (target == null) {
                cmd.sendMessage(sender, INVALID_TARGET, "{class} is not a valid class to change to",
                        RPGFilter.CLASS.setReplacement(className));
                return;
            }

            boolean bar = data.getSkillBar().isEnabled() && SkillAPI.getSettings().isSkillBarEnabled();
            if (bar) {
                PlayerSkillBar skillBar = data.getSkillBar();
                skillBar.toggleEnabled();
                skillBar.reset();
                data.getSkillBar().toggleEnabled();
            }

            clazz.setClassData(target);
            unload(player);
            SkillAPI.getPlayerAccountData(player).getActiveData().init(player);
            if (bar)
                SkillAPI.getPlayerData(player).getSkillBar().toggleEnabled();


            cmd.sendMessage(sender, SUCCESS, "You have changed {player} from a {name} to a {class}",
                    Filter.PLAYER.setReplacement(player.getName()),
                    RPGFilter.GROUP.setReplacement(groupName),
                    RPGFilter.CLASS.setReplacement(className),
                    RPGFilter.NAME.setReplacement(original));

            if (sender != player) {
                cmd.sendMessage(player, NOTIFICATION, "You have changed from a {name} to a {class}",
                        RPGFilter.GROUP.setReplacement(groupName),
                        RPGFilter.CLASS.setReplacement(className),
                        RPGFilter.NAME.setReplacement(original));
            }
        } else {
            cmd.displayHelp(sender);
        }
    }

    @Override
    @Nullable
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            return ConfigurableCommand.getPlayerTabCompletions(commandSender, args[0]);
        } else if (args.length > 1) {
            String[] group = Arrays.copyOfRange(args, 1, 2);
            int i = 3;
            while (i <= args.length) {
                String[] concat = Arrays.copyOfRange(args, 1, i);
                String g = String.join(" ", concat);
                if (SkillAPI.getGroups().stream().noneMatch(g1 -> StringUtil.startsWithIgnoreCase(g1, g))) {
                    // Assume latest concatenation was right, let's do the class
                    String finalGroup = String.join(" ", group);
                    return ConfigurableCommand.getTabCompletions(SkillAPI.getClasses().values().stream()
                            .filter(rpgClass -> rpgClass.getGroup().equalsIgnoreCase(finalGroup))
                            .map(RPGClass::getName)
                            .collect(Collectors.toList()), Arrays.copyOfRange(args, i-1, args.length));
                }
                group = concat;
                i++;
            }

            // There's still valid groups available, let's suggest those
            return ConfigurableCommand.getTabCompletions(SkillAPI.getGroups(), group);
        }
        return null;
    }
}
