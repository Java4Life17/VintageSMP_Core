package myplugin.myplugin.Custom_Items;

import myplugin.myplugin.Tools;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Actual_Items {

    public static ItemStack getHeart(){
        ItemStack  stack = Tools.getHeadFromBase64("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjEyNjZiNzQ4MjQyMTE1YjMwMzcwOGQ1OWNlOWQ1NTIzYjdkNzljMTNmNmRiNGViYzkxZGQ0NzIwOWViNzU5YyJ9fX0=");

        ItemMeta meta = stack.getItemMeta();

        return stack;
    }

}
