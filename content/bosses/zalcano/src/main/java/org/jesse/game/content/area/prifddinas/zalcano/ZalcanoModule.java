package org.jesse.game.content.area.prifddinas.zalcano;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.content.commands.DeveloperCommands;
import org.jesse.game.content.area.prifddinas.zalcano.combat.DownedCombatStrategy;
import org.jesse.game.content.area.prifddinas.zalcano.combat.TephraCombatStrategy;
import org.jesse.game.content.area.prifddinas.zalcano.combat.actions.DroppingBouldersAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.GameCommands.Command;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat.IPlayerCombatAttackHook;
import org.jesse.game.world.entity.player.privilege.PlayerPrivilege;
import org.jesse.plugins.events.ServerLaunchEvent;

import java.util.Set;

public class ZalcanoModule {

  @Subscribe
  public static void boot(ServerLaunchEvent event) {
    WorldTasksManager.scheduleCreation(() -> {
      if (DeveloperCommands.INSTANCE.getEnabledZalcano()) {
        registerPlayerAttackHook();
        registerCommands();
        ZalcanoInstance.INSTANCE.spawnZalcano();
      }
    });
  }

  private static void registerPlayerAttackHook() {
    PlayerCombat.registerAttackHook((player, entity, spell) -> {
      if (player.inArea(ZalcanoLair.class)) {
        player.getActionManager().setAction(new TephraCombatStrategy(entity));
        if (entity instanceof ZalcanoBoss) {
          if (((ZalcanoBoss) entity).getPhase() == ZalcanoPhase.DOWNED) {
            player.getActionManager().setAction(new DownedCombatStrategy(entity));
          }
        }
        return IPlayerCombatAttackHook.Result.Return;
      }

      return IPlayerCombatAttackHook.Result.Pass;
    });
  }

  private static void registerCommands() {
    new Command(PlayerPrivilege.ADMINISTRATOR, "z-reg", "Deactivate all Zalcano rock formations.", (p, args) -> {
      ZalcanoInstance.INSTANCE.getRockFormationHandler().deactivateAllFormations();
    });

    new Command(PlayerPrivilege.ADMINISTRATOR, "z-f", "Check floor object at your tile.", (p, args) -> {
      p.sendMessage("is floo free " + World.getObjectWithType(p.getPosition(), 10));
    });

    new Command(PlayerPrivilege.ADMINISTRATOR, "z-dep", "Deplete all Zalcano rock formations.", (p, args) -> {
      ZalcanoInstance.INSTANCE.getRockFormationHandler().depleteAllFormations();
    });

    new Command(PlayerPrivilege.ADMINISTRATOR, "z-n", "Switch active Zalcano rock formation.", (p, args) -> {
      ZalcanoInstance.INSTANCE.getRockFormationHandler().switchActivateFormation();
    });

    new Command(PlayerPrivilege.ADMINISTRATOR, "z-b", "Drop boulders at your tile.", (p, args) -> {
      WorldTasksManager
          .schedule(new DroppingBouldersAction(Set.of(p.getLocation().copy()), ZalcanoInstance.INSTANCE), 0, 0);
    });

    new Command(PlayerPrivilege.ADMINISTRATOR, "zalcano", "Teleport to the Zalcano arena.", (player, strings) -> {
      player.teleport(ZalcanoConstants.ZALCANO_LAYER_LOCATION);
    });
  }
}
