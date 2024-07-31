package kz.springboot.springbootdemo.services;

import kz.springboot.springbootdemo.entities.Categories;
import kz.springboot.springbootdemo.entities.Countries;
import kz.springboot.springbootdemo.entities.ShopItems;

import java.util.List;

public interface ItemService {

    ShopItems addItems(ShopItems item);
    List<ShopItems> getAllItems();
    ShopItems getItem(Long id);
    void deleteItem(ShopItems item);
    ShopItems saveItem(ShopItems item);

    List<Countries> getAllCountries();
    Countries addCountry(Countries country);
    Countries saveCountry(Countries country);
    Countries getCountry(Long id);


    List<Categories> getAllCategories();
    Categories getCategory(Long id);
    Categories addCategory(Categories Category);
    Categories saveCategory(Categories Category);

}
