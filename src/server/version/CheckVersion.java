package server.version;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.version.xml.ProgramInfo;

/**
 * @author Sanjin Kurelić
 */
public class CheckVersion extends Thread {

    private static final Logger LOG = Logger.getLogger(VersionSocket.class.getName());

    private final Socket client;
    private final BufferedReader params;
    private final PrintWriter output;
    private final ProgramInfo programInfo;

    public CheckVersion(Socket client, ProgramInfo programInfo) throws IOException
    {
        this.client = client;
        this.programInfo = programInfo;
        params = new BufferedReader(new InputStreamReader(client.getInputStream()));
        output = new PrintWriter(client.getOutputStream());
    }

    @Override
    public void run()
    {
        String input;

        try
        {
            while ((input = params.readLine()) != null)
            {
                output.write(getProgramVersion(input) + "\n");
                output.flush();
            }
            client.close();
        }
        catch (IOException ex)
        {
            Logger.getLogger(CheckVersion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String getProgramVersion(String programName)
    {
        LOG.info(String.format("Checking version for program: %s", programName));
        String ip = client.getRemoteSocketAddress().toString();
        ip = ip.substring(1, ip.indexOf(':'));
        return programInfo.getProgramVersion(ip, programName);
    }

}
