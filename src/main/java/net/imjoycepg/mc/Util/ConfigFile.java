package net.imjoycepg.mc.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ConfigFile {
    public File config;
    public FileConfiguration c;

    public ConfigFile(JavaPlugin plugin, String fileName) {
        this.config = new File(plugin.getDataFolder(), fileName);
        if (!this.config.exists()) {
            this.config.getParentFile().mkdirs();
            if (plugin.getResource(fileName) == null) {
                try {
                    this.config.createNewFile();
                } catch (IOException var4) {
                    plugin.getLogger().severe("Failed to create new file " + fileName);
                }
            } else {
                plugin.saveResource(fileName, false);
            }
        }

        this.c = YamlConfiguration.loadConfiguration(this.config);
    }

    public double getDouble(String path) {
        return this.c.contains(path) ? this.c.getDouble(path) : 0.0D;
    }

    public int getInt(String path) {
        return this.c.contains(path) ? this.c.getInt(path) : 0;
    }

    public boolean getBoolean(String path) {
        return this.c.contains(path) ? this.c.getBoolean(path) : false;
    }

    public long getLong(String path) {
        return this.c.contains(path) ? this.c.getLong(path) : 0L;
    }

    public String getString(String path) {
        return this.c.contains(path) ? ChatColor.translateAlternateColorCodes('&', this.c.getString(path)) : null;
    }

    public String getString(String path, String callback, boolean colorize) {
        if (this.c.contains(path)) {
            return colorize ? ChatColor.translateAlternateColorCodes('&', this.c.getString(path)) : this.c.getString(path);
        } else {
            return callback;
        }
    }

    public List<String> getReversedStringList(String path) {
        List<String> list = this.getStringList(path);
        if (list == null) {
            return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
        } else {
            int size = list.size();
            List<String> toReturn = new ArrayList();

            for(int i = size - 1; i >= 0; --i) {
                toReturn.add(list.get(i));
            }

            return toReturn;
        }
    }

    public List<String> getStringList(String path) {
        if (!this.c.contains(path)) {
            return Arrays.asList("ERROR: STRING LIST NOT FOUND!");
        } else {
            ArrayList<String> strings = new ArrayList();
            Iterator var3 = this.c.getStringList(path).iterator();

            while(var3.hasNext()) {
                String string = (String)var3.next();
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }

            return strings;
        }
    }

    public List<String> getStringListOrDefault(String path, List<String> toReturn) {
        if (!this.c.contains(path)) {
            return toReturn;
        } else {
            ArrayList<String> strings = new ArrayList();
            Iterator var4 = this.c.getStringList(path).iterator();

            while(var4.hasNext()) {
                String string = (String)var4.next();
                strings.add(ChatColor.translateAlternateColorCodes('&', string));
            }

            return strings;
        }
    }

    public void save() {
        try {
            this.c.save(this.config);
        } catch (IOException var2) {
            Bukkit.getLogger().severe("Could not save config file " + this.config.toString());
            var2.printStackTrace();
        }

    }

    public void reloadYMLS() {
        try {
            this.c.load(this.config);
        } catch (IOException | InvalidConfigurationException var2) {
            var2.printStackTrace();
        }

    }

    public File getConfig() {
        return this.config;
    }

    public FileConfiguration getC() {
        return this.c;
    }
}

