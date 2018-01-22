package server.download;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import updater.gui.UpdaterGui;

/**
 * @author Sanjin Kurelić
 */
public class RmiDownloadFileImpl extends UnicastRemoteObject implements RmiDownloadFile {

    private static final Logger LOG = Logger.getLogger(RmiDownloadFileImpl.class.getName());
    private String programsDirectory;

    public RmiDownloadFileImpl() throws RemoteException
    {
        programsDirectory = UpdaterGui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        programsDirectory = programsDirectory.replaceAll("\\\\", "/");
        programsDirectory = programsDirectory.substring(1, programsDirectory.lastIndexOf("/dist")) + "/serverprograms/";
    }

    @Override
    public byte[] downloadFileFromServer(String programName) throws RemoteException
    {
        LOG.log(Level.INFO, "RMI download started for: {0}", programName);
        File f = new File(getFilePath(programName));
        byte[] file = new byte[(int) f.length()];

        try (FileInputStream in = new FileInputStream(f))
        {
            in.read(file, 0, file.length);
        }
        catch (FileNotFoundException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            LOG.log(Level.SEVERE, null, ex);
        }

        return file;
    }

    private String getFilePath(String programName)
    {
        return programsDirectory + programName.replace("Program", "Program Updated");
    }

}
