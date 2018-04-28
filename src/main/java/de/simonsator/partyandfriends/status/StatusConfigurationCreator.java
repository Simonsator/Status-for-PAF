package de.simonsator.partyandfriends.status;

import de.simonsator.partyandfriends.api.PAFExtension;
import de.simonsator.partyandfriends.utilities.ConfigurationCreator;

import java.io.File;
import java.io.IOException;

/**
 * @author simonbrungs
 * @version 1.0.0 09.01.17
 */
public class StatusConfigurationCreator extends ConfigurationCreator {
	protected StatusConfigurationCreator(File pFile, PAFExtension pPlugin) throws IOException {
		super(pFile, pPlugin);
		readFile();
		loadDefaults();
		saveFile();
		process(configuration);
	}

	private void loadDefaults() {
		set("Commands.TopCommands.Status.Names", "status", "setstatus");
		set("Commands.TopCommands.Status.Permission", "");
		set("Messages.TooLessArguments", " &7You need to give a message.");
		set("Messages.MessageTooLong", " &7Your status can only be 100 characters long.");
		set("Messages.StatusWasSet", " &7Your status was set.");
	}
}
