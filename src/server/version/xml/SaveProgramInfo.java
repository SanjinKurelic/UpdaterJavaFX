package server.version.xml;

import java.io.File;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import server.version.ServerDirectory;

/**
 * @author Sanjin Kurelić
 */
public class SaveProgramInfo {

    private final DataXML data;
    private final String databasePath;
    private boolean clientList = false;

    public SaveProgramInfo(DataXML data)
    {
        this.data = data;
        databasePath = ServerDirectory.getDirectory() + "data2.xml";
    }

    public boolean save()
    {
        try
        {
            Document xmlFile = generateXMLDocument();
            saveToFile(xmlFile);
        }
        catch (ParserConfigurationException | TransformerException e)
        {
            System.err.println(e.getMessage());
            return false;
        }

        return true;
    }

    private Document generateXMLDocument() throws ParserConfigurationException
    {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element dataElement = document.createElement("data");
        dataElement.appendChild(generatePrograms(document, data.programs.programs));
        dataElement.appendChild(generateClients(document, data.clients.clients));

        document.appendChild(dataElement);

        return document;
    }

    private void saveToFile(Document xmlFile) throws TransformerException
    {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(xmlFile);
        StreamResult result = new StreamResult(new File(databasePath));

        transformer.transform(source, result);
    }

    private Node generatePrograms(Document document, List<ProgramXML> programs)
    {
        Element programsElement = document.createElement("programs");

        for (ProgramXML program : programs)
        {
            programsElement.appendChild(generateProgram(document, program));
        }

        return programsElement;
    }

    private Node generateClients(Document document, List<ClientXML> clients)
    {
        clientList = true;
        Element clientsElement = document.createElement("clients");

        for (ClientXML client : clients)
        {
            clientsElement.appendChild(generateClient(document, client));
        }

        return clientsElement;
    }

    private Node generateProgram(Document document, ProgramXML program)
    {
        Element programElement = document.createElement("program");
        programElement.setAttribute("name", program.name);
        if (!clientList && (program.description != null))
        {
            programElement.setAttribute("description", program.description);
        }
        programElement.appendChild(generateVersion(document, program.version));

        return programElement;
    }

    private Node generateClient(Document document, ClientXML client)
    {
        Element clientElement = document.createElement("client");
        clientElement.setAttribute("ip", client.ip);
        clientElement.appendChild(generatePrograms(document, client.programs.programs));

        return clientElement;
    }

    private Node generateVersion(Document document, VersionXML version)
    {
        Element versionElement = document.createElement("version");

        Element mainVersion = document.createElement("mainVersion");
        mainVersion.appendChild(document.createTextNode(version.mainVersion + ""));
        versionElement.appendChild(mainVersion);

        Element minorVersion = document.createElement("minorVersion");
        minorVersion.appendChild(document.createTextNode(version.minorVersion + ""));
        versionElement.appendChild(minorVersion);

        Element revisionVersion = document.createElement("revisionVersion");
        revisionVersion.appendChild(document.createTextNode(version.revisionVersion + ""));
        versionElement.appendChild(revisionVersion);

        return versionElement;
    }

}
