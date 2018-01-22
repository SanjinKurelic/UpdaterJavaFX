package server.version.xml;

/**
 * @author Sanjin Kurelić
 */
public class ProgramXML {

    String name;
    String description;
    VersionXML version;

    @Override
    public String toString()
    {
        if (description == null)
        {
            return name + ": " + version;
        }
        return name + " (" + description + "): " + version;
    }

}
