package com.github.takasab.Game;

import com.github.takasab.GameProcess.Request;
import com.github.takasab.GameProcess.StartThread;
import com.github.takasab.Main;
import com.github.takasab.Request.CarryRequst;
import com.github.takasab.Setting.Setting;
import net.minecraft.server.v1_7_R1.EntityInsentient;
import net.minecraft.server.v1_7_R1.Navigation;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_7_R1.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;

/*
 *@author:yudi
 *@QQ:1277832129
 *@Twitter:yudi_327
 (○´∀｀○)ﾉ
 */
public class Game {
/*Link start!*/
    boolean start = false;
    String name;
    int max;
    int min;

    public boolean gamefirst=false;

    int gametick=0;

    boolean iswait = true;
    Location blue;
    Location green;
    Location yellow;
    Location red;
    Location spawn;
    Location lobby;

    Location sheepblue;
    Location sheepgreen;
    Location sheepyellow;
    Location sheepred;
    HashMap<Color,Integer> map = new HashMap<Color, Integer>();
    List<Player> players = new ArrayList<Player>();
    HashMap<Player,String> job = new HashMap<Player, String>();

    HashMap<Player,Integer> scoremap = new  HashMap<Player,Integer> ();
    Game(String name,int min,int max,
    Location blue,Location green,Location yellow,Location red,Location sheep
    ,Location lobby,Location sheepblue,Location sheepgreen,Location sheepred,Location sheepyellow)
    { this.name=name;this.max=max;this.min=min;
    this.blue=blue;this.green=green;this.yellow=yellow;this.red=red;
        this.spawn=sheep; this.lobby = lobby;
    this.sheepblue=sheepblue;this.sheepgreen=sheepgreen;this.sheepyellow=sheepyellow;this.sheepred=sheepred;
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.handle, new Runnable(){
        @Override
        public void run(){
           if(Game.this.start==false) return;
           Sheep le =  (Sheep) Game.this.spawn.getWorld().spawnCreature(spawn, EntityType.SHEEP);
   
        }
        }, 20*20, 20*20);
        /*

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.handle, new Runnable() {
                    @Override
                    public void run() {
                        if (Game.this.start) {
                           for(LivingEntity le : spawn.getWorld().getLivingEntities()){
                               if(le instanceof Sheep){
                                   if(!le.isOnGround()) {
                                       try {
                                           Thread.sleep(3000);
                                       } catch (InterruptedException e) {
                                           e.printStackTrace();
                                       }
                                       if(!le.isOnGround()){
                                           le.remove();
                                       }
                                   }

                               }
                           }
                        }
                    }
                }, 20, 20);
                */
        //玩家计分板刷新
                

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.handle, new Runnable() {
            @Override
            public void run() {
                if (Game.this.start) {
                    Player player =null;

                    for (Player p : players) {
                        player = p;
                        Objective Obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

                        Scoreboard localScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                        Obj = localScoreboard.registerNewObjective("swalk", "haha");
                        Obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                        Obj.setDisplayName("§a§l偷羊羊小游戏");
                        p.setScoreboard(localScoreboard);


                        Obj = p.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                        Score s = Obj.getScore(Bukkit.getOfflinePlayer(players.size()+" §6玩家人数"));
                        Score me = Obj.getScore(Bukkit.getOfflinePlayer(new User(p).getScore()+" §6我的积分"));
                        Score l = Obj.getScore(Bukkit.getOfflinePlayer(""+Game.this.getTeamScore(Color.BLUE)+ChatColor.BLUE+" 蓝队"));
                        Score r = Obj.getScore(Bukkit.getOfflinePlayer(""+Game.this.getTeamScore(Color.RED)+ChatColor.RED+" 红队"));
                        Score g = Obj.getScore(Bukkit.getOfflinePlayer(""+Game.this.getTeamScore(Color.GREEN)+ChatColor.GREEN+" 绿队"));
                        Score y = Obj.getScore(Bukkit.getOfflinePlayer(""+Game.this.getTeamScore(Color.YELLOW)+ChatColor.YELLOW+" 黄队"));
                        Score w = Obj.getScore(Bukkit.getOfflinePlayer(""+Game.this.gametick+" §6剩余时间"));
                        
                        s.setScore(7);
                        me.setScore(5);
                        l.setScore(4);
                        r.setScore(3);
                        g.setScore(2);
                        y.setScore(1);
                        w.setScore(6);
                    }
                    int blue = 0;
                    int green =0;
                    int yellow = 0;
                    int red = 0;
                    if(player!=null){
                        if(player.isOnline()){
                            List<Entity> list = player.getNearbyEntities(350,350,350);
                            for(Entity entity:list){
                                if(entity instanceof Sheep){
                                    Sheep sheep = (Sheep) entity;
                                    Color thissheep = ColorTool.getByID(ColorTool.getDyeToColorID(sheep.getColor()));
                                    if(thissheep.equals(Color.GREEN)) green++;
                                    if(thissheep.equals(Color.BLUE)) blue++;
                                    if(thissheep.equals(Color.YELLOW)) yellow++;
                                    if(thissheep.equals(Color.RED)) red++;
                                }
                            }
                        }
                    }
                    setTeamScore(Color.BLUE,blue);
                    setTeamScore(Color.GREEN,green);
                    setTeamScore(Color.YELLOW,yellow);
                    setTeamScore(Color.RED,red);

                }
            }
        }, 10, 10);
        //小羊回家
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.handle, new Runnable(){
            @Override
            public void run() {
                if (Game.this.start == false) {
                    return;
                }
                System.out.print("小羊颜色检测0");
                List<LivingEntity> l1 = spawn.getWorld().getLivingEntities();
                LivingEntity le = null;
                for (int i = 0; i < l1.size(); i++) {
                    System.out.print("小羊颜色检测1");
                    le = l1.get(i);
                    if (le instanceof Sheep) {
                        Sheep sheep = (Sheep) le;
                        if (sheep.getColor().equals(DyeColor.WHITE)) {
                            continue;
                        }
              
                        
                        
                        System.out.print("小羊颜色检测2");
                        if (Game.this.getSheepSpawn(ColorTool.getByID(ColorTool.getDyeToColorID(sheep.getColor()))
                        ) == null) {
                            continue;
                        }
                        Location loc = Game.this.getSheepSpawn(ColorTool.getByID(ColorTool.getDyeToColorID(sheep.getColor())));
                        CraftLivingEntity l = (CraftLivingEntity) le;
                        EntityInsentient en = (EntityInsentient) l.getHandle();
                        Navigation na = en.getNavigation();
                        try{
                        na.a(loc.getX(), loc.getY(), loc.getZ(), 0.9);
                        }catch(Exception e){
                            e.printStackTrace();
                            System.out.print(na);
                            System.out.print(loc);
                        }
                        System.out.print("小羊回家监听器" + loc.getBlockX() + loc.getBlockZ());
                    }

                }
            }
        }, 10, 10);

        //小羊清理
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.handle, new Runnable(){
            @Override
            public void run() {
                if (Game.this.start) {
                    return;
                }
                System.out.print("小羊清理:"+Game.this.start);
                List<LivingEntity> l1 = spawn.getWorld().getLivingEntities();
                LivingEntity le = null;
                for (int i = 0; i < l1.size(); i++) {
                    le = l1.get(i);
                    if (le instanceof Sheep) {
                        le.remove();
                    }
                }
            }
        }, 200, 200);
    }
    public void 设置职业(Player p,String 职业){
        job.put(p,职业);

    }

    public Color getMaxTeam(){
        if(getTeamScore(Color.BLUE)>getTeamScore(Color.YELLOW)){
            if(getTeamScore(Color.BLUE)>getTeamScore(Color.GREEN)){
               if(getTeamScore(Color.BLUE)>getTeamScore(Color.RED)){
                   return Color.BLUE;
               }else{
                   return Color.RED;
               }
            }else if(getTeamScore(Color.GREEN)>getTeamScore(Color.RED)){
                 return Color.GREEN;
            }else {
                return Color.RED;
            }

        }else{
            if(getTeamScore(Color.YELLOW)>getTeamScore(Color.GREEN)){
                 if(getTeamScore(Color.YELLOW)>getTeamScore(Color.RED)){
                     return Color.YELLOW;
                 }else{
                     return Color.RED;
                 }
            }else if(getTeamScore(Color.GREEN)>getTeamScore(Color.RED)){
                return Color.GREEN;
            }else {
                return Color.RED;
            }
            }
        }


    public String 获取玩家职业(Player p){
        return job.get(p);
    }
    public Location getTeamSpawn(Color c){
        if(c.equals(Color.BLUE)) return blue;
        if(c.equals(Color.RED)) return red;
        if(c.equals(Color.YELLOW)) return yellow;
        if(c.equals(Color.GREEN)) return green;
        return null;
    }

    public Location getSheepSpawn(Color c){
        if(c.equals(Color.BLUE)) return sheepblue;
        if(c.equals(Color.RED)) return sheepred;
        if(c.equals(Color.YELLOW)) return sheepyellow;
        if(c.equals(Color.GREEN)) return sheepgreen;
        return null;
    }

    public int getTeamScore(Color c){
        if(!map.containsKey(c)) return 0;
        return map.get(c);
    }
    public void setTeamScore(Color color, int i){
        map.put(color,i);
    }
    public void clearTeamScore(){
        map.put(Color.BLUE, 0);
        map.put(Color.RED, 0);
        map.put(Color.YELLOW, 0);
        map.put(Color.GREEN,0);
    }
    public void addTeamScore(Color color,int amount){
        map.put(color,amount+this.getTeamScore(color));
    }
    public void start(){
        this.start=true;
        Random rand = new Random();
        for(Player p:players){
            User user = new User(p);
           if(rand.nextBoolean()){
               if(rand.nextBoolean()){
                 user.setColor(Color.BLUE);
               }else{
                 user.setColor(Color.RED);
               }
           }else{
               if(rand.nextBoolean()){
                 user.setColor(Color.YELLOW);
               }else{
                 user.setColor(Color.GREEN);
               }
           }
        }
        for(Player p:players){
            p.sendMessage(ChatColor.RED+"游戏开始");
            User user = new User(p);
            p.teleport(this.getTeamSpawn(user.getColor()));
            if(!job.containsKey(p)){
                job.put(p,"berserker");
                p.getInventory().setItem(0, new ItemStack(Material.STONE_AXE));
                     ItemStack saddle = new  ItemStack(Material.SADDLE);
            ItemMeta me = saddle.getItemMeta();
            me.setDisplayName(ChatColor.RED+"神奇的马鞍");
            saddle.setItemMeta(me);
            p.getInventory().setItem(1, saddle);
            p.updateInventory();
             scoremap.put(p, user.getScore());
                continue;
            }
            String 职业 = job.get(p);
            if (职业.equalsIgnoreCase("berserker"))
                p.getInventory().setItem(0, new ItemStack(Material.STONE_AXE));
            if (职业.equalsIgnoreCase("archer")) {
                p.getInventory().setItem(0, new ItemStack(Material.BOW));
                p.getInventory().setItem(15, new ItemStack(Material.ARROW, 16));
            }
            if (职业.equalsIgnoreCase("saber"))
                p.getInventory().setItem(0, new ItemStack(Material.WOOD_SWORD));
            
            ItemStack saddle = new  ItemStack(Material.SADDLE);
            ItemMeta me = saddle.getItemMeta();
            me.setDisplayName(ChatColor.RED+"神奇的马鞍");
            saddle.setItemMeta(me);
            p.getInventory().setItem(1, saddle);
            p.updateInventory();
            scoremap.put(p, user.getScore());
            
        }

        new Thread(){
            public void run(){
                gametick=Setting.WAITTIME;
                while(true){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(gametick>0){
                        gametick--;
                    }else{
                        break;
                    }
                }
            }

        }.start();

        //结束线程
        new  Thread(){
            public void run(){
                try {
                    sleep(Setting.WAITTIME* 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
System.out.print("游戏结束调试输出:正在进行");
                start = false;
                iswait = true;
                gamefirst = false;
                for (Player p : getPlayers()) {
                    if(!p.isOnline()) continue;
                    safeLeave(p);
                    User user = new User(p);

                    System.out.print("游戏结束调试输出:"+p.getName());
                    p.getInventory().setChestplate(null);
                    
                    p.sendMessage(ChatColor.BLUE + "===========================" + ChatColor.RED + "游戏结束" + ChatColor.BLUE + "===========================");
                    
                    p.sendMessage(ChatColor.BLUE + "获得积分:" + ChatColor.GREEN + (user.getScore()-scoremap.get(p)));
                    p.sendMessage(ChatColor.BLUE + "本队得分:" + ChatColor.RED + getTeamScore(user.getColor()));
                    p.sendMessage(ChatColor.BLUE+"蓝队得分:" + Game.this.getTeamScore(Color.BLUE) + ChatColor.GREEN+"                        绿队得分:" + getTeamScore(Color.GREEN));
                    p.sendMessage(ChatColor.YELLOW+"黄队得分:" + Game.this.getTeamScore(Color.YELLOW) +ChatColor.RED+ "                        红队得分:" + getTeamScore(Color.RED));
                    if(user.getColor().equals(getMaxTeam())){
                        p.sendMessage("恭喜你们队伍获胜 获得10积分奖励");
                        user.addScore(10);
                    }
                    p.sendMessage("游戏结束获得参与积分:10");

                    user.addScore(10);
                 
                }
                //清理残余
                Bukkit.getScheduler().scheduleSyncDelayedTask(Main.handle, new Runnable() {
                    public void run() {
                        List<LivingEntity> l1 = spawn.getWorld().getLivingEntities();
                        LivingEntity le = null;
                        for (int i = 0; i < l1.size(); i++) {
                            le = l1.get(i);
                            if (le instanceof Sheep) {
                                le.remove();
                            }
                        }
                        players.clear();
                        Game.this.clearTeamScore();
                        scoremap.clear();
                        System.out.print("完成");
                    }
                }, 10);
            }
        }.start();
        //小羊集合线程

        //小羊生成线程


    }
    public void broadcast(String str){
        for(Player p:players)
            p.sendMessage(str);
    }
    public synchronized boolean contains(Player player){
        return players.contains(player);
    }
    public void join(Player player){
        if(players.contains(player)){return;}
        if(!start) {
            if(players.size()>max){
                player.sendMessage(ChatColor.AQUA+"人满为患,请换其他房间");
                return;
            }
            player.getInventory().clear();
            players.add(player);
            //开始监听玩家手上的物品 如果不为马鞍的时候 移除小羊
            for(Player p:players){
                p.sendMessage(ChatColor.YELLOW+"玩家 "+player.getName()+" 进入游戏");
            }
            Request req = new CarryRequst(this,player);
            GamePool.pushRequst(req);
            player.teleport(lobby);
            if(players.size()>=min){
                for(Player p:players){
                    p.sendMessage("人数已经达到要求游戏将在10秒内开始,请做好准备");
                    if(iswait){
                        new Thread(new StartThread(this)).start();
                    }
                    iswait = false;
                }
            }
        }
    }
    public void died(Player player){
        player.sendMessage(ChatColor.RED+"你死亡了,请继续加油.你将暂停行动5秒,请耐心等待");
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*5,20*5));
        player.setHealth(player.getMaxHealth());
        User user = new User(player);
        user.addScore(-1);
        player.teleport(this.getTeamSpawn(user.getColor()));
    }
    public List<Player> getPlayers(){
        return players;
    }
    public void left(final Player player){
        if(players.contains(player))
        players.remove(player);
        if(player.isOnline()) {
            player.teleport(GamePool.lobby);
            player.getInventory().clear();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));
            player.getInventory().setChestplate(new ItemStack(Material.AIR));

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.handle, new Runnable(){
                @Override
                public void run() {

                    Objective Obj = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

                    Scoreboard localScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    Obj = localScoreboard.registerNewObjective("swalk", "haha");
                    Obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                    Obj.setDisplayName("§a§l偷羊羊小游戏");
                    player.setScoreboard(localScoreboard);


                    Obj = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                    Score s = Obj.getScore(Bukkit.getOfflinePlayer("§6期待你的下次胜利"));
                    s.setScore(1);
                }
            }, 10);

        }
        //正在写
    }
    public void safeLeave(final Player player){
        if(player.isOnline()) {
            player.teleport(GamePool.lobby);
            player.getInventory().clear();
            player.getInventory().setHelmet(new ItemStack(Material.AIR));

            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.handle, new Runnable(){
                @Override
                public void run() {

                    Objective Obj = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);

                    Scoreboard localScoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
                    Obj = localScoreboard.registerNewObjective("swalk", "haha");
                    Obj.setDisplaySlot(DisplaySlot.SIDEBAR);
                    Obj.setDisplayName("§a§l偷羊羊小游戏");
                    player.setScoreboard(localScoreboard);


                    Obj = player.getScoreboard().getObjective(DisplaySlot.SIDEBAR);
                    Score s = Obj.getScore(Bukkit.getOfflinePlayer("§6期待你的下次胜利"));
                    s.setScore(0);
                }
            }, 10);

        }
        //正在写
    
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Game){
            Game ga = (Game) obj;
            return ga.name.equals(this.name);
        }
        return false;
    }
    @Override
    public int hashCode(){
        return name.hashCode();
    }

    static class ByValueComparator implements Comparator<String> {
        HashMap<Color, Integer> base_map;

        public ByValueComparator(HashMap<Color, Integer> base_map) {
            this.base_map = base_map;
        }

        public int compare(String arg0, String arg1) {
            if (!base_map.containsKey(arg0) || !base_map.containsKey(arg1)) {
                return 0;
            }

            if (base_map.get(arg0) < base_map.get(arg1)) {
                return 1;
            } else if (base_map.get(arg0) == base_map.get(arg1)) {
                return 0;
            } else {
                return -1;
            }
        }
    }
}
