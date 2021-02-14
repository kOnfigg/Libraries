package net.hodyus.libsplugin.text;

import net.hodyus.LibsPlugin;
import org.json.simple.*;
import java.util.regex.*;
import java.util.*;
import org.bukkit.inventory.*;
import org.bukkit.potion.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.meta.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.*;

public class UltimateFancy {
    private ChatColor lastColor;
    private JSONArray constructor;
    private HashMap<String, Boolean> lastformats;
    private List<JSONObject> workingGroup;
    private List<ExtraElement> pendentElements;

    public UltimateFancy() {
        this.lastColor = ChatColor.WHITE;
        this.constructor = new JSONArray();
        this.workingGroup = new ArrayList<JSONObject>();
        this.lastformats = new HashMap<String, Boolean>();
        this.pendentElements = new ArrayList<ExtraElement>();
    }

    public UltimateFancy(String text) {
        this.lastColor = ChatColor.WHITE;
        this.constructor = new JSONArray();
        this.workingGroup = new ArrayList<JSONObject>();
        this.lastformats = new HashMap<String, Boolean>();
        this.pendentElements = new ArrayList<ExtraElement>();
        this.text(text);
    }

    public UltimateFancy text(String text) {
        String[] split;
        for (int length = (split = text.split("(?=§)")).length, i = 0; i < length; ++i) {
            String part = split[i];
            JSONObject workingText = new JSONObject();
            this.filterColors(workingText);
            Matcher match = Pattern.compile("^§([0-9a-fk-or]).*$").matcher(part);
            if (match.find()) {
                this.lastColor = ChatColor.getByChar(match.group(1).charAt(0));
                this.filterColors(workingText);
                if (part.length() == 2) {
                    continue;
                }
            }
            if (!ChatColor.stripColor(part).isEmpty()) {
                workingText.put((Object)"text", (Object)ChatColor.stripColor(part));
                this.filterColors(workingText);
                if (!workingText.containsKey((Object)"color")) {
                    workingText.put((Object)"color", (Object)"white");
                }
                this.workingGroup.add(workingText);
            }
        }
        return this;
    }

    private JSONObject filterColors(JSONObject obj) {
        for (Map.Entry<String, Boolean> format : this.lastformats.entrySet()) {
            obj.put((Object)format.getKey(), (Object)format.getValue());
        }
        if (this.lastColor.isFormat()) {
            String formatStr = this.lastColor.name().toLowerCase();
            if (this.lastColor.equals((Object)ChatColor.MAGIC)) {
                formatStr = "obfuscated";
            }
            if (this.lastColor.equals((Object)ChatColor.UNDERLINE)) {
                formatStr = "underlined";
            }
            this.lastformats.put(formatStr, true);
            obj.put((Object)formatStr, (Object)true);
        }
        if (this.lastColor.isColor()) {
            obj.put((Object)"color", (Object)this.lastColor.name().toLowerCase());
        }
        if (this.lastColor.equals((Object)ChatColor.RESET)) {
            obj.put((Object)"color", (Object)"white");
            for (String format2 : this.lastformats.keySet()) {
                this.lastformats.put(format2, false);
                obj.put((Object)format2, (Object)false);
            }
        }
        return obj;
    }

    public UltimateFancy next() {
        if (this.workingGroup.size() > 0) {
            for (JSONObject obj : this.workingGroup) {
                if (obj.containsKey((Object)"text") && obj.get((Object)"text").toString().length() > 0) {
                    for (ExtraElement element : this.pendentElements) {
                        obj.put((Object)element.getAction(), (Object)element.getJson());
                    }
                    this.constructor.add((Object)obj);
                }
            }
        }
        this.workingGroup = new ArrayList<JSONObject>();
        this.pendentElements = new ArrayList<ExtraElement>();
        return this;
    }

    public UltimateFancy clickRunCmd(String cmd) {
        this.pendentElements.add(new ExtraElement("clickEvent", this.parseJson("run_command", cmd)));
        return this;
    }

    public UltimateFancy clickSuggestCmd(String cmd) {
        this.pendentElements.add(new ExtraElement("clickEvent", this.parseJson("suggest_command", cmd)));
        return this;
    }

