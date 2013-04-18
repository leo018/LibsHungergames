package me.libraryaddict.Hungergames.Kits;

import java.util.HashMap;

import me.libraryaddict.Hungergames.Hungergames;
import me.libraryaddict.Hungergames.Managers.KitManager;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class Thor implements Listener {
    HashMap<String, Long> lastThored = new HashMap<String, Long>();

    private KitManager kits = HungergamesApi.getKitManager();
    private Hungergames hg = HungergamesApi.getHungergames();

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player p = event.getPlayer();
            if (kits.hasAbility(p, "Thor") && event.getItem() != null && event.getItem().getType() == Material.WOOD_AXE) {
                if (!lastThored.containsKey(p.getName()) || lastThored.get(p.getName()) < System.currentTimeMillis()) {
                    lastThored.put(p.getName(), System.currentTimeMillis() + 5000);
                    if (event.getClickedBlock().getType() != Material.BEDROCK)
                        event.getClickedBlock().setType(Material.NETHERRACK);
                    event.getClickedBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
                    LightningStrike strike = p.getWorld().strikeLightning(
                            p.getWorld().getHighestBlockAt(event.getClickedBlock().getLocation()).getLocation().clone()
                                    .add(0, 1, 0));
                    strike.setMetadata("DontHurt", new FixedMetadataValue(hg, p.getName()));
                } else
                    p.sendMessage(ChatColor.RED + "You may not do that at this time");
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof LightningStrike && event.getEntity() instanceof Player
                && event.getDamager().hasMetadata("DontHurt")
                && ((Player) event.getEntity()).getName().equals(event.getDamager().getMetadata("DontHurt").get(0).value()))
            event.setCancelled(true);
    }
}
