package com.spurposting.sportident;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import com.spurposting.sportident.database.Split;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SplitsBook {

    DateTimeFormatter dtf = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    String bookPage = "";
    ItemStack writtenBook;

    void addLine(String text) {
        bookPage += text;
        bookPage += "\n";
    }

    String formatDuration(Duration time) {
        return String.format("%02d:%02d",
                time.toMinutesPart(),
                time.toSecondsPart()/*,
            time.elapsedTime.toMillisPart()*/);
    }

    public SplitsBook(SportIdent SI, Player player, Boolean mispunched) {

        String status;
        if (mispunched) {
            status = "OK";
        } else {
            status = "MP";
        }

        writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();

        addLine(Main.config.bookTitle);
        addLine(Main.config.bookSubtitle);
        addLine(player.getDisplayName());
        if (Main.config.showChipNumber) addLine( "Chip: " + (2000000 + (int)(Math.random() * ((2999999 - 2000000) + 1))));
        addLine( "Status: " + status);
        if (Main.config.showAbsoluteTimes) {
            addLine("Start: " + SI.splits.startTime.format(dtf));
            addLine("Finish: " + SI.splits.finishTime.format(dtf));
        }

        for (Split split : SI.splits.controls) {
            String controlNum = ((Integer)split.controlNumber).toString();

            String absoluteTime = formatDuration(split.elapsedTime);
            String splitTime    = formatDuration(split.controlTime);

            if (Main.config.showAccumulatingTimes) {
                addLine(String.format("%s   %s %s", controlNum, splitTime, absoluteTime));
            } else {
                addLine(String.format("%s   %s", controlNum, splitTime));
            }

        }
        bookMeta.setTitle("Splits for " + player.getDisplayName());
        bookMeta.setAuthor(Main.config.author);
        bookMeta.setPages(bookPage);
        writtenBook.setItemMeta(bookMeta);
    }

    public ItemStack getBook() {
        return writtenBook;
    }
}