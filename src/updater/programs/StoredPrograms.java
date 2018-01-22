package updater.programs;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sanjin Kurelić
 */
public class StoredPrograms {

    public static List<Program> getPrograms()
    {
        List<Program> programs = new ArrayList<>();
        try
        {
            if (!(new File("programs.dat").exists()))
            {
                firstRun();
            }
            ObjectInputStream reader = new ObjectInputStream(new FileInputStream("programs.dat"));

            while (true)
            {
                Object program = reader.readObject();

                if (program instanceof Program)
                {
                    programs.add((Program) program);
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(StoredPrograms.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (EOFException ex)
        {
            // End of file
        }
        catch (IOException | ClassNotFoundException ex)
        {
            Logger.getLogger(StoredPrograms.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Loaded");
        return programs;
    }

    public static void setPrograms(List<Program> programs)
    {
        ObjectOutputStream repozitorij;
        try
        {
            if (!(new File("programs.dat").exists()))
            {
                createFile();
            }
            repozitorij = new ObjectOutputStream(new FileOutputStream("programs.dat"));
            for (Program program : programs)
            {
                repozitorij.writeObject(program);
            }
        }
        catch (FileNotFoundException ex)
        {
            Logger.getLogger(StoredPrograms.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IOException ex)
        {
            Logger.getLogger(StoredPrograms.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Saved");
    }

    public static void firstRun() throws IOException
    {
        GetProgramsPath programsPaths = new GetProgramsPath();
        List<Program> programs = programsPaths.getPaths();

        // Create file
        createFile();

        // Store default values
        ObjectOutputStream repozitorij = new ObjectOutputStream(new FileOutputStream("programs.dat"));
        for (Program program : programs)
        {
            repozitorij.writeObject(program);
        }

        System.out.println(programs);
        System.out.println("Generated");
    }

    private static void createFile() throws IOException
    {
        new File("programs.dat").createNewFile();
    }

}
