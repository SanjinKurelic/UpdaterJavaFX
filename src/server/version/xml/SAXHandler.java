package server.version.xml;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Sanjin Kurelić
 */
public class SAXHandler extends DefaultHandler {

    private ProgramsXML programs;
    private ClientsXML clients;

    private DataXML data;
    private ProgramXML program;
    private ClientXML client;
    private VersionXML version;

    private String currentElement;
    private boolean clientPart = false;

    public List<ClientXML> getClients()
    {
        return data.clients.clients;
    }

    public List<ProgramXML> getPrograms()
    {
        return data.programs.programs;
    }

    public DataXML getData()
    {
        return data;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        String value = new String(ch, start, length).trim();
        if (value.length() == 0)
        {
            return;
        }
        switch (currentElement)
        {
            case "mainVersion":
                version.mainVersion = Integer.valueOf(value);
                break;
            case "minorVersion":
                version.minorVersion = Integer.valueOf(value);
                break;
            case "revisionVersion":
                version.revisionVersion = Integer.valueOf(value);
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        switch (qName)
        {
            case "programs":
                if (clientPart)
                {
                    client.programs = programs;
                }
                else
                {
                    data.programs = programs;
                }
                programs = null;
                break;
            case "program":
                programs.programs.add(program);
                program = null;
                break;
            case "version":
                program.version = version;
                version = null;
                break;
            case "clients":
                data.clients = clients;
                clients = null;
                break;
            case "client":
                clients.clients.add(client);
                client = null;
                break;
        }
        currentElement = null;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
    {
        currentElement = qName;

        switch (currentElement)
        {
            case "data":
                data = new DataXML();
                break;
            case "programs":
                programs = new ProgramsXML();
                break;
            case "program":
                program = new ProgramXML();
                program.name = attributes.getValue("name");
                program.description = attributes.getValue("description");
                break;
            case "version":
                version = new VersionXML();
                break;
            case "clients":
                clients = new ClientsXML();
                clientPart = true;
                break;
            case "client":
                client = new ClientXML();
                client.ip = attributes.getValue("ip");
                break;
        }
    }

}
