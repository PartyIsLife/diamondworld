package ru.party;

import java.io.File;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;


public class Main extends JavaPlugin implements Listener {

	public static ProtocolManager pm;
	public static Plugin api;
	
	public void onEnable() {
		api = this;
		File f = new File("plugins/ocelot");
		f.mkdir();
		try {
			SQLite_Manager.SQLiteManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		pm = ProtocolLibrary.getProtocolManager();
		getServer().getPluginManager().registerEvents(new Listeners(), api);
		
		pm.addPacketListener(new PacketAdapter(this, 
		        PacketType.Play.Server.SPAWN_ENTITY) {
		    @Override
		    public void onPacketSending(PacketEvent event){
		        PacketContainer packet = event.getPacket();
		        Entity ent = packet.getEntityModifier(event).read(0);
		        if(!(ent instanceof CraftItem)) return;
		    	CraftItem item = (CraftItem) packet.getEntityModifier(event).read(0); 
		    	if(item.getItemStack().equals(Listeners.kozha())) {					  
		    																		  
		            packet = event.getPacket().deepClone();
		            event.setPacket(packet);
			    	item = (CraftItem) packet.getEntityModifier(event).read(0);
		            
			    	item.setCustomName(event.getPlayer().getName());
			    	item.setCustomNameVisible(true);
			    	
			    	
		    	}
		    }
		});
	   
	}
	
	
	public void onDisable() {
		Listeners.cats.clear();
	}
	
	

	
}
