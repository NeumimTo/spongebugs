package sponge.bugs;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.projectile.Projectile;
import org.spongepowered.api.event.CauseStackManager;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.entity.damage.DamageType;
import org.spongepowered.api.event.cause.entity.damage.source.IndirectEntityDamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.util.Tristate;

@Plugin(
        id = "bugs",
        name = "Bugs"
)
public class Bugs {

    @Inject
    private Logger logger;

    public static DamageType MY_DAMAGE_TYPE = new DamageType() {
        @Override
        public String getId() {
            return "bugs:mine";
        }

        @Override
        public String getName() {
            return "mine";
        }
    };

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

    }

    @Listener(order = Order.LAST)
    @IsCancelled(Tristate.FALSE)
    public void debugListener(DamageEntityEvent event) {
        if (event.getTargetEntity() instanceof Player) {
            ((Player)event.getTargetEntity()).sendMessage(Text.of("<<" + event.getFinalDamage()));
            event.getTargetEntity().offer(Keys.HEALTH, event.getTargetEntity().get(Keys.MAX_HEALTH).get());
        }
    }

    @Listener
    public void onIndirectEntityDamage(DamageEntityEvent event,
                                       @First(typeFilter = IndirectEntityDamageSource.class)
                                               IndirectEntityDamageSource indirectEntityDamageSource) {
        Projectile projectile = (Projectile) indirectEntityDamageSource.getSource();
        if (projectile.getType() == EntityTypes.TIPPED_ARROW && event.getTargetEntity() instanceof Living) {
            event.setCancelled(true);
            MyDamageSourceBuilder builder = new MyDamageSourceBuilder();
            builder.type(MY_DAMAGE_TYPE);
            MyDamageSource build = builder.build();

            try (CauseStackManager.StackFrame frame = Sponge.getCauseStackManager().pushCauseFrame()) {
                MyDomain myDomain = new MyDomain();
                EventContext ecbuild = EventContext.builder().add(MyContextKeys.TEST, myDomain).build();
                Sponge.getCauseStackManager().pushCause(Cause.of(ecbuild, myDomain));
                event.getTargetEntity().damage(10, build);
            }
        }
    }


    @Listener
    public void onMyDamage(DamageEntityEvent event,
                              @First(typeFilter = MyDamageSource.class)
                                      MyDamageSource damageSource) {
        event.setBaseDamage(15);
    }

}
