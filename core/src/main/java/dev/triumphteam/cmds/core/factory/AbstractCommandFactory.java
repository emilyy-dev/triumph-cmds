/**
 * MIT License
 *
 * Copyright (c) 2019-2021 Matt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.triumphteam.cmds.core.factory;

import dev.triumphteam.cmds.core.annotations.Command;
import dev.triumphteam.cmds.core.exceptions.CommandRegistrationException;
import dev.triumphteam.cmds.core.BaseCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractCommandFactory<C extends dev.triumphteam.cmds.core.Command> {

    private String name;
    private final List<String> alias = new ArrayList<>();

    protected AbstractCommandFactory(@NotNull final BaseCommand baseCommand) {
        extractCommandNames(baseCommand);
    }

    /**
     * Abstract method so children can handle the return of the new {@link dev.triumphteam.cmds.core.Command}.
     *
     * @return A {@link dev.triumphteam.cmds.core.Command} implementation.
     */
    @NotNull
    public abstract C create();

    /**
     * Used for the child factories to get the command name.
     *
     * @return The command name.
     */
    @NotNull
    protected String getName() {
        return name;
    }

    /**
     * Used for the child factories to get a {@link List<String>} with the command's alias.
     *
     * @return The command alias.
     */
    @NotNull
    protected List<String> getAlias() {
        return alias;
    }

    /**
     * Helper method for getting the command names from the command annotation.
     *
     * @param baseCommand The {@link BaseCommand} instance.
     * @throws CommandRegistrationException In case something goes wrong should throw exception.
     */
    private void extractCommandNames(final BaseCommand baseCommand) throws CommandRegistrationException {
        final Class<? extends BaseCommand> commandClass = baseCommand.getClass();
        final Command commandAnnotation = AnnotationUtil.getAnnotation(commandClass, Command.class);

        if (commandAnnotation == null) {
            final String commandName = baseCommand.getCommand();
            if (commandName == null) {
                throw new CommandRegistrationException("Command name or \"@" + dev.triumphteam.cmds.core.Command.class.getSimpleName() + "\" annotation missing", commandClass);
            }

            this.name = commandName;
            this.alias.addAll(baseCommand.getAlias());
        } else {
            this.name = commandAnnotation.value();
            Collections.addAll(this.alias, commandAnnotation.alias());
        }

        this.alias.addAll(baseCommand.getAlias());

        if (this.name.isEmpty()) {
            throw new CommandRegistrationException("Command name must not be empty", commandClass);
        }
    }

}