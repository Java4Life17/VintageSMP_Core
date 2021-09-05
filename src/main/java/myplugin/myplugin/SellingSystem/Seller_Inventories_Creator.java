package myplugin.myplugin.SellingSystem;

import myplugin.myplugin.MyPlugin;
import myplugin.myplugin.Tools;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Seller_Inventories_Creator {

    public static void createPages() throws IOException {
        //29
        int pages_Needed = 0;
        int selling_Items_Size = MyPlugin.selling_system.selling_Items.size();

        while (selling_Items_Size != 0) {
            pages_Needed++;
            selling_Items_Size = Math.max(selling_Items_Size - 29, 0);
        }

        if (pages_Needed == 0) {
            return;
        }

        fill_In_Pages(pages_Needed);


        MyPlugin.selling_system.pages.get(1).setItem(45, getDisplay_Pane());

        int size = 0;
        while (MyPlugin.selling_system.pages.get(size + 1) != null){
            size++;
        }

        MyPlugin.selling_system.pages.get(size).setItem(53, getDisplay_Pane());




        /*
       100 Items

       if 100 items - 29 > 0 | YES Output = 71;
       if 71 items - 29 > 0 | YES Output = 42;
       if 42 items - 29 > 0 | YES Output = 13;

       By this point, we already 3 pages.

       if 13 items - 29 > 0 | No Output = -16;

       Then we create another alg that makes a new inventory where only those 13 are going to fit, not more pages after that.

       13 - 13 = 0

         */
    }

    private static void fill_In_Pages(int pages_Needed) throws IOException {
        int pages_Filled = 1;

        // 21 pages needed
        // filled 20 pages.
        while (!(pages_Filled > pages_Needed)) {
            page_Filler(pages_Filled);
            pages_Filled++;
        }


    }

    private static void page_Filler(int pageNumber) throws IOException {
        Inventory inventory = Bukkit.createInventory(null, 54, Tools.colorMSG("&e&lSUBASTAS &7&oPagina " + pageNumber));

        List<Integer> display_Pane_Slots = Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 36, 37, 39, 40, 41, 43, 44, 46, 47, 48, 50, 51, 52);
        for (int slot : display_Pane_Slots) {
            inventory.setItem(slot, getDisplay_Pane());
        }

        inventory.setItem(45, getPreviousPage_Item());
        inventory.setItem(53, getNextPage_Item());
        inventory.setItem(49, getSellerSettings_Item());

        for (int i = 0; i < 54; i++) {
            if (inventory.getItem(i) == null || Objects.equals(Objects.requireNonNull(inventory.getItem(i)).getType(), Material.AIR) ||
                    Objects.equals(Objects.requireNonNull(inventory.getItem(i)).getType(), Material.VOID_AIR) ||
                    Objects.equals(Objects.requireNonNull(inventory.getItem(i)).getType(), Material.CAVE_AIR)) {

                if (!MyPlugin.selling_system.selling_Items.isEmpty()) {
                    inventory.setItem(i, MyPlugin.selling_system.selling_Items.get(0).getAsDisplay());
                    MyPlugin.selling_system.selling_Items.remove(MyPlugin.selling_system.selling_Items.get(0));
                }
            }
        }

        MyPlugin.selling_system.pages.put(pageNumber, inventory);
    }

    private static ItemStack getSellerSettings_Item() {
        ItemStack stack = new ItemStack(Material.SLIME_BALL);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG("&a&oConfiguración de Vendedor"));
        stack.setItemMeta(meta);
        return stack;
    }

    private static ItemStack getNextPage_Item() {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG("&6&lPAGINA SIGUIENTE &5->"));
        stack.setItemMeta(meta);
        return stack;
    }

    private static ItemStack getPreviousPage_Item() {
        ItemStack stack = new ItemStack(Material.ARROW);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(Tools.colorMSG("&5<- &6&lPAGINA ANTERIOR"));
        stack.setItemMeta(meta);
        return stack;
    }

    private static ItemStack getDisplay_Pane() {
        ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = stack.getItemMeta();
        assert meta != null;
        meta.setDisplayName(" ");
        stack.setItemMeta(meta);
        return stack;
    }

}
