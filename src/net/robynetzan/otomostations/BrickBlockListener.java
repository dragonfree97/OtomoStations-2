package net.robynetzan.otomostations;

import org.bukkit.Material;
import org.bukkit.event.block.BlockPlaceEvent;

public class BrickBlockListener implements org.bukkit.event.Listener
{
  public BrickBlockListener() {}
  
  @org.bukkit.event.EventHandler
  public void onPlaceBlock(BlockPlaceEvent event)
  {
    if ((event.getBlockAgainst().getType() == Material.BRICK) && 
      (event.getBlockPlaced().getBlockData() instanceof org.bukkit.block.data.Rail)) {
      org.bukkit.entity.Player player = event.getPlayer();
      if ((event.getBlockPlaced().getRelative(0, -2, 0).getBlockData() instanceof org.bukkit.block.data.type.Sign)) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign)event.getBlockPlaced().getRelative(0, -2, 0).getState();
        if (sign.getLine(0).equals("[Station]")) {
          player.sendMessage(OtomoStations.pluginTitle + "Station created! Station ID: '" + sign.getLine(1) + "'");
        }
      }
    }
    

    if ((event.getBlockAgainst().getType() == Material.OBSIDIAN) && 
      (event.getBlockPlaced().getBlockData() instanceof org.bukkit.block.data.Rail)) {
      org.bukkit.entity.Player player = event.getPlayer();
      if ((event.getBlockPlaced().getRelative(0, -2, 0).getBlockData() instanceof org.bukkit.block.data.type.Sign)) {
        org.bukkit.block.Sign sign = (org.bukkit.block.Sign)event.getBlockPlaced().getRelative(0, -2, 0).getState();
        if (sign.getLine(0).equals("[Speed]")) {
          int speed = 0;
          boolean failed = false;
          try {
            speed = Integer.parseInt(sign.getLine(1));
          } catch (NumberFormatException error) {
            speed = 1;
            failed = true;
          }
          if (!failed) {
            if (speed > 5) {
              speed = 5;
            }
            if (speed < 1) {
              speed = 1;
            }
            player.sendMessage(OtomoStations.pluginTitle + "Speed sign created! Speed limit set to " + speed);
          } else {
            player.sendMessage(OtomoStations.pluginTitle + "Error: Invalid speed! It must be an integer.");
          }
        }
      }
    }
  }
}
