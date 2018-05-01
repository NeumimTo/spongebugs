package sponge.bugs;

import org.spongepowered.api.event.cause.entity.damage.source.common.AbstractDamageSourceBuilder;

public class MyDamageSourceBuilder extends AbstractDamageSourceBuilder<MyDamageSource, MyDamageSourceBuilder> {

    @Override
    public MyDamageSource build() {
        return new MyDamageSource(this);
    }

}
