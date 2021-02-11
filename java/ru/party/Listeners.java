package ru.party;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftItem;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.minecraft.server.v1_13_R2.EntityOcelot;
import net.minecraft.server.v1_13_R2.EntityZombie;
import net.minecraft.server.v1_13_R2.PacketPlayOutEntityDestroy;
public class Listeners implements Listener{
	
	static ArrayList<UUID> cats = new ArrayList<UUID>();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
	}
	
	
	
	@EventHandler
	public void zombieDead(EntityDeathEvent e) {
		LivingEntity le = e.getEntity();
		if(le instanceof Zombie && le.getUniqueId() != tempID) {
			Location loc = le.getLocation();
			Ocelot cat = (Ocelot) le.getWorld().spawnEntity(loc, EntityType.OCELOT);
			cat.setCustomName(randomName(5));
			cat.setCustomNameVisible(true);
			cat.setAI(true);
			
			
		
			
			Zombie zom = (Zombie) le.getWorld().spawnEntity(loc, EntityType.ZOMBIE);	
			
			
			zom.setInvulnerable(true);
			zom.getEquipment().setItemInMainHand(new ItemStack(Material.AIR));
			
			
			cat.addPassenger(zom);		
			
			
			
			List<Entity> entities = cat.getNearbyEntities(10, 2, 10);
			for(Entity ent : entities) {
				if(ent instanceof Player) {
					cat.setTarget((Player) ent);
				}
			}
			
			
			
			
			
			cats.add(cat.getUniqueId());
			

			for( Player p : Bukkit.getServer().getOnlinePlayers()) {
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(zom.getEntityId());
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
            }
			
			tempID = zom.getUniqueId();
		}
	}
	
	public UUID tempID;
	
	
	@EventHandler
	public void damageCat(EntityDamageByEntityEvent e) {
		if(e.getEntity() instanceof Ocelot
				&& cats.contains(e.getEntity().getUniqueId())
				&& e.getDamager() instanceof Player) {
		
		
		Ocelot cat = (Ocelot) e.getEntity();
		Player p = (Player) e.getDamager();
		
		cat.setTarget(p);
		}
	}
	
	
	
	@EventHandler
	public void catDies(EntityDeathEvent e) {
		UUID uid = e.getEntity().getUniqueId();
		
		if(e.getEntity() instanceof Zombie) {
				e.getDrops().clear();
			
		}
		
		
		if(cats.contains(uid)){
		
		List<Entity> list = e.getEntity().getPassengers();
		Iterator iter = list.iterator();
		while(iter.hasNext()) {
			LivingEntity le = (LivingEntity) iter.next();
			le.setHealth(0);
			
		
		e.getDrops().clear();
		e.getDrops().add(kozha());
		}
		SQLite_Manager.addParams(System.currentTimeMillis(), e.getEntity().getKiller().getName(), e.getEntity().getCustomName());
		
		cats.remove(uid);
		}
	}
	
	
	
	
	
	
	public static ItemStack kozha() {
		ItemStack item = new ItemStack(Material.LEATHER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("Super Kozha");
		item.setItemMeta(meta);
		return item;
	}
	
	
	
	public String randomName(int num) {
		int z = 0;
		String nick = "";
		Random random = new Random();
		String str = random.ints(48, 122 + 1)
		  .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
		  .limit(num)
		  .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
		  .toString();
		nick = str;
		
		return nick;
	}
	
	
	
}

	
	
	


