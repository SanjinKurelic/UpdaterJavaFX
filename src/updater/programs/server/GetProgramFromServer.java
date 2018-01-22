package updater.programs.server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.download.RmiDownloadFile;
import updater.gui.ProgressBarStatus;
import updater.gui.UpdaterGui;
import updater.gui.WindowController;
import updater.programs.Program;
import updater.programs.ProgramStatus;

/**
 * @author Sanjin Kurelić
 */
public class GetProgramFromServer implements Runnable {

    private final WindowController gui;

    public GetProgramFromServer(WindowController gui)
    {
        this.gui = gui;
    }

    @Override
    public void run()
    {
        while (true)
        {
            try
            {
                Thread.sleep(ServerStatus.MAX_COMMUNICATION_TIME);
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(GetProgramFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            getProgram();
        }
    }

    private synchronized void getProgram()
    {
        while (ServerStatus.readingFromServer == true)
        {
            try
            {
                wait();
            }
            catch (InterruptedException ex)
            {
                Logger.getLogger(GetProgramFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ServerStatus.readingFromServer = true;

        Program program = ProgramStatus.getDownloadableProgram();
        if (program != null)
        {

            try
            {
                Registry reg = LocateRegistry.getRegistry("localhost", 51513);
                RmiDownloadFile inter = (RmiDownloadFile) reg.lookup("remoteObject");

                gui.refreshGui(ProgressBarStatus.UPDATING, program.getName());

                byte[] file = inter.downloadFileFromServer(program.getName());
                String programName = program.getName().replace("Program", "Updated Program");
                String programsDirectory = UpdaterGui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
                programsDirectory = programsDirectory.replaceAll("\\\\", "/");
                programsDirectory = programsDirectory.substring(1, programsDirectory.lastIndexOf("/dist")) + "/programs/";

                try (FileOutputStream out = new FileOutputStream(new File(programsDirectory + programName)))
                {
                    out.write(file);
                    out.flush();
                    gui.refreshGui(ProgressBarStatus.UPDATED, program.getName());
                }
                catch (FileNotFoundException ex)
                {
                    Logger.getLogger(GetProgramFromServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                catch (IOException ex)
                {
                    Logger.getLogger(GetProgramFromServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            catch (RemoteException | NotBoundException ex)
            {
                Logger.getLogger(GetProgramFromServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ServerStatus.readingFromServer = false;

        notifyAll();
    }

}
