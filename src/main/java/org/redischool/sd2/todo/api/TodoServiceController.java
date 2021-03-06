package org.redischool.sd2.todo.api;

import org.redischool.sd2.todo.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides the endpoints allowing the frontend to communicate with the server.
 */
@RestController
final class TodoServiceController {
  private final TodoListService todoListService;

  TodoServiceController(@Autowired TodoListService todoListService) {
    this.todoListService = todoListService;
  }

  @GetMapping("/api/items")
  FetchItemsResponseDto fetchItems() {
    return new FetchItemsResponseDto(currentItems());
  }

  @PostMapping("/api/items")
  AddItemResponseDto addItem(@RequestBody AddItemRequestDto addItemDto) {
    switch (addItemDto.type) {
      case "TASK":
        if (addItemDto.deadline != null && !addItemDto.deadline.isEmpty()) {
          todoListService.addTaskWithDeadline(
              addItemDto.label, LocalDate.parse(addItemDto.deadline, DateTimeFormatter.ISO_DATE));
        } else {
          todoListService.addTask(addItemDto.label);
        }
        break;
      case "RECURRING":
        todoListService.addRecurringTask(
            addItemDto.label, toPeriod(addItemDto.frequency, addItemDto.period));
        break;
      case "SHOPPING_ITEM":
        todoListService.addShoppingItem(addItemDto.label, addItemDto.amount);
        break;
      default:
        throw new HttpClientErrorException(
            HttpStatus.BAD_REQUEST, String.format("Unknown type %s", addItemDto.type));
    }
    return new AddItemResponseDto(currentItems());
  }

  private Period toPeriod(int frequency, String unit) {
    switch (unit) {
      case "DAY":
        return Period.ofDays(frequency);
      case "WEEK":
        return Period.ofWeeks(frequency);
      case "MONTH":
        return Period.ofMonths(frequency);
      case "YEAR":
        return Period.ofYears(frequency);
      default:
        throw new HttpClientErrorException(
            HttpStatus.BAD_REQUEST, String.format("Unknown period unit %s", unit));
    }
  }

  @DeleteMapping("/api/items/{id}")
  DeleteItemResponseDto deleteItem(@PathVariable("id") String id) {
    todoListService.markCompleted(id);
    return new DeleteItemResponseDto(currentItems());
  }

  @PutMapping("/api/items:updateRecurring")
  void updateRecurringTasks() {
    todoListService.updateRecurringTasks();
  }


  private List<ItemDto> currentItems() {
    List<Items> items = todoListService.currentItems();
    List<ItemDto> itemDto = new ArrayList<>();
    for (Items item:items) {
      itemDto.add(convertItemToItemDto(item));
    }
    return itemDto;
  }

  private ItemDto convertItemToItemDto(Items item){
    ItemDto itemDTo = new ItemDto();
    if (item instanceof OneTimeTask){
      if (((OneTimeTask) item).getDeadLine() != null){
        return itemDTo.oneTimeTaskWithLabelAndDeadline(item.getLabel(),((OneTimeTask) item).getDeadLine(),item.getId());
      }

      return itemDTo.oneTimeTaskWithLabel(item.getLabel(),item.getId());
    }

    if (item instanceof RecurringTask){
      return itemDTo.recurringTaskWithLabel(item.getLabel(),item.getId());
    }

    if (item instanceof ShoppingList){
      return itemDTo.shoppingItemWithLabel(((ShoppingList) item).getAmount(),item.getLabel(),item.getId());
    }
    return null;
  }

  //Another try with switch case
  /*private ItemDto convertToItemDTOWithSwitch(Items item) {
    ItemDto itemDto = new ItemDto();
    switch (item.getType()) {
      case ONE_TIME_TASK:
        OneTimeTask oneTimeTask = (OneTimeTask) item;
        if (oneTimeTask.getDeadLine() != null) {
          return itemDto.oneTimeTaskWithLabelAndDeadline(oneTimeTask.getNewItem(), oneTimeTask.getDeadLine());
        }
        return itemDto.oneTimeTaskWithLabel(oneTimeTask.getNewItem());
      case RECURRING_TASK:
        RecurringTask recurringTask =(RecurringTask) item;
        return itemDto.recurringTaskWithLabel(item.getNewItem());
      case SHOPPING_LIST:
        return itemDto.shoppingItemWithLabel(item.getNewItem());
    }
    return null;
  }*/

  private static final class FetchItemsResponseDto {
    private final List<ItemDto> items;

    FetchItemsResponseDto(List<ItemDto> items) {
      this.items = List.copyOf(items);
    }

    public List<ItemDto> getItems() {
      return items;
    }
  }

  private static final class AddItemResponseDto {
    private final List<ItemDto> items;

    AddItemResponseDto(List<ItemDto> items) {
      this.items = List.copyOf(items);
    }

    public List<ItemDto> getItems() {
      return items;
    }
  }

  private static final class DeleteItemResponseDto {
    private final List<ItemDto> items;

    DeleteItemResponseDto(List<ItemDto> items) {
      this.items = List.copyOf(items);
    }

    public List<ItemDto> getItems() {
      return items;
    }
  }

  private static final class AddItemRequestDto {
    String label;
    String type;
    Integer amount;
    Integer frequency;
    String period;
    String deadline;

    public void setLabel(String label) {
      this.label = label;
    }

    public void setType(String type) {
      this.type = type;
    }

    public void setAmount(Integer amount) {
      this.amount = amount;
    }

    public void setFrequency(Integer frequency) {
      this.frequency = frequency;
    }

    public void setPeriod(String period) {
      this.period = period;
    }

    public void setDeadline(String deadline) {
      this.deadline = deadline;
    }
  }
}
