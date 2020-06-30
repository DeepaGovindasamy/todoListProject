package org.redischool.sd2.todo.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class ConcreteTodoListServiceTest {

    TodoListService test = new ConcreteTodoListService();

    @Test
    public void testAddTaskVerifyLabel() {
        test.addTask("Learn German");
        OneTimeTask oneTimeTask = (OneTimeTask) test.currentItems().get(0);
        assertEquals("Learn German", oneTimeTask.getLabel());
    }

    @Test
    public void testAddTaskVerifyDuplicate() {
        test.addTask("Learn German");
        test.addTask("Learn German");
        assertEquals(1, test.currentItems().size());
    }

    @Test
    public void testAddTaskWithDeadlineVerifyDeadline() {
        test.addTaskWithDeadline("Go to Gym", LocalDate.of(2020, 07, 07));
        OneTimeTask onTimeTask = (OneTimeTask) test.currentItems().get(0);
        assertEquals("2020-07-07", onTimeTask.getDeadLine());
    }

    @Test
    public void testAddTaskWithDeadlineVerifyDuplicate() {
        test.addTaskWithDeadline("Go to Gym", LocalDate.of(2020, 07, 07));
        test.addTaskWithDeadline("Go to Gym", LocalDate.of(2020, 07, 07));
        OneTimeTask onTimeTask = (OneTimeTask) test.currentItems().get(0);
        assertEquals(1, test.currentItems().size());
    }

    @Test
    public void testAddRecurringTaskVerifyPeriod() {
        test.addRecurringTask("Learn Java", Period.of(0, 0, 5));
        RecurringTask recurringTask = (RecurringTask) test.currentItems().get(0);
        assertEquals("P5D", recurringTask.getPeriod());
    }

    @Test
    public void testAddShoppingListVerifyAmount() {
        test.addShoppingItem("Apple", 4);
        ShoppingList shoppingList = (ShoppingList) test.currentItems().get(0);
        assertEquals(4, shoppingList.getAmount());
    }

    @Test
    public void testAddShoppingListVerifyAmountDuplicate() {
        test.addShoppingItem("Apple", 4);
        test.addShoppingItem("Apple", 3);
        ShoppingList shoppingList = (ShoppingList) test.currentItems().get(0);
        assertEquals(1, test.currentItems().size());
        assertEquals(7, shoppingList.getAmount());
    }


}
