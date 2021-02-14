package net.hodyus.libsplugin.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;

import net.md_5.bungee.api.ChatColor;

public class ItemBuilder {

	private ItemStack is;

	public ItemBuilder(Material m) {
		this(m, 1, (short) 0);
	}

	public ItemBuilder(ItemStack is) {
		this.is = is.clone();
	}

	public ItemBuilder(Material m, int amount, short data) {
		is = new ItemStack(m, amount, data);
	}

	public ItemBuilder clone() {
		return new ItemBuilder(is);
	}

	public ItemBuilder durability(int dur) {
		is.setDurability((short) dur);
		return this;
	}

	public ItemBuilder name(String name) {
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder unsafeEnchantment(Enchantment ench, int level) {
		is.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemBuilder enchant(Enchantment ench, int level) {
		ItemMeta im = is.getItemMeta();
		im.addEnchant(ench, level, true);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment ench) {
		is.removeEnchantment(ench);
		return this;
	}

	public ItemBuilder owner(String owner) {
		try {
			SkullMeta im = (SkullMeta) is.getItemMeta();
			im.setOwner(owner);
			is.setItemMeta(im);
		} catch (ClassCastException expected) {
		}
		return this;
	}
	
	public ItemBuilder addFlag(ItemFlag flag) {
		try {
			ItemMeta im = is.getItemMeta();
			im.addItemFlags(flag);
			is.setItemMeta(im);
		}
		catch (Exception e) {
		}
		return this;
	}
	
	public ItemBuilder removeFlag(ItemFlag flag) {
		try {
			ItemMeta im = is.getItemMeta();
			im.removeItemFlags(flag);
			is.setItemMeta(im);
		} catch (Exception e) {
		}
		return this;
	}
	

	public ItemBuilder infinityDurabilty() {
		is.setDurability(Short.MAX_VALUE);
		return this;
	}

	public ItemBuilder lore(String... lore) {
		ItemMeta im = is.getItemMeta();
		List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
		for (String string : lore)
			out.add(ChatColor.translateAlternateColorCodes('&', string));
		im.setLore(out);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilder listLore(List<String> lore) {
		ItemMeta im = is.getItemMeta();
		List<String> out = im.getLore() == null ? new ArrayList<>() : im.getLore();
		for (String string : lore)
			out.add(ChatColor.translateAlternateColorCodes('&', string));
		im.setLore(out);
		is.setItemMeta(im);
		return this;
	}

	@SuppressWarnings("deprecation")
	public ItemBuilder woolColor(DyeColor color) {
		if (!is.getType().equals(Material.WOOL))
			return this;
		is.setDurability(color.getDyeData());
		return this;
	}

	public ItemBuilder amount(int amount) {
		if (amount > 64)
			amount = 64;
		is.setAmount(amount);
		return this;
	}

	public ItemBuilder removeAttributes() {
		ItemMeta meta = is.getItemMeta();
		meta.addItemFlags(ItemFlag.values());
		is.setItemMeta(meta);
		return this;
	}

	public ItemStack build() {
		return is;
	}

	public ItemBuilder color(Color color) {
		if (!is.getType().name().contains("LEATHER_"))
			return this;
		LeatherArmorMeta meta = (LeatherArmorMeta) is.getItemMeta();
		meta.setColor(color);
		is.setItemMeta(meta);
		return this;
	}

	public static ItemStack createHead(String displayName, List<String> lore, String texture) {
		GameProfile profile = createGameProfile(texture, UUID.randomUUID());
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		RefSet(headMetaClass, headMeta, "profile", profile);
		head.setItemMeta(headMeta);
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setDisplayName(displayName);
		skullMeta.setLore(lore);
		head.setItemMeta(skullMeta);
		return head;
	}

	public static ItemStack createHead(final String displayName, final String lore, final String texture) {
		final GameProfile profile = createGameProfile(texture, UUID.randomUUID());
		final ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		final ItemMeta headMeta = head.getItemMeta();
		final Class<?> headMetaClass = headMeta.getClass();
		RefSet(headMetaClass, headMeta, "profile", profile);
		head.setItemMeta(headMeta);
		final SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setDisplayName(displayName);
		final String[] lines = lore.split("\n");
		final ArrayList<String> Lore = new ArrayList<String>();
		for (int i = 0; i < lines.length; ++i) {
			Lore.add(lines[i]);
		}
		skullMeta.setLore(Lore);
		head.setItemMeta((ItemMeta) skullMeta);
		return head;
	}

	public static ItemStack createHead(String displayName, String texture) {
		GameProfile profile = createGameProfile(texture, UUID.randomUUID());
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		if (!RefSet(headMetaClass, headMeta, "profile", profile)) {
			return null;
		} else {
			head.setItemMeta(headMeta);
			SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
			skullMeta.setDisplayName(displayName);
			head.setItemMeta(skullMeta);
			return head;
		}
	}

	public static ItemStack createHead(String displayName, GameProfile profile) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		ItemMeta headMeta = head.getItemMeta();
		Class<?> headMetaClass = headMeta.getClass();
		if (!RefSet(headMetaClass, headMeta, "profile", profile)) {
			return null;
		} else {
			head.setItemMeta(headMeta);
			SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
			skullMeta.setDisplayName(displayName);
			head.setItemMeta(skullMeta);
			return head;
		}
	}

	public static GameProfile createGameProfile(String texture, UUID id) {
		GameProfile profile = new GameProfile(id, (String) null);
		PropertyMap propertyMap = profile.getProperties();
		propertyMap.put("textures", new Property("textures", texture));
		return profile;
	}

	public static ItemStack createPlayerHead(String displayname, String playername, String... lore) {
		ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
		SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
		skullMeta.setDisplayName(displayname);
		skullMeta.setOwner(playername);
		if (lore.length != 0) {
			String[] lines = lore[0].split("\n");
			ArrayList<String> Lore = new ArrayList<String>();

			for (int i = 0; i < lines.length; ++i) {
				Lore.add(lines[i]);
			}

			skullMeta.setLore(Lore);
		}

		head.setItemMeta(skullMeta);
		return head;
	}

	public static ItemStack createItem(Material material, int amount, String itemname, short... data) {
		ItemStack item = data.length == 0 ? new ItemStack(material, amount) : new ItemStack(material, amount, data[0]);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(itemname);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack createItem(Material material, int amount, String itemname, String lore, short... data) {
		ItemStack item = data.length == 0 ? new ItemStack(material, amount) : new ItemStack(material, amount, data[0]);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(itemname);
		if (lore.length() != 0) {
			String[] lines = lore.split("\n");
			ArrayList<String> Lore = new ArrayList<String>();

			for (int i = 0; i < lines.length; ++i) {
				Lore.add(lines[i]);
			}

			meta.setLore(Lore);
		}

		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack renameStack(ItemStack stack, int newamount, String newitename, boolean remlore) {
		ItemStack item = stack.clone();
		item.setAmount(newamount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(newitename);
		if (remlore && meta.hasLore()) {
			List<String> lore = null;
			meta.setLore((List<String>) lore);
		}

		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack renameStack(ItemStack stack, int newamount, String newitename, String newlore,
			short... newdata) {
		if (newdata.length != 0) {
			stack.setAmount(newamount);
		}

		stack.setAmount(newamount);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(newitename);
		if (newlore.length() != 0) {
			String[] lines = newlore.split("\n");
			ArrayList<String> Lore = new ArrayList<String>();

			for (int i = 0; i < lines.length; ++i) {
				Lore.add(lines[i]);
			}

			meta.setLore(Lore);
		}

		stack.setItemMeta(meta);
		return stack;
	}

	public static boolean RefSet(Class<?> sourceClass, Object instance, String fieldName, Object value) {
		try {
			Field field = sourceClass.getDeclaredField(fieldName);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			int modifiers = modifiersField.getModifiers();
			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			if ((modifiers & 16) == 16) {
				modifiersField.setAccessible(true);
				modifiersField.setInt(field, modifiers & -17);
			}

			try {
				field.set(instance, value);
			} finally {
				if ((modifiers & 16) == 16) {
					modifiersField.setInt(field, modifiers | 16);
				}

				if (!field.isAccessible()) {
					field.setAccessible(false);
				}

			}

			return true;
		} catch (Exception var11) {
			Bukkit.getLogger().log(Level.WARNING, "Unable to inject Gameprofile", var11);
			return false;
		}
	}
}