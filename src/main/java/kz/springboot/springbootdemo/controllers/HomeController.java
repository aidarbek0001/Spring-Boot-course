package kz.springboot.springbootdemo.controllers;

import kz.springboot.springbootdemo.entities.Categories;
import kz.springboot.springbootdemo.entities.Countries;
import kz.springboot.springbootdemo.entities.ShopItems;
import kz.springboot.springbootdemo.entities.Users;
import kz.springboot.springbootdemo.services.ItemService;
import kz.springboot.springbootdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

//    @Autowired
//    private TestBean testBean;
//
//    @Autowired
//    private FirstBean firstBean;
//
//    @Autowired
//    private ThirdBean thirdBean;
//
//    @Autowired
//    private TestService testService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @GetMapping(value = "/")
    public String index(Model model) {
        model.addAttribute("currentUser", getUserData());

        List<ShopItems> items = itemService.getAllItems();
        model.addAttribute("tovary", items);

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        return "index";
    }

    @GetMapping(value = "/about")
    public String about(Model model) {
        model.addAttribute("currentUser", getUserData());
        return "about";
    }

    @PostMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String addItem(@RequestParam(name = "country_id", defaultValue = "0") Long id,
                          @RequestParam(name = "item_name", defaultValue = "No Item") String name,
                          @RequestParam(name = "item_price", defaultValue = "0") int price,
                          @RequestParam(name = "item_amount", defaultValue = "0") int amount) {

        Countries cnt = itemService.getCountry(id);
        if (cnt!=null){
            ShopItems item = new ShopItems();
            item.setName(name);
            item.setAmount(amount);
            item.setPrice(price);
            item.setCountry(cnt);
            itemService.addItems(item);
        }

        return "redirect:/";

    }

    @GetMapping(value = "/details/{idshka}")
    public String details(Model model, @PathVariable(name = "idshka") Long id) {
        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);
        model.addAttribute("currentUser", getUserData());

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        model.addAttribute("categories", categories);

        return "details";
    }

    @GetMapping(value = "/edititem/{idshka}")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String edititem(Model model, @PathVariable(name = "idshka") Long id) {
        ShopItems item = itemService.getItem(id);
        model.addAttribute("item", item);
        model.addAttribute("currentUser", getUserData());

        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);

        List<Categories> categories = itemService.getAllCategories();
        categories.removeAll(item.getCategories());
        model.addAttribute("categories", categories);

        return "edititem";
    }

    @PostMapping(value = "/saveitem")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "id", defaultValue = "0") Long id,
                           @RequestParam(name = "country_id", defaultValue = "0") Long countryId,
                           @RequestParam(name = "item_name", defaultValue = "No Item") String name,
                           @RequestParam(name = "item_price", defaultValue = "0") int price,
                           @RequestParam(name = "item_amount", defaultValue = "0") int amount) {
        ShopItems item = itemService.getItem(id);
        if (item!=null){
            Countries cnt = itemService.getCountry(countryId);
            if (cnt!=null) {
                item.setName(name);
                item.setPrice(price);
                item.setAmount(amount);
                item.setCountry(cnt);
                itemService.saveItem(item);
            }

        }
//       return "redirect:/edititem/" + id;
        return "redirect:/";
    }

    @PostMapping(value = "/deleteitem")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String saveItem(@RequestParam(name = "id", defaultValue = "0") Long id) {
        ShopItems item = itemService.getItem(id);
        if (item!=null){
            itemService.deleteItem(item);
        }
        return "redirect:/";
    }

    @PostMapping(value = "/assigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String assignCategory(@RequestParam(name = "item_id") Long itemId,
                                 @RequestParam(name = "category_id") Long categoryId){
        Categories cat = itemService.getCategory(categoryId);
        if (cat!=null){
            ShopItems item = itemService.getItem(itemId);
            if (item!=null){
                List<Categories> categories = item.getCategories();
                if (categories==null){
                    categories = new ArrayList<>();
                }
                categories.add(cat);
                itemService.saveItem(item);
                return "redirect:/edititem/" + itemId + "#CategoriesDiv";
            }
        }
        return "redirect:/";
    }

    @PostMapping(value = "/unassigncategory")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String unAssignCategory(@RequestParam(name = "item_id") Long itemId,
                                 @RequestParam(name = "category_id") Long categoryId){
        Categories cat = itemService.getCategory(categoryId);
        if (cat!=null){
            ShopItems item = itemService.getItem(itemId);
            if (item!=null){
                List<Categories> categories = item.getCategories();
                if (categories==null){
                    categories = new ArrayList<>();
                }
                categories.remove(cat);
                itemService.saveItem(item);
                return "redirect:/edititem/" + itemId + "#CategoriesDiv";
            }
        }
        return "redirect:/";
    }

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser", getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("currentUser", getUserData());
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser", getUserData());
        return "profile";
    }

    @GetMapping(value = "/additem")
    @PreAuthorize("hasAnyRole('ROLE_ADIMN', 'ROLE_MODERATOR')")
    public String addItem(Model model){
        List<Countries> countries = itemService.getAllCountries();
        model.addAttribute("countries", countries);
        model.addAttribute("currentUser", getUserData());
        return "additem";
    }

    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User)authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("currentUser", getUserData());
        return "register";
    }

    @PostMapping(value = "/register")
    public String toRegister(@RequestParam(name = "user_email") String email,
                             @RequestParam(name = "user_password") String password,
                             @RequestParam(name = "re_user_password") String rePassword,
                             @RequestParam(name = "user_full_name") String fullName){

        if (password.equals(rePassword)){
            Users newUser = new Users();
            newUser.setFullName(fullName);
            newUser.setPassword(password);
            newUser.setEmail(email);


            if (userService.creatUser(newUser)!=null){
                return "redirect:/register?success";
            }
        }
        return "redirect:/register?error";
    }
}