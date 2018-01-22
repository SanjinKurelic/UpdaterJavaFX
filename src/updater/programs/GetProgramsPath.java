package updater.programs;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import updater.gui.UpdaterGui;

/**
 * @author Sanjin Kurelić
 */
public class GetProgramsPath {

    private String programsDirectory;

    public GetProgramsPath()
    {
        programsDirectory = UpdaterGui.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        programsDirectory = programsDirectory.replaceAll("\\\\", "/");
        programsDirectory = programsDirectory.substring(1, programsDirectory.lastIndexOf("/dist")) + "/programs/";
    }

    public List<Program> getPaths()
    {
        List<Program> programs = new ArrayList<>();
        Hashtable<String, String> dir = new Hashtable<>();

        dir.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.fscontext.RefFSContextFactory");
        dir.put(Context.PROVIDER_URL, "file:" + programsDirectory);

        try
        {
            Context context = new InitialContext(dir);
            NamingEnumeration files = context.listBindings("");

            Binding file;
            Program program;
            String path;

            while (files.hasMore())
            {
                file = (Binding) files.next();
                path = file.getObject().toString();
                program = new Program(file.getName(), path, GetProgramsVersion.getProgramVersion(path));
                programs.add(program);
            }
        }
        catch (NamingException ex)
        {
            Logger.getLogger(GetProgramsPath.class.getName()).log(Level.SEVERE, null, ex);
        }
        return programs;
    }

}
