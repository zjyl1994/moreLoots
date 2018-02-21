/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zjyl1994.moreloots;

import java.nio.file.Path;
import com.google.inject.Inject;
import java.io.IOException;
import org.spongepowered.api.config.DefaultConfig;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Hostile;
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

/**
 *
 * @author zjyl1
 */
@Plugin(id = "moreloots", name = "MoreLoots", version = "1.0")
public class MoreLoots {
    List<LootItem> LootList;
    
    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        LootList = new ArrayList();
        LootList.add(new LootItem("mw:copperingot",16));
        LootList.add(new LootItem("minecraft:gunpowder",16));
    }
    @Listener
    public void onEntityDeath(DestructEntityEvent.Death event) {
        Living targetEntity = event.getTargetEntity();
        if (targetEntity instanceof Hostile) {
            if(!LootList.isEmpty()){
                Random rand = new Random();
                if(rand.nextBoolean()){
                    LootItem li = LootList.get(rand.nextInt(LootList.size()));
                    Location<World> location = targetEntity.getLocation();
                    spawnItem(li.getItemID(),rand.nextInt(li.getItemNum())+1 ,location);
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
