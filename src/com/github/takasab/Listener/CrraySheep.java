package com.github.takasab.Listener;

import com.github.takasab.Game.GamePool;
import com.github.takasab.Game.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;


/*
 *@author:yudi
 *@QQ:1277832129
 *@Twitter:yudi_327
 (○´∀｀○)ﾉ
 */
public class CrraySheep {
/*Link start!*/
    @EventHandler
    void onCrray(PlayerInteractEntityEvent event){
        if(GamePool.getPlayerIn(event.getPlayer())!=null){
            User user = new User(event.getPlayer());
            if(user.isHandItem("神奇的马鞍")){
                event.getPlayer().setPassenger(event.getRightClicked());
            }
        }
    }
}