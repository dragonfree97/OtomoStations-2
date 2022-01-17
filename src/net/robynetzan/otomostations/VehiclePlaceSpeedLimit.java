package net.robynetzan.otomostations;

import org.bukkit.entity.Minecart;
import org.bukkit.event.vehicle.VehicleCreateEvent;

public class VehiclePlaceSpeedLimit implements org.bukkit.event.Listener
{
  public VehiclePlaceSpeedLimit() {}
  
  @org.bukkit.event.EventHandler
  public void onPlaceVehicle(VehicleCreateEvent event)
  {
    Minecart cart = (Minecart)event.getVehicle();
    cart.setMaxSpeed(0.4D);
    org.bukkit.util.Vector vec = new org.bukkit.util.Vector(1, 1, 1);
    cart.setFlyingVelocityMod(vec);
  }
}
