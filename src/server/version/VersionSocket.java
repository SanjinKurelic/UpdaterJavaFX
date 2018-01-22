package server.version;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import server.version.xml.ProgramInfo;

/**
 * @author Sanjin Kurelić
 */
public class VersionSocket {

    private static final int SAVE_INTERVAL = 1000 * 30;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // Connect to XML database
        ProgramInfo programInfo = new ProgramInfo();
        programInfo.showProgramInfo();

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run()
            {
                programInfo.saveProgramInfo();
            }
        }, 0, SAVE_INTERVAL);

        // Start server
        ServerSocket ss = new ServerSocket(51512);
        Socket client;

        Logger.getLogger(VersionSocket.class.getName()).info("Socket Server started");

        while (true)
        {
            client = ss.accept();
            new CheckVersion(client, programInfo).start();
        }
    }

}
