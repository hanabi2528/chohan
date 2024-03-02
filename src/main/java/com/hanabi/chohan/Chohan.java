package com.hanabi.chohan;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

public final class Chohan extends JavaPlugin {
    boolean game;
    int betmoney;
    List<Player> cho = new ArrayList<>();
    List<Player> han = new ArrayList<>();

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
                sender.sendMessage("mc c : 開催されているゲームに半で参加します");
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
                        //仮のプログラム

                        /* お金の判定
                        if(所持金 < betmoney){
                           Sender.sendMessage("所持金が足りないので、参加できません！)
                           retern false;
                         }

                         if(所持金 >= betmoney){
                           Bukkit.getServer().broadcastMessage(name+ "が丁に参加しました！" )
                           cho += 1
                         */




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
                            sender.sendMessage(ChatColor.RED + "コマンドが間違っています、もう一度試してください");
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
                if (time <= 0){
                    Bukkit.getServer().broadcastMessage(name + "の丁半の募集は終了しました");
                    this.cancel();
                    game = false;
                }
                Bukkit.getServer().broadcastMessage(name + "が" + betmoney + "円丁半を募集しています！");
                Bukkit.getServer().broadcastMessage("残り募集時間"+ time + "秒です");
            }
        }.runTaskTimer(this,0,20*20L);
    }
}