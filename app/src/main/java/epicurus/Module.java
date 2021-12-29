package epicurus;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import proto.CommandFactory;
import proto.CommandParser;


class Module extends AbstractModule {
    @Provides
    @Singleton
    CommandParser provideCommandParser(CommandFactory factory) {
        return new CommandParser(factory);
    }

    @Override
    protected void configure() {
        install(new FactoryModuleBuilder()
            .build(TcpHandleFactory.class));
    }
}