package updater.programs;

/**
 * @author Sanjin Kurelić
 */
public class ProgramVersion {

    private final int mainVersion;
    private final int minorVersion;
    private final int revisionVersion;

    public ProgramVersion(int mainVersion, int minorVersion, int revisionVersion)
    {
        this.mainVersion = mainVersion;
        this.minorVersion = minorVersion;
        this.revisionVersion = revisionVersion;
    }

    public int getMainVersion()
    {
        return mainVersion;
    }

    public int getMinorVersion()
    {
        return minorVersion;
    }

    public int getRevisionVersion()
    {
        return revisionVersion;
    }

}
