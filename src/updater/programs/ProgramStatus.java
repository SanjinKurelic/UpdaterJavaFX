package updater.programs;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author Sanjin Kurelić
 */
public class ProgramStatus {

    private static List<Program> programs;
    private static int programIndex;

    static
    {
        programs = StoredPrograms.getPrograms();
        programIndex = -1;
    }

    public static synchronized Program getProgram()
    {
        programIndex++;
        if (programIndex >= programs.size() || programIndex < 0)
        {
            return null;
        }
        return programs.get(programIndex);
    }

    public static synchronized Program getDownloadableProgram()
    {
        for (Program program : programs)
        {
            if (program.needsToBeDownloaded())
            {
                program.setDownloaded();
                return program;
            }
        }
        return null;
    }

    public static synchronized List<Program> getAllPrograms()
    {
        return programs;
    }

    public static void savePrograms()
    {
        StoredPrograms.setPrograms(programs);
    }

    public static String getJsonProgramDefinition()
    {
        StringBuilder jsonRequest = new StringBuilder();
        String variableType;
        String comma = "";

        Class<?> programClass = Program.class;
        jsonRequest.append("{");

        // Variables
        jsonRequest.append("\"variables\":[");
        Field[] variables = programClass.getDeclaredFields();

        for (Field variable : variables)
        {
            jsonRequest.append(comma);
            // Name
            jsonRequest.append("\"").append(variable.getName()).append("\":");
            // Type
            variableType = variable.getType().toString().toLowerCase();
            if (variableType.contains("string"))
            {
                variableType = "string";
            }
            jsonRequest.append("\"").append(variableType).append("\"");
            comma = ",";
        }

        // Methods
        Method[] methods = programClass.getMethods();
        jsonRequest.append("],\"methods\":[");
        comma = "";
        String comma2 = "";
        for (Method method : methods)
        {
            if (Modifier.isPublic(method.getModifiers()))
            {
                jsonRequest.append(comma);
                jsonRequest.append("\"").append(method.getName()).append("\":[");
                Parameter[] parameters = method.getParameters();
                comma2 = "";
                for (Parameter parameter : parameters)
                {
                    jsonRequest.append(comma2);
                    // Name
                    jsonRequest.append("\"").append(parameter.getName()).append("\":");
                    // Type
                    variableType = parameter.getType().toString().toLowerCase();
                    if (variableType.contains("string"))
                    {
                        variableType = "string";
                    }
                    jsonRequest.append("\"").append(variableType).append("\"");
                    comma2 = ",";
                }
                jsonRequest.append("]");
                comma = ",";
            }
        }
        jsonRequest.append("]}");

        return jsonRequest.toString();
    }

}
