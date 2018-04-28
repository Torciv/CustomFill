
package com.torciv.customfill;

import com.torciv.customfill.comandos.CmdFill;
import com.torciv.customfill.utils.Configuration;
import com.torciv.customfill.utils.Utils;
import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CustomFill extends JavaPlugin{
    
    public static final String USER = "%%__USER__%%";
    
    private static CustomFill instance;
    
    private Utils utils;
    private Configuration conf;
    private static CmdFill fill;
    
    public FileConfiguration config;
    public File cfile;
    
    public CustomFill(){
        instance = this;
        utils = new Utils(instance);
        conf = new Configuration(instance);
    }
    
    @Override
    public void onEnable(){
        this.cfile = new File(getDataFolder(), "config.yml");
        if (!this.cfile.exists()) {
          getLogger().info("Config.yml not found, creating!");
          saveDefaultConfig();
        } else {
          getLogger().info("Config.yml found, loading!");
        }
        
        this.config = getConfig();
        this.config.options().copyDefaults(true);
        
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l=+=+=+=+=+=+=+= &aCustomFill &8&l=+=+=+=+=+=+=+="));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCustomFill has been &a&lENABLED"));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAuthor: &b&lTorciv"));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAll Rights reserved"));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l=+=+=+=+=+=+=+= &aCustomFill &8&l=+=+=+=+=+=+=+="));
        
        getCommand("tntfill").setExecutor(new CmdFill(this, utils, conf));
        
    }
    
    
    
    @Override
    public void onDisable(){
        saveConfig();
        
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l=+=+=+=+=+=+=+= &aCustomFill &8&l=+=+=+=+=+=+=+="));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eCustomFill has been &c&lDISABLED"));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAuthor: &b&lTorciv"));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eAll Rights reserved"));
        console.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8&l=+=+=+=+=+=+=+= &aCustomFill &8&l=+=+=+=+=+=+=+="));    
        
    }

    public static CustomFill getInstance() {
        return instance;
    }
}
