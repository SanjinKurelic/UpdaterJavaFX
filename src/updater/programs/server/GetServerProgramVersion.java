package updater.programs.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import updater.gui.ProgressBarStatus;
import updater.gui.WindowController;
import updater.programs.Program;
import updater.programs.ProgramStatus;

/**
 * @author Sanjin Kurelić
 */
public class GetServerProgramVersion implements Runnable {

    private final WindowController gui;

    public GetServerProgramVersion(WindowController gui)
    {
        this.gui = gui;
    }

    @Override
    public void run()
    {
        while (true)
        {
            checkProgramVersion();
        }
    }

    private synchronized void checkProgramVersion()
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

        Program program = ProgramStatus.getProgram();
        if (program != null)
        {
            int mainVersion = 0, minorVersion = 0, revisionVersion = 0;

            try (Socket s = new Socket("localhost", 51512);
                 BufferedReader serverResponse = new BufferedReader(new InputStreamReader(s.getInputStream())))
            {
                // Send program name to server
                s.getOutputStream().write((program.getName() + "\n").getBytes());
                s.getOutputStream().flush();

                while (!serverResponse.ready())
                {
                }
                String[] response = serverResponse.readLine().split("\\.");
                if (response.length != 3)
                {
                    throw new IOException("Wrong return parameters from server");
                }
                mainVersion = Integer.valueOf(response[0]);
                minorVersion = Integer.valueOf(response[1]);
                revisionVersion = Integer.valueOf(response[2]);
            }
            catch (IOException ex)
            {
                Logger.getLogger(GetServerProgramVersion.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (program.checkVersion(mainVersion, minorVersion, revisionVersion))
            {
                gui.refreshGui(ProgressBarStatus.START_UPDATING, program.getName());
            }
            else
            {
                gui.refreshGui(ProgressBarStatus.UPDATED, program.getName());
            }
        }

        ServerStatus.readingFromServer = false;

        notifyAll();
    }

}
