package server.version.xml;

/**
 * @author Sanjin Kurelić
 */
public class ClientXML {

    String ip;
    ProgramsXML programs;

    @Override
    public String toString()
    {
        return "Client (" + ip + ")";
    }

}
