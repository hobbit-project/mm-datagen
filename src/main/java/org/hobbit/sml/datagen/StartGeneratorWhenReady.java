package org.hobbit.sml.datagen;

import org.hobbit.sdk.utils.CommandSender;
import org.hobbit.sdk.utils.commandreactions.CommandReaction;
import org.hobbit.core.Commands;

public class StartGeneratorWhenReady implements CommandReaction {

    private boolean generatorReady = false;
    private boolean commandSent = false;

    public StartGeneratorWhenReady() {

    }

    @Override
    public void  handleCmd(Byte command, byte[] data, String replyTo){
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
