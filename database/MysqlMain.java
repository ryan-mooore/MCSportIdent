package com.spurposting.plugin;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class MysqlMain extends JavaPlugin {
    private Connection connection;
    public String host, database, username, password;
    public int port;

    public void onEnable() {
        loadConfig();
        mysqlSetup();
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public void mysqlSetup() {
        host = this.getConfig().getString("host");
        port = this.getConfig().getInt("3306");
        database = this.getConfig().getString("database");
        username = this.getConfig().getString("username");
        password = this.getConfig().getString("table");

        mysqlOpenConnection();
    }

    private void mysqlOpenConnection() {
        try {
            synchronized (this) {
                if(connection != null && !connection.isClosed()) {
                    return;
                }
            }

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(
                "jdbc:mysql://" + this.host +
                ":" + this.port +
                "/" + this.database
                , this.username, this.password);

            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
        
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}