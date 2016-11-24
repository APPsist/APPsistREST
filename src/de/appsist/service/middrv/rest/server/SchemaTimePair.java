package de.appsist.service.middrv.rest.server;

import de.appsist.service.middrv.entity.MachineSchema;

public class SchemaTimePair {
    private MachineSchema schema;
    private long timeOfLastUpdate;

    public SchemaTimePair(MachineSchema schema, long timeOfLastUpdate) {
        this.schema = schema;
        this.timeOfLastUpdate = timeOfLastUpdate;
    }

    public SchemaTimePair(MachineSchema schema) {
        this.schema = schema;
        this.timeOfLastUpdate = System.currentTimeMillis();
    }

    public MachineSchema getSchema() {
        return schema;
    }

    public long getTimeOfLastUpdate() {
        return timeOfLastUpdate;
    }

    public void updateTimeOfLastUpdate() {
        timeOfLastUpdate = System.currentTimeMillis();
    }
}
