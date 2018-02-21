/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zjyl1994.moreloots;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.entity.spawn.EntitySpawnCause;
import org.spongepowered.api.event.cause.entity.spawn.SpawnTypes;
import org.spongepowered.api.event.entity.DestructEntityEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;
import java.nio.file.Path;
import com.google.inject.Inject;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.logging.Level;
import org.spongepowered.api.config.DefaultConfig;
import org.slf4j.Logger;


/**
 *
 * @author zjyl1
 */
@Plugin(id = "moreloots", name = "MoreLoots", version = "1.0")
public class MoreLoots {
    HashMap<String,HashMap<String,Integer>> LootList;
    
    @Inject
    @DefaultConfig(sharedRoot = true)
    private Path defaultConfig;

    
    @Inject
    private Logger logger;
    
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        LootList = new HashMap<>();
        Type type = new TypeToken<HashMap<String,HashMap<String,Integer>>>(){}.getType();
//        LootList.add(new LootItem("mw:copperingot",16));
//        LootList.add(new LootItem("minecraft:gunpowder",16));
        Gson gson = new Gson();
        JsonReader reader;
        //logger.debug("Configure:" + defaultConfig.toString());
        try {
            reader = new JsonReader(new FileReader(defaultConfig.toFile()));
            LootList = gson.fromJson(reader, type);
            //logger.debug("LootList", LootList);
        } catch (FileNotFoundException ex) {
            logger.error(ex.getMessage());
        }
    }
    
    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        Living targetEntity = event.getTargetEntity();
        if (!LootList.isEmpty()){
            String EntityName = targetEntity.getType().getName().toLowerCase();
            if(LootList.containsKey(EntityName)){
                HashMap<String, Integer> SpecificLootList = LootList.get(EntityName);
                Random rand = new Random();
                if(rand.nextBoolean()){
                    List<String> SLLKeys = new ArrayList<>(SpecificLootList.keySet());
                    String selectedLootID = SLLKeys.get(rand.nextInt(SLLKeys.size()));
                    Location<World> location = targetEntity.getLocation();
                    spawnItem(selectedLootID,rand.nextInt(SpecificLootList.get(selectedLootID))+1 ,location);
                }
            }
        }
    }
    
    private void spawnItem(String itemID,Integer itemNum, Location<World> spawnLocation) {
        if (Sponge.getRegistry().getType(ItemType.class, itemID).isPresent()) {
	    ItemStack spawnItems = ItemStack.builder().itemType(Sponge.getRegistry().getType(ItemType.class, itemID).get()).quantity(itemNum).build();
            Extent extent = spawnLocation.getExtent();
            Entity item = extent.createEntity(EntityTypes.ITEM, spawnLocation.getPosition());
            item.offer(Keys.REPRESENTED_ITEM, spawnItems.createSnapshot());
            extent.spawnEntity(item, Cause.source(EntitySpawnCause.builder().entity(item).type(SpawnTypes.PLUGIN).build()).build());
	}
    }
}
