package me.fun.woder.BirthDay;

import java.util.Calendar;
import java.util.TimerTask;

import org.bukkit.Bukkit;

public class Runner extends TimerTask{
public static BirthDay plugin;
	
	public Runner(BirthDay instance) {
		plugin = instance;
	}

	@Override
	public void run() {
		    Calendar cal = Calendar.getInstance();
		    String text1 = "";
		    int day_2 = cal.get(Calendar.DATE);
	        int month_2 = cal.get(Calendar.MONTH) + 1;
		    boolean on = false;
			for(int i = 0; i < BirthDay.Broadcast.length; i++){
				if(BirthDay.Broadcast[i] != null){
				 if(day_2 == Integer.parseInt(BirthDay.Broadcast[i].split("~")[1]) && month_2 == Integer.parseInt(BirthDay.Broadcast[i].split("~")[2])){
				  if(on){
					 text1 = text1 + ", " + BirthDay.Broadcast[i].split("~")[0];
				  }else{
					 text1 = BirthDay.Broadcast[i].split("~")[0];
					 on = true;
				  }
				 }else{
					 BirthDay.Broadcast[i] = null;
				 }
				}
			}
			String text = "";
			text = plugin.TimedMessage.replace("&", "§");
			text = text.replace("%g", BirthDayPlayerListener.list());
			text = text.replace("%p", text1);
		  if(text1.equalsIgnoreCase("") || text1 == null){
			  
		  }else{
			Bukkit.getServer().broadcastMessage(text);
	      }
	}
}
