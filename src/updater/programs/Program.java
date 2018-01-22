package updater.programs;

import java.io.Serializable;
import updater.gui.ProgressBarStatus;

/**
 * @author Sanjin Kurelić
 */
public class Program implements Serializable {

    static final long serialVersionUID = -6029568751172572559L;

    // Program info
    private String name;
    private String path;
    // Program version info
    private int mainVersion;
    private int minorVersion;
    private int revisionVersion;
    // Latest program version info
    private int latestMainVersion;
    private int latestMinorVersion;
    private int latestRevisionVersion;

    // Additional data
    private ProgressBarStatus status = ProgressBarStatus.CHECKING_VERSION;
    private boolean needToBeDownload;

    public Program(String name, String path, ProgramVersion programVersion)
    {
        this.name = name;
        this.path = path;
        this.mainVersion = programVersion.getMainVersion();
        this.minorVersion = programVersion.getMinorVersion();
        this.revisionVersion = programVersion.getRevisionVersion();
        this.needToBeDownload = false;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getVersion()
    {
        return mainVersion + "." + minorVersion + "." + revisionVersion;
    }

    public void setVersion(int mainVersion, int minorVersion, int revisionVersion)
    {
        this.mainVersion = mainVersion;
        this.minorVersion = minorVersion;
        this.revisionVersion = revisionVersion;
    }

    public boolean needsToBeDownloaded()
    {
        return this.needToBeDownload;
    }

    public void setStatus(ProgressBarStatus status)
    {
        this.status = status;
    }

    public ProgressBarStatus getStatus()
    {
        return status;
    }

    public void setDownloaded()
    {
        this.needToBeDownload = false;
        mainVersion = latestMainVersion;
        minorVersion = latestMinorVersion;
        revisionVersion = latestRevisionVersion;
    }

    /**
     * Returns true if version from parameter is bigger than this version
     *
     * @param mainVersion
     * @param minorVersion
     * @param revisionVersion
     * @return
     */
    public boolean checkVersion(int mainVersion, int minorVersion, int revisionVersion)
    {
        if (mainVersion < this.mainVersion)
        {
            return false; // can't happen
        }

        if (mainVersion == this.mainVersion)
        {
            if (minorVersion < this.minorVersion)
            {
                return false; // can't happen
            }
            if (minorVersion == this.minorVersion)
            {
                if (revisionVersion <= this.revisionVersion)
                {
                    return false;
                }
            }
        }

        this.latestMainVersion = mainVersion;
        this.latestMinorVersion = minorVersion;
        this.latestRevisionVersion = revisionVersion;
        this.needToBeDownload = true;

        return true;
    }

    @Override
    public String toString()
    {
        return "Program{" + "name=" + name + ", path=" + path + ", mainVersion=" + mainVersion + ", minorVersion=" + minorVersion + ", revisionVersion=" + revisionVersion + ", latestMainVersion=" + latestMainVersion + ", latestMinorVersion=" + latestMinorVersion + ", latestRevisionVersion=" + latestRevisionVersion + ", needToBeDownload=" + needToBeDownload + "}\n";
    }

}
