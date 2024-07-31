package kz.springboot.springbootdemo.db;

import java.util.ArrayList;

public class DBManager {

    private static ArrayList<Items> items = new ArrayList<>();

    static {
        items.add(new Items(1L, "IPhone 12", 400000, 25));
        items.add(new Items(2L, "IPhone 13", 450000, 14));
        items.add(new Items(3L, "IPhone 14", 500000, 62));
        items.add(new Items(4L, "IPhone 15", 550000, 39));
    }

    private static Long id = 5L;

    public static ArrayList<Items> getItems(){
        return items;
    }

    public static void addItem(Items item){
        item.setId(id);
        items.add(item);
        id++;

    }

    public static Items getItem(Long id){
        for (Items it : items){
            if (it.getId() == id)
                return it;
        }
        return null;
    }

}
