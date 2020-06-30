package org.redischool.sd2.todo.domain;

import java.time.Clock;
import java.util.Date;
import java.util.UUID;

public class RecurringTask implements Items {

    private Integer frequency;
    private String period;
    private String label;
    private String id;
    private Date addedDate;


    public RecurringTask(Integer frequency,String period, String label) {
        this.frequency = frequency;
        this.period = period;
        this.label = label;
        this.id= UUID.randomUUID().toString();
        this.addedDate= new Date();
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public ItemTypes getType() {
        return ItemTypes.RECURRING_TASK;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public String getPeriod(){return period; }

    @Override
    public String getId() { return id; }

    public Date getAddedDate(){ return addedDate; }
}
