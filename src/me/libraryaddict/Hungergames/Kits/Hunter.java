package me.libraryaddict.Hungergames.Kits;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import me.libraryaddict.Hungergames.Managers.KitManager;
import me.libraryaddict.Hungergames.Types.HungergamesApi;

public class Hunter implements Listener {

    private KitManager kits = HungergamesApi.getKitManager();

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if ((event.getEntity() instanceof Chicken || event.getEntity() instanceof Cow || event.getEntity() instanceof Pig)
                && event.getEntity().getKiller() != null && kits.hasAbility(event.getEntity().getKiller(), "Hunter")) {
            Iterator<ItemStack> itel = event.getDrops().iterator();
            List<ItemStack> toAdd = new ArrayList<ItemStack>();
            while (itel.hasNext()) {
                ItemStack item = itel.next();
                if (item == null
                        || (item.getType() != Material.RAW_BEEF && item.getType() != Material.RAW_CHICKEN && item.getType() != Material.PORK))
                    continue;
                if (item.getType() == Material.RAW_CHICKEN)
                    toAdd.add(new ItemStack(Material.COOKED_CHICKEN, item.getAmount()));
                else if (item.getType() == Material.RAW_BEEF)
                    toAdd.add(new ItemStack(Material.COOKED_BEEF, item.getAmount()));
                else if (item.getType() == Material.PORK)
                    toAdd.add(new ItemStack(Material.GRILLED_PORK, item.getAmount()));
                itel.remove();
            }
            for (ItemStack item : toAdd)
                event.getDrops().add(item);
        }
    }
}