    public UltimateFancy clickOpenURL(String url) {
        this.pendentElements.add(new ExtraElement("clickEvent", this.parseJson("open_url", url)));
        return this;
    }

    public UltimateFancy hoverShowText(String text) {
        this.pendentElements.add(new ExtraElement("hoverEvent", this.parseHoverText(text)));
        return this;
    }

    public UltimateFancy hoverShowItem(ItemStack item) {
        this.pendentElements.add(new ExtraElement("hoverEvent", this.parseHoverItem(item)));
        return this;
    }

    public String toOldFormat() {
        StringBuilder result = new StringBuilder();
        for (Object mjson : this.constructor) {
            JSONObject json = (JSONObject)mjson;
            if (!json.containsKey((Object)"text")) {
                continue;
            }
            String colorStr = json.get((Object)"color").toString();
            if (ChatColor.valueOf(colorStr.toUpperCase()) != null) {
                ChatColor color = ChatColor.valueOf(colorStr.toUpperCase());
                if (color.equals((Object)ChatColor.WHITE)) {
                    result.append(String.valueOf(ChatColor.RESET));
                }
                else {
                    result.append(String.valueOf(color));
                }
            }
            ChatColor[] values;
            for (int length = (values = ChatColor.values()).length, i = 0; i < length; ++i) {
                ChatColor frmt = values[i];
                if (!frmt.isColor()) {
                    String frmtStr = frmt.name().toLowerCase();
                    if (frmt.equals((Object)ChatColor.MAGIC)) {
                        frmtStr = "obfuscated";
                    }
                    if (frmt.equals((Object)ChatColor.UNDERLINE)) {
                        frmtStr = "underlined";
                    }
                    if (json.containsKey((Object)frmtStr)) {
                        result.append(String.valueOf(frmt));
                    }
                }
            }
            result.append(json.get((Object)"text").toString());
        }
        return result.toString();
    }

    private JSONObject parseHoverText(String text) {
        text = ChatColor.translateAlternateColorCodes('&', text);
        JSONArray extraArr = this.addColorToArray(text);
        JSONObject objExtra = new JSONObject();
        objExtra.put((Object)"text", (Object)"");
        objExtra.put((Object)"extra", (Object)extraArr);
        JSONObject obj = new JSONObject();
        obj.put((Object)"action", (Object)"show_text");
        obj.put((Object)"value", (Object)objExtra);
        return obj;
    }

    private JSONObject parseJson(String action, String value) {
        JSONObject obj = new JSONObject();
        obj.put((Object)"action", (Object)action);
        obj.put((Object)"value", (Object)value);
        return obj;
    }

