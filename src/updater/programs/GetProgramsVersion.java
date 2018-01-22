package updater.programs;

import java.util.Random;

/**
 * @author Sanjin Kurelić
 */
public class GetProgramsVersion {

    private static final int MAX_NUMBER_OF_PROGRAMS = 20;

    public static ProgramVersion getProgramVersion(String path)
    {
        Random r = new Random();
        return new ProgramVersion(r.nextInt(MAX_NUMBER_OF_PROGRAMS),
                                  r.nextInt(MAX_NUMBER_OF_PROGRAMS),
                                  r.nextInt(MAX_NUMBER_OF_PROGRAMS));
    }

}
