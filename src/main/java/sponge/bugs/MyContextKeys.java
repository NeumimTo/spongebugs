package sponge.bugs;

import org.spongepowered.api.event.cause.EventContextKey;

public class MyContextKeys {

    public static final EventContextKey<MyDomain> TEST = EventContextKey
            .builder(MyDomain.class)
            .name("myDoamin")
            .id("mine.myDoamin")
            .build();

}
