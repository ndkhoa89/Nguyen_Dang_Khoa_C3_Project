import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {
    Restaurant restaurant;

    @BeforeEach
    public void InitTestRestaurant() {
        LocalTime openingTime = LocalTime.parse("10:30:00");
        LocalTime closingTime = LocalTime.parse("22:00:00");
        restaurant = new Restaurant("Amelie's cafe", "Chennai", openingTime, closingTime);
        restaurant.addToMenu("Sweet corn soup", 119);
        restaurant.addToMenu("Vegetable lasagne", 269);
    }

    //>>>>>>>>>>>>>>>>>>>>>>>>>OPEN/CLOSED<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void is_restaurant_open_should_return_true_if_time_is_between_opening_and_closing_time() {
        LocalTime currentLocalTime = LocalTime.of(12, 30, 0);
        try (MockedStatic<LocalTime> topDateTimeUtilMock = Mockito.mockStatic(LocalTime.class)) {
            topDateTimeUtilMock.when(() -> LocalTime.now()).thenReturn(currentLocalTime);
            assertTrue(restaurant.isRestaurantOpen());
        }
    }

    @Test
    public void is_restaurant_open_should_return_false_if_time_is_outside_opening_and_closing_time() {
        LocalTime currentLocalTime = LocalTime.of(7, 45, 30);
        try (MockedStatic<LocalTime> topDateTimeUtilMock = Mockito.mockStatic(LocalTime.class)) {
            topDateTimeUtilMock.when(() -> LocalTime.now()).thenReturn(currentLocalTime);
            assertFalse(restaurant.isRestaurantOpen());
        }
    }

    //<<<<<<<<<<<<<<<<<<<<<<<<<OPEN/CLOSED>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>


    //>>>>>>>>>>>>>>>>>>>>>>>>>>>MENU<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void adding_item_to_menu_should_increase_menu_size_by_1() {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.addToMenu("Sizzling brownie", 319);
        assertEquals(initialMenuSize + 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_from_menu_should_decrease_menu_size_by_1() throws itemNotFoundException {
        int initialMenuSize = restaurant.getMenu().size();
        restaurant.removeFromMenu("Vegetable lasagne");
        assertEquals(initialMenuSize - 1, restaurant.getMenu().size());
    }

    @Test
    public void removing_item_that_does_not_exist_should_throw_exception() {
        assertThrows(itemNotFoundException.class,
                () -> restaurant.removeFromMenu("French fries"));
    }
    //<<<<<<<<<<<<<<<<<<<<<<<MENU>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
    @Test
    public void calculate_order_value_should_return_0_if_no_item_selected() {
        int orderValue = restaurant.calculateOrderValue(new ArrayList<String>());
        assertEquals(orderValue, 0);
    }

    @Test
    public void calculate_order_value_should_return_expected_value_for_1_selected_item() {
        int orderValue = restaurant.calculateOrderValue(new ArrayList<String>() {
            { add("Sweet corn soup"); }
        });
        assertEquals(orderValue, 119);
    }

    @Test
    public void calculate_order_value_should_return_expected_value_for_multiple_selected_items() {
        int orderValue = restaurant.calculateOrderValue(new ArrayList<String>() {
            {
                add("Sweet corn soup");
                add("Vegetable lasagne");
            }
        });
        assertEquals(orderValue, 388);
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>ORDER<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
}