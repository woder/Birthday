package me.fun.woder.BirthDay;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
//import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Calendar;
import java.util.Timer;

//import com.iConomy.*;

public class BirthDay extends JavaPlugin{
	//public iConomy iConomy = null;
	Logger log = Logger.getLogger("Minecraft");
	static String mainDirectory = "plugins/Birthday"; //sets the main directory for easy reference
	public HashMap<String, Calendar> BDUsers = new HashMap<String, Calendar>();
	public final BirthDayPlayerListener playerListener = new BirthDayPlayerListener(this);
	public static String[] Broadcast = new String[1000];
	public String Birthday_Message = "Happy Birthday! Here is a gift!";
	public String[] Gift_On_Birthday = null;
	public String[] Number_Of_gift = null;
	public int Iconomy_Money;
	public String G_Message = "";
	public String TimedMessage = "";
	public int Month_mode = 0;
	public int Date = 0;
	long Time = 0;
	
	private void createConfig() {
		this.getConfig().options().copyDefaults(true);
	}
	
	private void loadConfig() {
		
		Birthday_Message = (String) getConfig().getString("Birthday Message");
		G_Message = (String) getConfig().getString("Global message");
		Gift_On_Birthday = ((String) getConfig().getString("Items on birthday")).split(",");
		Number_Of_gift = ((String)getConfig().getString("Number of gifts")).split(",");
		TimedMessage = ((String)getConfig().getString("Timed message"));
        Iconomy_Money = getConfig().getInt("Message interval", 0);
        Time = getConfig().getInt("Message interval", 0);
        Date = getConfig().getInt("Use american date format", 0);
        Month_mode = getConfig().getInt("Month mode", 0);
        
	}

	@Override
	public void onEnable(){

		if (getDataFolder().exists()) {
			BDUsers = load("birthday.hm");
			if(BDUsers == null){
			   BDUsers = new HashMap<String, Calendar>();
			}
			loadConfig();
		}else{
			getDataFolder().mkdirs();
			createConfig();
		}
		//setupPermissions();
		log.info("BirthDay has been enabled.");
		getServer().getPluginManager().registerEvents(playerListener, this);
		/*pm.addPermission(new Permission("birthday.admin"));
		pm.addPermission(new Permission("birthday.check"));
		pm.addPermission(new Permission("birthday.use"));*/
		ItemList.Call();
		if(Time != 0){
		Timer timer = new Timer();
	    timer.scheduleAtFixedRate(new Runner(this), 0, Time * 60 * 1000);
		}
		
		/*if(!setupEconomy()){
		   Iconomy_Money = 0;
		}*/

	}
	@Override
	public void onDisable(){
		log.info("BirthDay has been disabled.");
		save(BDUsers, "birthday.hm");
	}
	
	/*private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }*/