    private JSONObject parseHoverItem(ItemStack item) {
        StringBuilder itemBuild = new StringBuilder();
        StringBuilder itemTag = new StringBuilder();
        String itemType = item.getType().toString().replace("_ITEM", "").replace("_SPADE", "_SHOVEL").replace("GOLD_", "GOLDEN_");
        itemBuild.append("id:" + itemType + ",Count:" + 1 + ",Damage:" + item.getDurability() + ",");
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                StringBuilder lore = new StringBuilder();
                for (String lorep : meta.getLore()) {
                    lore.append(String.valueOf(String.valueOf(lorep)) + ",");
                }
                itemTag.append("display:{Lore:[" + lore.toString().substring(0, lore.length() - 1) + "]},");
            }
            else if (meta.hasDisplayName()) {
                itemTag.append("display:{Name:" + meta.getDisplayName() + "},");
            }
            else if (meta.hasLore() && meta.hasDisplayName()) {
                StringBuilder lore = new StringBuilder();
                for (String lorep : meta.getLore()) {
                    lore.append(String.valueOf(String.valueOf(lorep)) + ",");
                }
                itemTag.append("display:{Name:" + meta.getDisplayName() + ",Lore:[" + lore.toString().substring(0, lore.length() - 1) + "]},");
            }
            if (meta instanceof PotionMeta) {
                StringBuilder itemEnch = new StringBuilder();
                itemEnch.append("CustomPotionEffects:[");
                Potion pot = Potion.fromItemStack(item);
                itemEnch.append("{Id:" + pot.getType().getEffectType().getId() + ",Duration:" + pot.getType().getEffectType().getDurationModifier() + ",Ambient:true,},");
                itemTag.append(String.valueOf(String.valueOf(itemEnch.toString().substring(0, itemEnch.length() - 1))) + "],");
            }
            else if (meta instanceof EnchantmentStorageMeta) {
                StringBuilder itemEnch = new StringBuilder();
                itemEnch.append("ench:[");
                for (Map.Entry<Enchantment, Integer> ench : ((EnchantmentStorageMeta)meta).getStoredEnchants().entrySet()) {
                    itemEnch.append("{id:" + ench.getKey().getId() + ",lvl:" + ench.getValue() + "},");
                }
                itemTag.append(String.valueOf(String.valueOf(itemEnch.toString().substring(0, itemEnch.length() - 1))) + "],");
            }
            else if (meta.hasEnchants()) {
                StringBuilder itemEnch = new StringBuilder();
                itemEnch.append("ench:[");
                for (Map.Entry<Enchantment, Integer> ench : meta.getEnchants().entrySet()) {
                    itemEnch.append("{id:" + ench.getKey().getId() + ",lvl:" + ench.getValue() + "},");
                }
                itemTag.append(String.valueOf(String.valueOf(itemEnch.toString().substring(0, itemEnch.length() - 1))) + "],");
            }
        }
        if (itemTag.length() > 0) {
            itemBuild.append("tag:{" + itemTag.toString().substring(0, itemTag.length() - 1) + "},");
        }
        JSONObject obj = new JSONObject();
        obj.put((Object)"action", (Object)"show_item");
        obj.put((Object)"value", (Object)ChatColor.stripColor("{" + itemBuild.toString().substring(0, itemBuild.length() - 1).replace(" ", "_") + "}"));
        return obj;
    }

    private JSONArray addColorToArray(String text) {
        JSONArray extraArr = new JSONArray();
        ChatColor color = ChatColor.WHITE;
        String[] split;
        for (int length = (split = text.split("(?=§[0-9a-fk-or])")).length, i = 0; i < length; ++i) {
            String part = split[i];
            JSONObject objExtraTxt = new JSONObject();
            Matcher match = Pattern.compile("^§([0-9a-fk-or]).*$").matcher(part);
            if (match.find()) {
                color = ChatColor.getByChar(match.group(1).charAt(0));
                if (part.length() == 2) {
                    continue;
                }
            }
            objExtraTxt.put((Object)"text", (Object)ChatColor.stripColor(part));
            if (color.isColor()) {
                objExtraTxt.put((Object)"color", (Object)color.name().toLowerCase());
            }
            if (color.equals((Object)ChatColor.RESET)) {
                objExtraTxt.put((Object)"color", (Object)"white");
            }
            if (color.isFormat()) {
                if (color.equals((Object)ChatColor.MAGIC)) {
                    objExtraTxt.put((Object)"obfuscated", (Object)true);
                }
                else {
                    objExtraTxt.put((Object)color.name().toLowerCase(), (Object)true);
                }
            }
            extraArr.add((Object)objExtraTxt);
        }
        return extraArr;
    }

    private String toJson() {
        return "[\"\"," + this.constructor.toJSONString().substring(1);
    }

    public void send(CommandSender to) {
        this.next();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + to.getName() + " " + toJson());
    }

    public void sendSync(Player player) {
        Bukkit.getScheduler().runTask(LibsPlugin.getInstance(), () -> {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + player.getName() + " " + toJson());
        });
    }

    private void performCommand(CommandSender sender, String command) {
        Bukkit.getServer().dispatchCommand(sender, command);
    }

    public class ExtraElement
    {
        private String action;
        private JSONObject json;

        public ExtraElement(String action, JSONObject json) {
            this.action = action;
            this.json = json;
        }

        public String getAction() {
            return this.action;
        }

        public JSONObject getJson() {
            return this.json;
        }
    }
}
