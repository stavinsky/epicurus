package epicurus;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Retention;
import javax.inject.Qualifier;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import proto.CommandFactory;
import proto.CommandParser;

@Qualifier
@Retention(RUNTIME)
@interface Tst {} 


class Module extends AbstractModule{
    @Provides
    @Tst
    static String provideMessage(){
        return "hello_world";
    }

    @Provides
    @Singleton
    CommandParser provideCommandParser(CommandFactory factory) {
        return new CommandParser(factory);
    }

    @Override
	protected void configure()
	{
        install(new FactoryModuleBuilder()
            .build(TcpHandleFactory.class));
    }
}