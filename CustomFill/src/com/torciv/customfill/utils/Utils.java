/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.torciv.customfill.utils;

import com.torciv.customfill.CustomFill;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 *
 * @author victor
 */
public class Utils {
    private CustomFill instance;
    
    public Utils(CustomFill pl) {
        this.instance = pl;
    }
   
    public String color (String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
    
    public String getPrefix() {
        String prefix = color(instance.getConfig().getString("Prefix"));
        return prefix;
    }
    
    public void message(Player p, String s){
        p.sendMessage("\n" + getPrefix() + color(s) + "\n" + " ");
    }
    
    public void messageFormat2(Player p, String s){
        p.sendMessage(getPrefix() + color(s));
    }
    
    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    
}
