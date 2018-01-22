package server.version.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import server.version.ServerDirectory;

/**
 * @author Sanjin Kurelić
 */
public class ProgramInfo {

    private final String databasePath;
    private SAXHandler handler;
    private List<ProgramXML> programs;

    public ProgramInfo()
    {
        databasePath = ServerDirectory.getDirectory() + "data.xml";
    }

    // Read XML
    public void showProgramInfo()
    {
        SAXParserFactory factory = SAXParserFactory.newInstance();

        if (!(new File(databasePath)).exists())
        {
            return;
        }
        try
        {
            InputStream xmlFile = new FileInputStream(databasePath);

            SAXParser saxParser = factory.newSAXParser();
            handler = new SAXHandler();
            saxParser.parse(xmlFile, handler);

            // Store in memory
            programs = handler.getPrograms();

            System.out.println("Program versions:");
            for (ProgramXML program : programs)
            {
                System.out.println(program);
            }

            System.out.println("\nClient versions:");
            for (ClientXML client : handler.getClients())
            {
                System.out.println(client + " has following programs:");
                for (ProgramXML program : client.programs.programs)
                {
                    System.out.println("\t" + program);
                }
            }

        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            Logger.getLogger(ProgramInfo.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public String getProgramVersion(String clientIp, String programName)
    {
        programName = programName.replace(".exe", "");
        if (programs != null)
        {
            for (ProgramXML program : programs)
            {
                if (program.name.equals(programName))
                {
                    updateClient(clientIp, programName, program.version);
                    return program.version.toString();
                }
            }
        }
        return "0.0.0";
    }

    public void updateClient(String clientIp, String programName, VersionXML version)
    {
        for (ClientXML client : handler.getClients())
        {
            if (client.ip.equals(clientIp))
            {
                for (ProgramXML program : client.programs.programs)
                {
                    if (program.name.equals(programName))
                    {
                        program.version = version;
                    }
                }
                return;
            }
        }
        createClient(clientIp);
    }

    private void createClient(String clientIp)
    {
        // Create new client
        ClientXML client = new ClientXML();
        client.ip = clientIp;
        client.programs = new ProgramsXML();
        client.programs.programs = handler.getPrograms();

        // Add to clients
        handler.getClients().add(client);
    }

    public void saveProgramInfo()
    {
        if (new SaveProgramInfo(handler.getData()).save())
        {
            System.out.println("Program info is saved");
        }
    }

}
