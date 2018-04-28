package com.torciv.customfill.comandos;

import com.torciv.customfill.CustomFill;
import com.torciv.customfill.utils.Configuration;
import com.torciv.customfill.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CmdFill implements CommandExecutor {

    private CustomFill instance;
    private Utils utils;
    private Configuration conf;

    public CmdFill(CustomFill pl, Utils u, Configuration cnf) {
        this.instance = pl;
        this.utils = u;
        this.conf = cnf;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (cmd.getName().equalsIgnoreCase("tntfill")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(utils.getPrefix() + utils.color(" &cError! This command can only be executed by Players!"));
                return true;
            } else {
                Player p = (Player) sender;
                if (!p.hasPermission("custom.tntfill")) {
                    utils.message(p, conf.getNoperms());
                    return true;
                }

                if (args.length == 0) {
                    utils.messageFormat2(p, " &cUse: &6/tntfill &e<radius> <amount>");
                    utils.messageFormat2(p, " &cUse: &6/tntfill &eclear");                    
                    return true;
                }
                if (args.length > 2) {
                    utils.message(p, " &cToo many arguments!");
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    if (args.length == 1) {
                        if (p.hasPermission("custom.tntfill.reload")) {
                            instance.reloadConfig();
                            instance.saveConfig();
                            conf = new Configuration(instance);
                            utils.message(p, " &aConfiguration Reloaded!");
                            return true;
                        } else {
                            utils.message(p, conf.getNReload());
                            return true;
                        }
                    }
                }

                PlayerInventory pinv = p.getInventory();

                if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("clear")) {

                        if (!p.hasPermission("custom.tntfill.clear")) {
                            utils.message(p, conf.getNclear());
                            return true;
                        }

                        if (pinv.firstEmpty() == -1) {
                            utils.message(p, conf.getInvfull());
                            return true;
                        }
                        List<Dispenser> disp = isDispenserNearby(p.getLocation().getBlock(), conf.getClearRad());
                        
                        int numDisp = disp.size();
                        int cleared = 0;
                        
                        if (!disp.isEmpty()) {
                            int toclear = amountTntDispensers(disp);
                            if(toclear == 0){
                                utils.messageFormat2(p, conf.getAcleared());
                                return true;
                            }
                            for (final Dispenser d : disp) {
                                int dispAmount = dispenserTntAmount(d);
                                if(dispAmount > 0){
                                    addClearedTNT(pinv, Material.TNT, dispAmount, p, d);
                                    
                                    int am = dispenserTntAmount(d);
                                    if(am == 0){
                                        numDisp--;
                                        cleared++;
                                    }
                                }

                            }
                            int lefttoclear = amountTntDispensers(disp);
                            utils.messageFormat2(p, conf.getDispcleared().replace("%TNT_CLEARED%", String.valueOf(toclear-lefttoclear)).replace("%DISP_CLEARED%", String.valueOf(cleared)));
                            if (numDisp != 0){
                                utils.messageFormat2(p, conf.getNocleardisp().replace("%NO_DISP_CLEARED%", String.valueOf(numDisp)).replace("%TNT_TO_CLEAR%", String.valueOf(lefttoclear)));    
                            }

                        } else {
                            utils.message(p, conf.getNoDispAround().replace("%RADIUS%", String.valueOf(conf.getClearRad())));
                            return true;

                        }
                    }
                }

                if (args.length == 2) {
                    if ((utils.isInt(args[0])) && (utils.isInt(args[1]))) {

                        if (!p.hasPermission("custom.tntfill.fill")) {
                            utils.message(p, conf.getNfill());
                            return true;
                        }

                        if (!pinv.contains(Material.TNT)) {
                            utils.message(p, conf.getNoTnt());
                            return true;
                        }

                        int rad = Integer.parseInt(args[0]);
                        int amount = Integer.parseInt(args[1]);

                        if (rad <= conf.getMaxRadius()) {
                            rad = Math.abs(rad);
                            if (amount <= conf.getMaxTnt()) {
                                amount = Math.abs(amount);

                                List<Dispenser> disp = isDispenserNearby(p.getLocation().getBlock(), rad);
                                if (!disp.isEmpty()) {
                                    
                                    int usedtnt = 0;
                                    int pAmount = playerTntAmount(p);
                                    int alreadyFilled = 0;
                                    int filled = 0;
                                    int nDispensers = disp.size();

                                    for (final Dispenser d : disp) {

                                        int dispAmount = dispenserTntAmount(d);

                                        if (dispAmount < amount) {
                                            int left = amount - dispAmount;

                                            if (nDispensers != 0) {
                                                if (pAmount >= left) {
                                                    d.getInventory().addItem(new ItemStack[]{new ItemStack(Material.TNT, left)});
                                                    removeItem(pinv, Material.TNT, left);
                                                    pAmount = playerTntAmount(p);
                                                    filled++;
                                                    usedtnt += left;
                                                    nDispensers--;
                                                } else {
                                                    if (pAmount != 0) {
                                                        d.getInventory().addItem(new ItemStack[]{new ItemStack(Material.TNT, pAmount)});
                                                        removeItem(pinv, Material.TNT, pAmount);
                                                        usedtnt += pAmount;
                                                        pAmount = playerTntAmount(p);
                                                    }
                                                }
                                            }
                                        } else {
                                            alreadyFilled++;
                                            nDispensers--;
                                        }
                                    }
                                    
                                    utils.messageFormat2(p, conf.getFilledDisp().replace("%FILLED%", String.valueOf(filled)).replace("%TNT%", String.valueOf(usedtnt)).replace("%TNT_X_DISP%", String.valueOf(amount)));
                                    if (alreadyFilled != 0) {
                                        utils.messageFormat2(p, conf.getAfilled().replace("%A_FILLED%", String.valueOf(alreadyFilled)).replace("%TNT_X_DISP%", String.valueOf(amount)));
                                    }
                                    if (nDispensers != 0) {
                                        utils.messageFormat2(p, conf.getNoFilledDisp().replace("%NO_FILLED%", String.valueOf(nDispensers)));
                                    }
                                } else {
                                    utils.message(p, conf.getNoDispAround().replace("%RADIUS%", String.valueOf(rad)));
                                    return true;
                                }

                            } else {
                                utils.message(p, conf.getLowAmount().replace("%TNT_MAX%", String.valueOf(conf.getMaxTnt())));
                                return true;
                            }
                        } else {
                            utils.message(p, conf.getLowRad().replace("%MAX_RADIUS%", String.valueOf(conf.getMaxRadius())));
                            return true;
                        }

                    }

                }
            }
        }
        return true;
    }

    private List<Dispenser> isDispenserNearby(Block bl, int rad) {
        List<Dispenser> dispensers = new ArrayList<Dispenser>();
        World world = bl.getWorld();

        int blockXx = bl.getX();
        int blockYy = bl.getY();
        int blockZz = bl.getZ();

        for (int fromX = -rad; fromX <= rad; fromX++) {
            for (int fromY = -rad; fromY <= rad; fromY++) {
                for (int fromZ = -rad; fromZ <= rad; fromZ++) {
                    Block b = world.getBlockAt(blockXx + fromX, blockYy + fromY, blockZz + fromZ);
                    if (b.getType().equals(Material.DISPENSER)) {
                        dispensers.add((Dispenser) b.getState());
                    }
                }
            }
        }
        return dispensers;
    }

    private int dispenserTntAmount(Dispenser d) {
        int tnt = 0;
        ItemStack[] cont;
        for (int length = (cont = d.getInventory().getContents()).length, i = 0; i < length; i++) {
            ItemStack it = cont[i];
            if (it != null) {
                if (it.getType().equals((Object) Material.TNT)) {
                    if (!it.hasItemMeta()) {
                        tnt += it.getAmount();
                    } else {
                        int am = it.getAmount();
                        removeItem(d.getInventory(), Material.TNT, am);
                        d.getInventory().addItem(new ItemStack[]{new ItemStack(Material.TNT, am)});
                        tnt += it.getAmount();
                    }
                }
            }
        }
        return tnt;
    }
    
    private int amountTntDispensers(List<Dispenser> dlist){
        int tntCount = 0;
        for(Dispenser d : dlist){
        ItemStack[] contents;
        for (int length = (contents = d.getInventory().getContents()).length, k = 0; k < length; ++k) {
            final ItemStack i = contents[k];
            if (i != null) {
                if (i.getType().equals((Object) Material.TNT)) {
                        tntCount += i.getAmount();
                    }
                }
            }
        }
        return tntCount;        
    }

    private int playerTntAmount(Player p) {
        int tntCount = 0;
        ItemStack[] contents;
        for (int length = (contents = p.getInventory().getContents()).length, k = 0; k < length; ++k) {
            final ItemStack i = contents[k];
            if (i != null) {
                if (i.getType().equals((Object) Material.TNT)) {
                    if (!i.hasItemMeta()) {
                        tntCount += i.getAmount();
                    } else {
                        int am = i.getAmount();
                        i.setAmount(0);
                        i.setType(Material.TNT);
                        i.setAmount(am);
                        tntCount += i.getAmount();
                    }
                }
            }
        }
        return tntCount;
    }

    private void removeItem(Inventory inventory, Material m, int quantity) {
        int rest = quantity;
        for (int i = 0; i < inventory.getSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack == null || stack.getType() != m) {
                continue;
            }
            if (rest >= stack.getAmount()) {
                rest -= stack.getAmount();
                inventory.clear(i);
            } else if (rest > 0) {
                stack.setAmount(stack.getAmount() - rest);
                rest = 0;
            } else {
                break;
            }
        }
    }

    private void addClearedTNT(Inventory inventory, Material m, int quantity, Player p, Dispenser d) {
        int toadd = quantity;
        ItemStack[] contents;
        for (int length = (contents = p.getInventory().getContents()).length, k = 0; k < length; ++k) {
            if (toadd != 0){
                final ItemStack i = contents[k];
                if (i == null) {
                    if (toadd >= 64) {
                        inventory.addItem(new ItemStack[]{new ItemStack(Material.TNT, 64)});
                        toadd -= 64;
                        removeItem(d.getInventory(), m, 64);

                    } else {
                        inventory.addItem(new ItemStack[]{new ItemStack(Material.TNT, toadd)});
                        removeItem(d.getInventory(), m, toadd);
                        toadd = 0;
                    }
                } else {
                    if (i.getType().equals((Object) Material.TNT)) {
                        if (!i.hasItemMeta()) {
                            int count = i.getAmount();
                            if(count != 64){
                                int sum = toadd + count;
                                if (sum >= 64) {
                                    i.setAmount(64);
                                    int toremove = 64 - count;
                                    toadd = sum - 64;
                                    removeItem(d.getInventory(), m, toremove);
                                } else {
                                    i.setAmount(sum);
                                    removeItem(d.getInventory(), m, toadd);
                                    toadd = 0;
                                }
                            }
                        }
                    }
                }
            } 
        }
    }

}
