package com.lb.game.model;

public class GameType {
    private Byte gameid;

    private String gamename;

    private Byte status;

    public Byte getGameid() {
        return gameid;
    }

    public void setGameid(Byte gameid) {
        this.gameid = gameid;
    }

    public String getGamename() {
        return gamename;
    }

    public void setGamename(String gamename) {
        this.gamename = gamename == null ? null : gamename.trim();
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}