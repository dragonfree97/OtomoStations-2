package net.robynetzan.otomostations;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class OtomoStations extends org.bukkit.plugin.java.JavaPlugin implements Listener
{
  private static Plugin plugin;
  public static String pluginTitle = ChatColor.RED + "[" + ChatColor.GOLD + "OtomoStations" + ChatColor.RED + "] " + ChatColor.GRAY;
  
  public OtomoStations() {}
  
  public void onEnable() {
    plugin = this;
    getServer().getPluginManager().registerEvents(new BrickBlockListener(), this);
    getServer().getPluginManager().registerEvents(new SignClickListener(), this);
    getServer().getPluginManager().registerEvents(new VehiclePlaceSpeedLimit(), this);
    getServer().getPluginManager().registerEvents(this, this);
  }
  


  public void onDisable() {}
  

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
  {
    if (cmd.getName().equalsIgnoreCase("station")) {
      if ((sender instanceof Player)) {
        if (args.length < 1)
        {
          return false;
        }
        Player player = (Player)sender;
        if (player.hasPermission("otomostations.station")) {
          String station = "";
          for (int i = 0; i < args.length; i++) {
            station = station + args[i];
            if (i != args.length - 1) {
              station = station + " ";
            }
          }
          setStation(player, station, this, Boolean.valueOf(true));
          return true;
        }
      }
      else
      {
        sender.sendMessage(pluginTitle + "You must be a player!");
        return false;
      }
    } else { if (cmd.getName().equalsIgnoreCase("getstation")) {
        if ((sender instanceof Player)) {
          Player player = (Player)sender;
          if (player.hasPermission("otomostations.getstation")) {
            player.sendMessage(pluginTitle + "Your current destination is " + (String)getStation(player, this) + ".");
            return true;
          }
          player.sendMessage(pluginTitle + "You don't have permission to do that.");
          return true;
        }
        
        sender.sendMessage(pluginTitle + "You must be a player!");
        return false;
      }
      if (cmd.getName().equalsIgnoreCase("setstation")) {
        if (args.length < 1) {
          return false;
        }
        String station = "";
        for (int i = 0; i < args.length; i++) {
          station = station + args[i];
          if (i != args.length - 1) {
            station = station + " ";
          }
        }
        if ((sender instanceof Player)) {
          Player player = (Player)sender;
          if (getConfig().getBoolean(station + ".set")) {
            if ((player.hasPermission("otomostations.overwritestation")) && (player.hasPermission("otomostations.setstation"))) {
              double x = player.getLocation().getX();
              double y = player.getLocation().getY();
              double z = player.getLocation().getZ();
              getConfig().set(station + ".x", Double.valueOf(x));
              getConfig().set(station + ".y", Double.valueOf(y));
              getConfig().set(station + ".z", Double.valueOf(z));
              getConfig().set(station + ".set", Boolean.valueOf(true));
              saveConfig();
              sender.sendMessage(pluginTitle + station + " exit set to (" + x + ", " + y + ", " + z + ")");
              return true;
            }
            player.sendMessage(pluginTitle + "You don't have permission to do that.");
            return true;
          }
          
          if (player.hasPermission("otomostations.setstation")) {
            double x = player.getLocation().getX();
            double y = player.getLocation().getY();
            double z = player.getLocation().getZ();
            getConfig().set(station + ".x", Double.valueOf(x));
            getConfig().set(station + ".y", Double.valueOf(y));
            getConfig().set(station + ".z", Double.valueOf(z));
            getConfig().set(station + ".set", Boolean.valueOf(true));
            saveConfig();
            sender.sendMessage(pluginTitle + station + " exit set to (" + x + ", " + y + ", " + z + ")");
            return true;
          }
          player.sendMessage(pluginTitle + "You don't have permission to do that.");
          return true;
        }
        
      }
      else if (cmd.getName().equalsIgnoreCase("delstation")) {
        if (args.length < 1) {
          return false;
        }
        String station = "";
        for (int i = 0; i < args.length; i++) {
          station = station + args[i];
          if (i != args.length - 1) {
            station = station + " ";
          }
        }
        if ((sender instanceof Player)) {
          Player player = (Player)sender;
          if (player.hasPermission("otomostations.deletestation")) {
            getConfig().set(station + ".set", Boolean.valueOf(false));
            saveConfig();
            player.sendMessage(pluginTitle + station + " exit removed.");
            return true;
          }
          player.sendMessage(pluginTitle + "You don't have permission to do that.");
          return true;
        }
        

        sender.sendMessage(pluginTitle + "You must be a player!");
        return false;
      }
    }
    


    return false;
  }
  
  public static void setStation(Player p, String station, OtomoStations plugin, Boolean display) {
    p.setMetadata("station", new FixedMetadataValue(plugin, station));
    if (display.booleanValue()) {
      p.sendMessage(pluginTitle + "Destination station set to " + station + ".");
    }
  }
  
  public Object getStation(Player p, OtomoStations plugin) {
    java.util.List<MetadataValue> values = p.getMetadata("station");
    for (MetadataValue value : values)
    {
      if (value.getOwningPlugin() == plugin) {
        return value.value();
      }
    }
    return null;
  }
  

  @EventHandler
  public void onCartMove(VehicleMoveEvent event)
  {
    List<Entity> passengers = event.getVehicle().getPassengers();
    if (passengers.size() == 0) {
    	return;
    }
    Entity passenger = passengers.get(0);
    Vehicle m = event.getVehicle();
    
    if ((event.getFrom().getBlock().getRelative(0, -1, 0).getType() == Material.OBSIDIAN) && ((event.getFrom().getBlock().getRelative(0, -2, 0).getBlockData() instanceof org.bukkit.block.data.type.Sign))) {
      Sign speedSign = (Sign)event.getFrom().getBlock().getRelative(0, -2, 0).getState();
      
      if (speedSign.getLine(0).equals("[Speed]")) {
        int speed = 1;
        boolean failed = false;
        try {
          speed = Integer.parseInt(speedSign.getLine(1));
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
          
          Minecart cart = (Minecart)m;
          double newspeed = 0.4D * speed;
          if (newspeed != cart.getMaxSpeed()) {
            passenger.sendMessage(pluginTitle + "Speed set to " + speed);
          }
          cart.setMaxSpeed(newspeed);
        }
      }
    }
    
    if ((event.getFrom().getBlock().getRelative(0, -1, 0).getType() == Material.BRICK) && ((event.getFrom().getBlock().getRelative(0, -2, 0).getBlockData() instanceof org.bukkit.block.data.type.Sign))) {
      Sign sign = (Sign)event.getFrom().getBlock().getRelative(0, -2, 0).getState();
      
      if ((sign.getLine(0).equals("[Station]")) && 
        (!m.isEmpty()) && 
        (passenger.getType() == org.bukkit.entity.EntityType.PLAYER)) {
        String destination = (String)getStation((Player)passenger, this);
        if (sign.getLine(1).equalsIgnoreCase(destination)) {
          org.bukkit.World world = m.getWorld();
          m.remove();
          if (getConfig().getBoolean(destination + ".set")) {
            Location exit = new Location(world, getConfig().getDouble(destination + ".x"), getConfig().getDouble(destination + ".y"), getConfig().getDouble(destination + ".z"));
            passenger.teleport(exit);
          }
          passenger.sendMessage(pluginTitle + "Welcome to " + destination + "!");
          setStation((Player)passenger, "", this, Boolean.valueOf(false));
        }
      }
    }
  }
  

  public static Plugin getPlugin()
  {
    return plugin;
  }
}
