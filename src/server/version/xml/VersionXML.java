package server.version.xml;

/**
 * @author Sanjin Kurelić
 */
public class VersionXML {

    int mainVersion;
    int minorVersion;
    int revisionVersion;

    @Override
    public String toString()
    {
        return mainVersion + "." + minorVersion + "." + revisionVersion;
    }

}
