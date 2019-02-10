package bgu.spl.net.api.bidi;

import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImplementation<T> implements Connections<T> {
    private ConcurrentHashMap<Integer,ConnectionHandler<T>> connectionHandlerHashMap = new ConcurrentHashMap<>();

    public void addConnection(ConnectionHandler<T> connection, int currentId)
    {
    	connectionHandlerHashMap.put(currentId,connection);
    }
    @Override
    public boolean send(int connectionId, T msg) {
        ConnectionHandler<T> connection = connectionHandlerHashMap.get(connectionId);
        if(connection == null){
            return false;
        }
        connection.send(msg);
        return true;
    }

    @Override
    public void broadcast(T msg) {
        for(ConnectionHandler<T> eachClient : connectionHandlerHashMap.values()){
            eachClient.send(msg);
        }

    }

    @Override
    public void disconnect(int connectionId) {
        connectionHandlerHashMap.remove(connectionId);
    }
}
