package server.download;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

/**
 * @author Sanjin Kurelić
 */
public class DownloadRmi {

    public static void main(String[] args) throws RemoteException, AlreadyBoundException
    {
        Registry reg = LocateRegistry.createRegistry(51513);

        RmiDownloadFileImpl impl = new RmiDownloadFileImpl();
        reg.bind("remoteObject", impl);

        Logger.getLogger(DownloadRmi.class.getName()).info("RMI Server started");
    }

}
