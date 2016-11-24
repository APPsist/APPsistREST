package de.appsist.service.middrv.rest.server;

import de.appsist.service.middrv.entity.Machine;
import de.appsist.service.middrv.rest.Constants;
import de.appsist.service.middrv.rest.ContentType;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.HttpServerResponse;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HeartBeatParser implements ContentParser {

    private long heartBeatInterval;
    private ConcurrentHashMap<Machine, SchemaTimePair> schemas;
    private HeartBeatHandler heartBeatHandler;

    public HeartBeatParser(long heartBeatInterval, ConcurrentHashMap<Machine, SchemaTimePair> schemas, HeartBeatHandler heartBeatHandler) {
        this.heartBeatInterval = heartBeatInterval;
        this.schemas = schemas;
        this.heartBeatHandler = heartBeatHandler;

        // Create background thread which checks if a machine did not report back within heartBeatinterval
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                checkTimedOutMachines();
            }
        };
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(runnable, 0, heartBeatInterval, TimeUnit.MILLISECONDS);

    }

    @Override
    public void parseContent(byte[] content, ContentType contentType, HttpServerResponse response, ContentType responseContentType, HttpServerRequest request) throws Exception {
        // reset timer of machine
        String machineId = request.params().get(Constants.GET_PARAM_MACHINE_ID);

        for (Map.Entry<Machine, SchemaTimePair> entry : schemas.entrySet()) {
            if (entry.getKey().getMachineID().equals(machineId)) {
                entry.getValue().updateTimeOfLastUpdate();
            }
        }

        // return OK
        response.setStatusCode(200);
        response.end();
    }

    @Override
    public boolean allowEmptyContent() {
        return true;
    }

    @Override
    public boolean allowResponseContentTypeEmpty() {
        return false;
    }

    private void checkTimedOutMachines() {
        for (Iterator<Map.Entry<Machine, SchemaTimePair> > it = schemas.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Machine, SchemaTimePair> entry = it.next();
            if (entry.getValue().getTimeOfLastUpdate() + heartBeatInterval < System.currentTimeMillis() ) {
                // call callback
                heartBeatHandler.handleLostMachine(entry.getKey());
                // remove from schemas
                it.remove();
            }
        }
    }
}
