package de.appsist.service.middrv.rest.server;


import de.appsist.service.middrv.entity.Machine;

public interface HeartBeatHandler {
    void handleLostMachine(Machine machine);
}
