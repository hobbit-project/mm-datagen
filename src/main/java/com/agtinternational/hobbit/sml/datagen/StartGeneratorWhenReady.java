package com.agtinternational.hobbit.sml.datagen;

import com.agtinternational.hobbit.sdk.utils.CommandSender;
import com.agtinternational.hobbit.sdk.utils.commandreactions.CommandReaction;
import org.hobbit.core.Commands;

public class StartGeneratorWhenReady implements CommandReaction {

    private boolean generatorReady = false;
    private boolean commandSent = false;

    public StartGeneratorWhenReady() {

    }

    public void accept(Byte command, byte[] bytes) {
        if (command.byteValue() == 3) {
            this.generatorReady = true;
        }

        synchronized(this) {
            if (this.generatorReady && !this.commandSent) {
                this.commandSent = true;

                try {
                    new CommandSender(Commands.DATA_GENERATOR_START_SIGNAL).send();
                } catch (Exception var6) {
                    System.out.println(var6.getMessage());
                }
            }

        }
    }
}
