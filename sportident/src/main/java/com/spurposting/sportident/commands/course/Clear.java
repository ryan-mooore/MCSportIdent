package com.spurposting.sportident.commands.course;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.classes.SIStation;
import org.bukkit.GameMode;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataHolder;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;

public class Clear extends SIStation implements CommandExecutor {
    NamespacedKey key;

    public Clear(Block c) {
        super(c);
        this.key = new NamespacedKey(Main.getInstance(), "splits");
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        ArrayList<Player> competitors = this.getNearbyCompetitors();

        for (Player competitor : competitors) {
            if (competitor.getGameMode().equals(GameMode.ADVENTURE)) {
                ItemStack sportIdentItem = this.getSportIdent(competitor);
                Main.database.deleteReference(sportIdentItem);

                ItemMeta itemMeta = sportIdentItem.getItemMeta();
                PersistentDataContainer container = null;
                assert itemMeta != null;
                container = ((PersistentDataHolder) itemMeta).getPersistentDataContainer();

                if (container.has(this.key, PersistentDataType.INTEGER)) {
                    container.remove(key);
                    sportIdentItem.setItemMeta(itemMeta);

                    competitor.sendMessage(Main.config.clearMessage);
                } else {
                    competitor.sendMessage(Main.config.standbyMessage);
                }

                ((Player) competitor).playSound(location, Sound.BLOCK_NOTE_BLOCK_PLING, 2f, 2f);
            }
        }
        return true;
    }
}
