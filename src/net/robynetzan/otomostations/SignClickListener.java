package net.robynetzan.otomostations;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignClickListener implements org.bukkit.event.Listener
{
  OtomoStations plugin = (OtomoStations)OtomoStations.getPlugin();
  
  public SignClickListener() {}
  
  @EventHandler
  public void onClickSign(PlayerInteractEvent event) { 
	Player p = event.getPlayer();
    if ((event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) && 
      (event.hasBlock()) && (event.getClickedBlock().getBlockData() instanceof org.bukkit.block.data.type.Sign)) {
      Sign clickedSign = (Sign)event.getClickedBlock().getState();
      if (clickedSign.getLine(0).equals("[Station]")) {
        OtomoStations.setStation(p, clickedSign.getLine(1), plugin, Boolean.valueOf(true));
      }
    }
  }
}
