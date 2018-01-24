package com.nc.socket;

import com.nc.model.Task;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TaskManagerSocket
{
    private static Socket socket;

    public static List<Task> sendDataToServer(String action, Object task)
    {
        //Socket socket = getSocket();
        List<Task> request = new ArrayList<>();

        //Повисает при повторном испозьзовании, решил пока забить
        //Создаю каждый раз новый сокет
        try (Socket socket = new Socket("localhost", 11111))
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());

            HashMap<String, Object> map = new HashMap<>();
            map.put(action, task);

            outputStream.writeObject(map);
            outputStream.flush();

            request = (List<Task>) inputStream.readObject();
        }
        catch (Exception x)
        {
            x.printStackTrace();
        }
        return request;
    }

    private static synchronized Socket getSocket()
    {
        if (socket == null)
        {
            int serverPort = 11111;
            String address = "127.0.0.1";
            try
            {
                InetAddress ipAddress = InetAddress.getByName(address);
                socket = new Socket(ipAddress, serverPort);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return socket;
    }
}
