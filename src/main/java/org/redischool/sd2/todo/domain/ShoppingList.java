package org.redischool.sd2.todo.domain;

import java.util.UUID;

public class ShoppingList implements Items {

    private Integer amount;
    private String label;
    private String id;

    public ShoppingList(Integer amount, String label) {
        this.amount = amount;
        this.label = label;
        this.id= UUID.randomUUID().toString();;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public ItemTypes getType() {
        return ItemTypes.SHOPPING_LIST;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount){
        this.amount = amount;
    }
    @Override
    public String getId() { return id; }
}
