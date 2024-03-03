package com.hanabi.chohan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public final class Chohan extends JavaPlugin {
    boolean game;
    int betmoney;
    int allmoney;
    List<String> cho_member = new ArrayList<>();
    List<String> han_member = new ArrayList<>();

    @Override
    public void onEnable() {
        game = false;
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private BukkitTask timerTask = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("mc")) {
            Player player_sender = (Player) sender;
            String name = player_sender.getName();

            if (args.length == 0) {
                sender.sendMessage("==================YbChohan==================");
                sender.sendMessage("mc create <金額> : 新しい丁半のゲームを作成します");
                sender.sendMessage("mc c : 開催されているゲームに丁で参加します");
                sender.sendMessage("mc h : 開催されているゲームに半で参加します");
                sender.sendMessage("==================YbChohan==================");
                return true;
            }

            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("create")) {
                    sender.sendMessage(ChatColor.RED + "金額を決めてください");
                    return false;
                }

                if (args[0].equalsIgnoreCase("c")){
                    if (!game) {
                        sender.sendMessage(ChatColor.RED + "ゲームはまだ開催されていません");
                        sender.sendMessage(ChatColor.GREEN + "/mc create <金額>でゲームを開催しよう！");
                        return false;
                    }

                    if (game){
                        /* お金の判定
                        if(所持金 < betmoney){
                           Sender.sendMessage("所持金が足りないので、参加できません！")
                           retern false;
                         }
                         */
                        if(cho_member.contains(name)){
                            sender.sendMessage(ChatColor.RED + "あなたはすでに丁に参加しています！");
                            return false;
                        } else if(han_member.contains(name)) {
                            Bukkit.getServer().broadcastMessage(ChatColor.RED + name + "が丁に移動しました！");
                            han_member.remove(name);
                            cho_member.add(name);
                        } else {
                            Bukkit.getServer().broadcastMessage(ChatColor.RED + name + "が丁に参加しました！");
                            allmoney += betmoney;
                            cho_member.add(name);
                            return true;
                        }

                    }
                }
                if (args[0].equalsIgnoreCase("h")){
                    if (!game) {
                        sender.sendMessage(ChatColor.RED + "ゲームはまだ開催されていません");
                        sender.sendMessage(ChatColor.GREEN + "/mc create <金額>でゲームを開催しよう！");
                        return false;
                    }

                    if (game){
                        /* お金の判定
                        if(所持金 < betmoney){
                           Sender.sendMessage("所持金が足りないので、参加できません！)
                           retern false;
                         }
                         */
                        //仮のプログラム
                        if(han_member.contains(name)){
                            sender.sendMessage(ChatColor.BLUE + "あなたはすでに半に参加しています！");
                            return false;
                        } else if(cho_member.contains(name)) {
                            Bukkit.getServer().broadcastMessage(ChatColor.BLUE + name + "が半に移動しました！");
                            cho_member.remove(name);
                            han_member.add(name);
                        } else {
                            Bukkit.getServer().broadcastMessage(ChatColor.BLUE + name + "が半に参加しました！");
                            allmoney += betmoney;
                            han_member.add(name);
                            return true;
                        }

                    }
                }
            }

            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("create")) {
                    if (game) {
                        sender.sendMessage(ChatColor.RED + "ゲームはすでに開催されています");
                        sender.sendMessage(ChatColor.GREEN + "/mc cで丁に、/mc hで半に参加しよう！");
                        return false;
                    }

                    if (!game) {
                        try {
                            betmoney = Integer.parseInt(args[1]);
                            game = true;
                            CountDownTimer(name);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(ChatColor.RED + "コマンドが間違っています");
                            sender.sendMessage(ChatColor.GREEN + "/mcでコマンドを確認しよう！");
                        }
                    }
                }
            }
        }
        return false;
    }
    public void CountDownTimer(String name){
        timerTask = new BukkitRunnable(){
            int time = 80;
            @Override
            public void run() {
                time -= 20;
                if (time <= 0) {
                    if (!(!cho_member.isEmpty() && !han_member.isEmpty())) {
                        Bukkit.getServer().broadcastMessage(ChatColor.RED + name + "の丁半の募集は人数不足のため終了しました");
                        end();
                        this.cancel();
                    }
                    else if (cho_member.size() + han_member.size() == 3){
                        Bukkit.getServer().broadcastMessage(ChatColor.RED + name + "の丁半は賭けが成立しないため終了しました");
                        end();
                        this.cancel();
                    } else {
                        Game();
                        this.cancel();
                    }
                }
                if (time > 0){
                    Bukkit.getServer().broadcastMessage(name + "が" + betmoney + "円丁半を募集しています！");
                    Bukkit.getServer().broadcastMessage("残り募集時間"+ time + "秒です");
                }
            }
        }.runTaskTimer(this,0,20*20L);
    }

    public void Game(){
        Bukkit.getServer().broadcastMessage("さいころを振っています...");
        Bukkit.getScheduler().runTaskLater(this, () -> {
            int dice = (int) ((Math.random()*6 + 1) + (Math.random()*6 + 1));
            Bukkit.getServer().broadcastMessage(dice + "が出ました！");
            if (dice % 2 == 0) {
                Bukkit.getServer().broadcastMessage("丁の勝ち！");
                for (String s : cho_member) Bukkit.getServer().broadcastMessage(s + "の勝ち！");
                end();
                // 丁のメンバーにお金を増やす
            } else {
                Bukkit.getServer().broadcastMessage("半の勝ち！");
                for (String s : han_member) Bukkit.getServer().broadcastMessage(s + "の勝ち！");
                end();
                    //半のメンバーにお金を増やす
            }
        }, 2 * 20);
    }
    public void end(){
        game = false;
        betmoney = 0;
        allmoney = 0;
        cho_member.clear();
        han_member.clear();
    }
}