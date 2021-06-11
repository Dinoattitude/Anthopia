package fr.dinoattitude.anthopia.portals;



import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import fr.dinoattitude.anthopia.utils.Messages;
import fr.dinoattitude.anthopia.portals.portals_api.PortalData;

public class PortalCommand extends PortalData implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		if(args[0].equalsIgnoreCase("set")) {
			
			Integer coordX;
			Integer coordY;
			Integer coordZ;
			String namePortal;
			
			if(args[1]==null ||args[2]==null || args[3]==null || args[4]==null) {
				sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INCOMPLETE_COMMAND_COORD);
			}
			else {
				try {
					coordX=Integer.valueOf(args[1]);
					coordY=Integer.valueOf(args[2]);
					coordZ=Integer.valueOf(args[3]);
					System.out.println(getPortalName(args[4]));
					if(getPortalName(args[4])==null) {
						namePortal=args[4];
						setPortalData(namePortal, coordX, coordY, coordZ);
					}
					else
						sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INVALIDE_NAME);
				}
				catch(Exception e){
					sender.sendMessage(Messages.PLUGIN_NAME.toString() + Messages.INCORRECT_COMMAND_COORD);
				}
			}	
		}else if(args[0].equalsIgnoreCase("remove")) {
			
		}else if(args[0].equalsIgnoreCase("update")) {
			
		}
		
		
		
		
		return false;
	}

}


