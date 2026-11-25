package dev.khoa.plugin.cookieportal.portal;

import dev.khoa.plugin.cookieportal.CookiePortalPlugin;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.GameRule;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

public final class InstantPortalTravelService implements Listener {
   private static final GameRule<Integer> PORTAL_DELAY_RULE = resolvePortalDelayRule();
   private final CookiePortalPlugin plugin;

   public InstantPortalTravelService(CookiePortalPlugin plugin) {
      this.plugin = plugin;
   }

   public void start() {
      if (!this.plugin.settings().enabled()) {
         return;
      }
      this.plugin.scheduler().runGlobal(() -> {
         for(World world : this.plugin.getServer().getWorlds()) {
            this.enableInstantTravel(world);
         }

      });
   }

   @EventHandler
   public void onWorldLoad(WorldLoadEvent event) {
      if (!this.plugin.settings().enabled()) {
         return;
      }
      World world = event.getWorld();
      this.plugin.scheduler().runGlobal(() -> this.enableInstantTravel(world));
   }

   private void enableInstantTravel(World world) {
      if (PORTAL_DELAY_RULE == null) {
         return;
      }
      Integer current = world.getGameRuleValue(PORTAL_DELAY_RULE);
      if (current == null || current != 0) {
         world.setGameRule(PORTAL_DELAY_RULE, 0);
      }

   }

   @SuppressWarnings("unchecked")
   private static GameRule<Integer> resolvePortalDelayRule() {
      try {
         Field registryField = Registry.class.getField("GAME_RULE");
         Object registryObject = registryField.get(null);
         if (registryObject instanceof Registry<?> registry) {
            Object rule = registry.get(NamespacedKey.minecraft("players_nether_portal_default_delay"));
            if (rule instanceof GameRule<?> gameRule && gameRule.getType() == Integer.class) {
               return (GameRule<Integer>) gameRule;
            }
         }
      } catch (ReflectiveOperationException | LinkageError ignored) {
      }

      try {
         Class<?> gameRulesClass = Class.forName("org.bukkit.GameRules");
         Object rule = gameRulesClass.getField("PLAYERS_NETHER_PORTAL_DEFAULT_DELAY").get(null);
         if (rule instanceof GameRule<?> gameRule && gameRule.getType() == Integer.class) {
            return (GameRule<Integer>) gameRule;
         }
      } catch (ReflectiveOperationException | LinkageError ignored) {
      }

      try {
         Method getByName = GameRule.class.getMethod("getByName", String.class);
         for (String name : new String[]{"playersNetherPortalDefaultDelay", "players_nether_portal_default_delay"}) {
            Object rule = getByName.invoke(null, name);
            if (rule instanceof GameRule<?> gameRule && gameRule.getType() == Integer.class) {
               return (GameRule<Integer>) gameRule;
            }
         }
      } catch (ReflectiveOperationException | LinkageError ignored) {
      }

      try {
         Object rule = GameRule.class.getField("PLAYERS_NETHER_PORTAL_DEFAULT_DELAY").get(null);
         if (rule instanceof GameRule<?> gameRule && gameRule.getType() == Integer.class) {
            return (GameRule<Integer>) gameRule;
         }
      } catch (ReflectiveOperationException | LinkageError ignored) {
      }

      return null;
   }
}
