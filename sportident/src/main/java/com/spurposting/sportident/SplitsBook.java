package com.spurposting.sportident;

import java.time.Duration;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class SplitsBook {

    public static ItemStack create(String playerName, Duration startTime, Duration finishTime, String status, List<Split> splits) {
        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();

        String bookPage = "Spurposting Event 2" + "\n";
        bookPage += "Venice" + "\n";
        bookPage += playerName + "\n";
        bookPage += "Chip: " + (2000000 + (int)(Math.random() * ((2999999 - 2000000) + 1))) + "\n";
        bookPage += "Status: " + status + "\n";
        bookPage += "Start: " + String.format("%02d:%02d:%02d",
        startTime.toHoursPart(),
        startTime.toMinutesPart(),
        startTime.toSecondsPart()) + "\n";
        bookPage += "Finish: " +
            String.format("%02d:%02d:%02d",
            finishTime.toHoursPart(),
            finishTime.toMinutesPart(),
            finishTime.toSecondsPart()) + "\n";

        for (Split split:splits) {
            String controlNum = ((Integer)split.controlNumber).toString();
            String absoluteTime = String.format("%02d:%02d",
            split.elapsedTime.toMinutesPart(),
            split.elapsedTime.toSecondsPart()/*,
            split.elapsedTime.toMillisPart()*/);
            String splitTime = String.format("%02d:%02d",
            split.controlTime.toMinutesPart(),
            split.controlTime.toSecondsPart()/*,
            split.controlTime.toMillisPart()*/);
            String line = String.format("%s %s %s", controlNum, splitTime, absoluteTime);
            bookPage += line + "\n";

        }
        bookMeta.setTitle("Splits for " + playerName);
        bookMeta.setAuthor("Ryan's SportIdent Solutions");
        bookMeta.setPages(bookPage);
        writtenBook.setItemMeta(bookMeta);
        return writtenBook;
    }
}