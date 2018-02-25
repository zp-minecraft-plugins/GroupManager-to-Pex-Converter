package me.zackpollard.pro;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author zack
 * @Date 06/02/15.
 */
public class Converter {

    public static void main(String[] args) {

        File file = new File("./groups.yml");
        YamlConfiguration yc = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection groups = yc.getConfigurationSection("groups"), group, info;
        for(String groupName : groups.getKeys(false))
        {
            group = groups.getConfigurationSection(groupName);
            if(group == null)
            {
                continue;
            }

            info = group.getConfigurationSection("info");
            if(info != null) {

                Map<String, Object> data = info.getValues(true);
                info = group.createSection("options", data);
                if(group.isSet("default"))
                {
                    info.set("default", group.getBoolean("default"));
                }
                group.set("info", null);
            }
        }

        try {
            yc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /**
         * User.yml section
         */

        file = new File("./users.yml");
        yc = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection users = yc.getConfigurationSection("users");
        for(String userID : users.getKeys(false))
        {
            ConfigurationSection user = users.getConfigurationSection(userID);
            if(user == null)
            {
                continue;
            }

            info = user.getConfigurationSection("info");
            if(info != null) {

                Map<String, Object> data = info.getValues(true);
                user.createSection("options", data);
                user.set("info", null);
            }



            List<String> userGroups = new ArrayList<>();
            userGroups.add(user.getString("group"));
            userGroups.addAll(user.getStringList("subgroups"));
            Object[] userGroupsArray = userGroups.toArray();

            user.set("group", null);
            user.set("subgroups", null);

            user.set("group", userGroupsArray);

            user.set("options.name", user.getString("lastname"));
            user.set("lastname", null);
        }

        try {
            yc.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}