package org.redischool.sd2.todo.domain;

import java.util.UUID;

public class OneTimeTask implements Items {

    private String deadLine;
    private String label;
    private String id;

    public OneTimeTask(String deadLine, String label) {
        this.deadLine = deadLine;
        this.label = label;
        this.id= UUID.randomUUID().toString();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public ItemTypes getType() {
        return ItemTypes.ONE_TIME_TASK;
    }

    @Override
    public String getId() { return id; }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine){
        this.deadLine = deadLine;
    }
}
