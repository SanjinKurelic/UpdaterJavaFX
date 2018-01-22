package server.version;

import updater.gui.UpdaterGui;

/**
 * @author Sanjin Kurelić
 */
public class ServerDirectory {

    public static String getDirectory()
    {
        String directory = UpdaterGui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        directory = directory.replaceAll("\\\\", "/");
        directory = directory.substring(1, directory.lastIndexOf("/dist")) + "/serverprograms/";
        return directory;
    }

}
