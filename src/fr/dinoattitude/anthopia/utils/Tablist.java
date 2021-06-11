package fr.dinoattitude.anthopia.utils;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_16_R2.PlayerConnection;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;

public class Tablist {
	
	public static void Tab(Player player, String header, String footer) {
		if(header == null) header = "";
		if(footer == null) footer = "";
		PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
		
		IChatBaseComponent Title = ChatSerializer.a("{\"text\": \"" + header + "\"}");
		IChatBaseComponent Foot = ChatSerializer.a("{\"text\": \"" + footer + "\"}");
		
		PacketPlayOutPlayerListHeaderFooter headerPacket = new PacketPlayOutPlayerListHeaderFooter();
		
		try {
			Field fieldA = (Field) headerPacket.getClass().getDeclaredField("header");
            fieldA.setAccessible(true);
            fieldA.set(headerPacket, Title);
            fieldA.setAccessible(!fieldA.isAccessible());

			Field field = headerPacket.getClass().getDeclaredField("footer");
			field.setAccessible(true);
			field.set(headerPacket, Foot);
			field.setAccessible(!field.isAccessible());
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			connection.sendPacket(headerPacket);
		}
	}
}