	@SuppressWarnings("unchecked")
	public HashMap<String, Calendar> load(String filename){
		try{


			@SuppressWarnings("resource")
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getDataFolder() + File.separator
					+ filename));
			Object result = ois.readObject();
			//you can feel free to cast result to HashMap<Player,Boolean> if you know there's that HashMap in the file
			return (HashMap<String, Calendar>) result;

		}catch(Exception e){


			e.printStackTrace();

		}
		return null;

	}

	public void save(HashMap<String, Calendar> pluginEnabled, String filename) {

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(getDataFolder() + File.separator
					+ filename));

			oos.writeObject(pluginEnabled);
			oos.flush();
			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static int Possition(){
		int Posstion = 0;
		for(int i = 0; i < Broadcast.length; i++){
			if(Broadcast[i] == null){
			  Posstion = i;
			  break;
			}
		}
		return Posstion;
    }
	
	public boolean check(String name){
		Calendar cal = Calendar.getInstance();
		boolean Output = false;
		if(this.BDUsers.containsKey(name)){
		   if(this.BDUsers.get(name).get(Calendar.MONTH) + 1 < cal.get(Calendar.MONTH) + 1){
			  Output = true;
		   }
		}
		return Output;
	}

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String cmd = command.getName();
		Player player = (Player) sender;
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DATE);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		String day_L = String.valueOf(day);
		String month_L = String.valueOf(month);
		String Player_Name = player.getName();
		String Name = Player_Name.toLowerCase();
		String Name2 = null;
		int[] Days_Of_Months = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		boolean blnExists = BDUsers.containsKey(Name);
		int day_checker = 0;
		int month_checker = 0;
		if (cmd.equalsIgnoreCase("birthday")) {
			// if (!Basic.permissionHandler.has(player,"test.test")) {
		  if(!player.hasPermission("birthday.use")){
			 player.sendMessage(ChatColor.RED + "You do not have permission to use this!");
			 return true;
		  }
		  if(Date == 0){
			try { 
				month_checker = Integer.parseInt(args[1]); } 
			catch(Exception e) {
				month_checker = 0;
				//e.printStackTrace();
			}
			try { day_checker = Integer.parseInt(args[0]); }   
			catch(Exception e) {
				day_checker = 0;
				//e.printStackTrace();
			}
		  }else{
			try { 
				month_checker = Integer.parseInt(args[0]); } 
		    catch(Exception e) {
					month_checker = 0;
					//e.printStackTrace();
			}
			try { 
			    day_checker = Integer.parseInt(args[1]); }   
		    catch(Exception e) {
				day_checker = 0;
					//e.printStackTrace();
			}  
		  }
				if (day_checker == 0||month_checker == 0 ){
				  if(Date == 0){
					player.sendMessage(ChatColor.RED + "/birthday <day> <month>");
				  }else{
					player.sendMessage(ChatColor.RED + "/birthday <month> <day>");
				  }
					return true;
				}else{
					if (month_checker > 12) {
						player.sendMessage(ChatColor.RED + "They are only 12 months!!");
						return true;
					}
					if (Days_Of_Months[month_checker-1] >= day_checker){
						if (blnExists == true){
						  if(Month_mode == 1){
							if(check(player.getName().toLowerCase())){
								if (args[0].equals(day_L) & args[1].equals(month_L)){
									player.sendMessage(ChatColor.RED + "Can not set birthday to current day!");
									return true;
								}else{
								  if(Date == 0){
									player.sendMessage(ChatColor.RED + "Set your birthday to the " + args[0] + "/" + args[1] + "/" + year);
								  }else{
									player.sendMessage(ChatColor.RED + "Set your birthday to the " + args[1] + "/" + args[0] + "/" + year);  
								  }
									cal.set(year, month_checker, day_checker);
									this.BDUsers.put(Name, cal);
								}
								return true;
							}
						  }else{
							player.sendMessage(ChatColor.RED + "You have already set your birthday, ask a admin to change it for you");
						  }
							return true;
						}else{
							if (args[0].equals(day_L) & args[1].equals(month_L)){
								player.sendMessage(ChatColor.RED + "Can not set birthday to current day!");
								return true;
							}else{
							  if(Date == 0){
								player.sendMessage(ChatColor.RED + "Set your birthday to the " + args[0] + "/" + args[1] + "/" + year);
							  }else{
								player.sendMessage(ChatColor.RED + "Set your birthday to the " + args[1] + "/" + args[0] + "/" + year);  
							  }
								cal.set(year, month_checker, day_checker);
								this.BDUsers.put(Name, cal);
							}
							return true;
						}
					}else{ player.sendMessage(ChatColor.RED + "That month does not have that many days!");
					return true;
					}
				}
		}
		else if (cmd.equalsIgnoreCase("birthdayp")) {
			if(!player.hasPermission("birthday.admin")){
				 player.sendMessage(ChatColor.RED + "You do not have permission to use this!");
				 return true;
			}
			if (sender.isOp()){
				if (args.length < 3){
				  if(Date == 0){
					player.sendMessage(ChatColor.RED + "/birthdayp <player> <day> <month>");
				  }else{
					player.sendMessage(ChatColor.RED + "/birthdayp <player> <month> <day>");  
				  }
					return true;
				}else{
				  if(Date == 0){
					try { 
						Player buddy = player.getServer().getPlayer(args[0]); 
						Name2 = buddy.getName().toLowerCase();
					}
					catch(Exception e) { 
							player.sendMessage("Syntax error: /birthdayp <player> <day> <month>");
					}
					try { 
						month_checker = Integer.parseInt(args[2]); } 
					catch(Exception e) { 
							month_checker = 0;
							e.printStackTrace();
					}
					try { 
						day_checker = Integer.parseInt(args[1]); } 
					catch(Exception e) {
							e.printStackTrace();
					}
				  }else{
					try { 
						    Player buddy = player.getServer().getPlayer(args[0]); 
							Name2 = buddy.getName().toLowerCase();
					}
					catch(Exception e) { 
								player.sendMessage("Syntax error: /birthdayp <player> <month> <day>");
					}
					try { 
							month_checker = Integer.parseInt(args[1]); } 
					catch(Exception e) { 
							month_checker = 0;
					}
					try { 
							day_checker = Integer.parseInt(args[2]); } 
					catch(Exception e) {
							day_checker = 0;
					}  
				  }
					if (day_checker == 0 && month_checker == 0 ){
					}else{
						if (Days_Of_Months[month_checker-1] >= day_checker){
							if (month_checker > 12) {
								player.sendMessage(ChatColor.RED + "They are only 12 months!!");
								return true;
							}
						   if(Date == 0){
							player.sendMessage(ChatColor.RED + "Set '" + Name2 + "'s birthday to the " + day_checker + "/" + month_checker + "/" + year);
						   }else{
							player.sendMessage(ChatColor.RED + "Set '" + Name2 + "'s birthday to the " + month_checker + "/" + day_checker + "/" + year);   
						   }
							cal.set(year, month_checker, day_checker);
							this.BDUsers.put(Name2, cal);
							return true;
						}else{ player.sendMessage(ChatColor.RED + "That month does not have that many days!");
						return true;
						}
					}
				}
			}else{
		    player.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return true;
            }
		}else if (cmd.equalsIgnoreCase("bd")) {
			if(!player.hasPermission("birthday.check")){
				 player.sendMessage(ChatColor.RED + "You do not have permission to use this!");
				 return true;
			}
				if (args.length < 1){
					player.sendMessage(ChatColor.RED + "/bd <player>");
					return true;
				}else{
					String Name_3 = args[0].toLowerCase();
					if (BDUsers.containsKey(Name_3)) {
						int day_3 = BDUsers.get(Name_3).get(Calendar.DATE);
						int month_3 = BDUsers.get(Name_3).get(Calendar.MONTH);
						int year_3 = BDUsers.get(Name_3).get(Calendar.YEAR);
					   if(Date == 0){
				        player.sendMessage(ChatColor.YELLOW + "" + Name_3 + "'s birthday is on: " + day_3 + "/" + month_3 + "/" + year_3);
					   }else{
						player.sendMessage(ChatColor.YELLOW + "" + Name_3 + "'s birthday is on: " + month_3 + "/" + day_3 + "/" + year_3);
					   }
				        return true;
					}else{
						player.sendMessage(ChatColor.YELLOW + "" + Name_3 + " did not set his birthday, use /birthday to set it");
						return true;
					}
				}
		     }
		return false;
	}
}


