package com.spurposting.sportident.classes;

import com.spurposting.sportident.Main;
import com.spurposting.sportident.database.Result;
import com.spurposting.sportident.database.Split;
import com.spurposting.sportident.database.SportIdent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

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

    public SplitsBook(SportIdent SI, Player player, Result.Status status) {


        writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) writtenBook.getItemMeta();

        addLine(Main.config.bookTitle);
        addLine(Main.config.bookSubtitle);
        addLine(player.getDisplayName());
        if (Main.config.showChipNumber) {
            addLine( "Chip: " + (Main.config.minChipNumber + (int)(Math.random() * ((Main.config.maxChipNumber - Main.config.minChipNumber) + 1))));
        }
        addLine( "Status: " + status.name());
        if (Main.config.showAbsoluteTimes) {
            addLine("Start: " + SI.splits.startTime.format(dtf));
            addLine("Finish: " + SI.splits.finishTime.format(dtf));
        }

        bookMeta.addPage(bookPage);
        bookPage = "";

        addLine(String.format("%1$-7s", "Start") + String.format(" %s %s", formatDuration(Duration.ofMillis(0)), formatDuration(Duration.ofMillis(0))));

        for (Split split : SI.splits.controls) {
            if (bookPage.split(System.getProperty("line.separator")).length == 14) {
                bookMeta.addPage(bookPage);
                bookPage = "";
            };

            String controlNum = ((Integer)split.controlNumber).toString();

            String absoluteTime = formatDuration(split.elapsedTime);
            String splitTime    = formatDuration(split.controlTime);

            if (Main.config.showAccumulatingTimes) {
                addLine(String.format("%1$-7s", controlNum) + String.format(" %s %s", splitTime, absoluteTime));
            } else {
                addLine(String.format("%1$-7s", controlNum) + String.format(" %s", splitTime));
            }

        }

        Split lastSplit = SI.splits.controls.get(SI.splits.controls.size() - 1);

        String controlTime = formatDuration(Duration.between(lastSplit.time, SI.splits.finishTime));
        String totalTime = formatDuration(Duration.between(SI.splits.startTime, SI.splits.finishTime));

        if (bookPage.split(System.getProperty("line.separator")).length == 14) {
            bookMeta.addPage(bookPage);
            bookPage = "";
        };

        addLine(String.format("%1$-7s", "Finish") + String.format(" %s %s", controlTime, totalTime));

        bookMeta.setTitle("Splits for " + player.getDisplayName());
        bookMeta.setAuthor(Main.config.author);
        bookMeta.addPage(bookPage);
        writtenBook.setItemMeta(bookMeta);
    }

    public ItemStack getBook() {
        return writtenBook;
    }
}