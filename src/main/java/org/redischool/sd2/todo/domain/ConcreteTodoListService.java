package org.redischool.sd2.todo.domain;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

/**
 * Concrete manager for the TODO list.
 */
@Service
final class ConcreteTodoListService implements TodoListService {
    List<Items> currentItems = new ArrayList();

    /**
     * Add  one time task without deadline
     *
     * @param label
     */
    @Override
    public void addTask(String label) {
        Items item = new OneTimeTask(null, label);
        for (Items items : currentItems) {

            if (items.getLabel().equalsIgnoreCase(label)) {
                return;
            }
        }
        currentItems.add(item);
    }

    /**
     * Add one time task with deadline
     *
     * @param label
     * @param deadline
     */
    @Override
    public void addTaskWithDeadline(String label, LocalDate deadline) {
        OneTimeTask item = new OneTimeTask(deadline.toString(), label);
        for (Items items : currentItems) {
            OneTimeTask task = (OneTimeTask) items;
            if (task.getLabel().equalsIgnoreCase(label) && (task.getDeadLine() != null && task.getDeadLine().equalsIgnoreCase(deadline.toString()))) {
                return;
            }
        }
        item.setDeadLine(deadline.toString());
        currentItems.add(item);
    }

    /**
     * add recurring task
     *
     * @param label
     * @param recurrencePeriod
     */
    @Override
    public void addRecurringTask(String label, Period recurrencePeriod) {
        Items item = new RecurringTask(null, recurrencePeriod.toString(), label);
        int j = 0;
        for (Items items : currentItems) {
            if (items.getLabel().equalsIgnoreCase(label)) {
                RecurringTask task = (RecurringTask) items;
                if (task.getPeriod() != null && task.getPeriod().equals(recurrencePeriod.toString())) {
                    return;
                }
                j++;
            }
        }
        if (j == 0) {
            currentItems.add(item);
        }
    }

    /**
     * add Shopping items
     *
     * @param label
     * @param amount
     */
    @Override
    public void addShoppingItem(String label, int amount) {
        Items item = new ShoppingList(amount, label);
        int j = 0;
        for (int i = 0; i < currentItems.size(); i++) {
            if (currentItems.get(i).getLabel().equalsIgnoreCase(label)) {
                ShoppingList oldItem = (ShoppingList) currentItems.get(i);
                oldItem.setAmount(amount + oldItem.getAmount());
                currentItems.set(i, oldItem);
                j++;
            }
        }

        if (j == 0) {
            currentItems.add(item);
        }
    }

    /**
     * mark an item as completed
     *
     * @param itemId
     */
    @Override
    public void markCompleted(String itemId) {
        for (Items items : currentItems) {
            if (items.getId().equals(itemId)) {
                this.currentItems.remove(items);
                break;
            }
        }
    }

    @Override
    public void updateRecurringTasks() {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Display current list of items
     *
     * @return
     */
    public List<Items> currentItems() {
        return currentItems;
    }
}
