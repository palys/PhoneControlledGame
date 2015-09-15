package server;

import javax.microedition.io.StreamConnection;
import java.io.InputStream;
import java.io.OutputStream;

public class Player {

    private int playerNumber;
    private String playerName;
    private boolean isAuthenticated = false;

    private int score;

    private StreamConnection connection;
    private InputStream inputStream;
    private OutputStream outputStream;

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public StreamConnection getConnection() {
        return connection;
    }

    public void setConnection(StreamConnection connection) {
        this.connection = connection;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
