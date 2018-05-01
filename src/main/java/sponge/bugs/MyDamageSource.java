package sponge.bugs;

import org.spongepowered.api.event.cause.entity.damage.source.common.AbstractDamageSource;
import org.spongepowered.api.event.cause.entity.damage.source.common.AbstractDamageSourceBuilder;


public class MyDamageSource extends AbstractDamageSource {
    protected MyDamageSource(AbstractDamageSourceBuilder<?, ?> builder) {
        super(builder);
    }
}
