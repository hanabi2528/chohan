package com.hanabi.chohan;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;

public class Database {
    private final String DBname = "database.db";
    private Connection connection = null;
    private Statement statement = null;

    public Database() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.DBname);
            this.statement = connection.createStatement();
            statement.close();

            Bukkit.getLogger().info("ーーーーーsakeーーーーー");
            Bukkit.getLogger().info("データベースに接続しました");
            Bukkit.getLogger().info("ーーーーーーーーーーーーー");
        } catch (Exception e) {
            Bukkit.getLogger().warning("ーーーーーsakeーーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーー");
        }
    }

    public void CloseConnection() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            Bukkit.getLogger().warning(e.toString());
        }
    }

    public void AddMoney(Player player, int add_money){
        try{
            String uuid = player.getUniqueId().toString();

            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");

            int money = resultSet.getInt("money");
            money = money + add_money;

            this.statement.executeUpdate("update moneydata set money = " + money + " where uuid = '" + uuid + "'");

            resultSet.close();
            statement.close();
        }
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーMineDiceーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーー");
        }
    }

    public int CheckMoney(Player player){
        try{
            String uuid = player.getUniqueId().toString();

            this.statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select money from moneydata where uuid = '" + uuid + "'");

            int money = resultSet.getInt("money");

            resultSet.close();
            statement.close();
            return money;
        }
        catch (SQLException e){
            Bukkit.getLogger().warning("ーーーーMineDiceーーーー");
            Bukkit.getLogger().warning(e.toString());
            Bukkit.getLogger().warning("ーーーーーーーーーーーーー");
        }

        return -1;
    }
}

