package me.fun.woder.BirthDay;

import java.util.Calendar;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;


/**
 * Handle events for all Player related events
 * @author woder
 */
public class BirthDayPlayerListener implements Listener {
	//You HAVE to have this!
	public static BirthDay plugin;
	
	public BirthDayPlayerListener(BirthDay instance) {
		plugin = instance;
	}
	//You HAVE to have this!
	
	@EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	String Player_Name = player.getName();
    	String Name = Player_Name.toLowerCase();
	  if (plugin.BDUsers.containsKey(Name)) {
		Calendar cal = Calendar.getInstance();
		int day = plugin.BDUsers.get(Name).get(Calendar.DATE);
		int month = plugin.BDUsers.get(Name).get(Calendar.MONTH);
		int year = plugin.BDUsers.get(Name).get(Calendar.YEAR);
        int day_2 = cal.get(Calendar.DATE);
        int month_2 = cal.get(Calendar.MONTH) + 1;
        int year_2 = cal.get(Calendar.YEAR);
		if (day == day_2 && month == month_2 && year == year_2) {
			String text = plugin.G_Message.replace("&", "§");
			text = text.replace("%p", player.getName());
			text = text.replace("%g", list());
			Bukkit.getServer().broadcastMessage(text);
			text = plugin.Birthday_Message.replace("&", "§");
			text = text.replace("%p", player.getName());
			text = text.replace("%g", list());
            player.sendMessage(text);
           for(int its = 0; its < plugin.Number_Of_gift.length; its++){
           if(plugin.Number_Of_gift[its] != null){
            int cnt = Integer.parseInt(plugin.Number_Of_gift[its].replace(" ", ""));
            Material m = Material.getMaterial(Integer.parseInt(plugin.Gift_On_Birthday[its].replace(" ", "")));
            Player target = player;
            byte dam = 0;
            ItemStack stack = new ItemStack(m, cnt, (short)dam);
    		target.getInventory().addItem(stack);
           }
           }
    		/*if(plugin.Iconomy_Money != 0){
    			EconomyResponse r = BirthDay.econ.depositPlayer(player.getName(), plugin.Iconomy_Money);
                if(r.transactionSuccess()) {
                    player.sendMessage(String.format("You were given %s and now have %s", BirthDay.econ.format(r.amount), BirthDay.econ.format(r.balance)));
                } else {
                    player.sendMessage(String.format("An error occured: %s", r.errorMessage));
                }
    		}*/
            cal.set(year + 1, month, day);
            int possition = BirthDay.Possition();
            BirthDay.Broadcast[possition] = Name + "~" + day + "~" + month;
            BirthDayPlayerListener.plugin.BDUsers.put(Name, cal);
		    }
     }
    }
    
    public static String list(){
    	String Output = "";
    	boolean on = false;
    	for(int its = 0; its < plugin.Number_Of_gift.length; its++){
            if(plugin.Number_Of_gift[its] != null && plugin.Gift_On_Birthday[its] != null){
             if(on){
              Output = Output + ", " + ItemList.ID.get(plugin.Gift_On_Birthday[its].replace(" ", "")) + ":" + plugin.Number_Of_gift[its].replace(" ", ""); 
             }else{
              Output = ItemList.ID.get(plugin.Gift_On_Birthday[its].replace(" ", "")) + ":" + plugin.Number_Of_gift[its].replace(" ", "");
              on = true;
             }
            }
    	}
    	return Output;
    }
    
}
