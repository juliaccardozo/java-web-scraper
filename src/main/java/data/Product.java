package data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {
    private String url;
    private String image;
    private String name;
    private String price;

    @Override
    public String toString() {
        return "{ \"url\":\"" + url + "\", "
                + " \"image\": \"" + image + "\", "
                + "\"name\":\"" + name + "\", "
                + "\"price\": \"" + price + "\" }";
    }

}
