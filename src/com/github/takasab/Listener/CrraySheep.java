package com.github.takasab.Listener;

import com.github.takasab.Game.GamePool;
import com.github.takasab.Game.User;
import org.bukkit.DyeColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Sheep;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.potion.PotionEffectType;


/*
 *@author:yudi
 *@QQ:1277832129
 *@Twitter:yudi_327
 (○´∀｀○)ﾉ
 */
public class CrraySheep implements Listener{
/*Link start!*/
    //右键举起小羊
    @EventHandler
    void onCrray(PlayerInteractEntityEvent event){
        System.out.print("listen");
        if(GamePool.getPlayerIn(event.getPlayer())!=null){
            System.out.print("crray");
            User user = new User(event.getPlayer());
            if(user.isHandItem("神奇的马鞍")){
                if(event.getRightClicked() instanceof Sheep) {
                    Sheep target = (Sheep) event.getRightClicked();
                    if(target.getColor()== DyeColor.BLACK){
                        target.getWorld().createExplosion(target.getLocation(),0);
                        event.getPlayer().damage(5D);
                        return;
                    }
                    if(target.getColor()==DyeColor.getByColor(user.getColor())){
                        return;
                    }

                    System.out.print("add");
                    user.addSheep((Sheep)event.getRightClicked());
                    try {
                        ((LivingEntity) event.getRightClicked()).removePotionEffect(PotionEffectType.SLOW);
                    }catch (Exception e){}
                }
            }
        }
    }
}
