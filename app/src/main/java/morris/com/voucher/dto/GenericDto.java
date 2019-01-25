package morris.com.voucher.dto;

/**
 * Created by morris on 2019/01/25.
 */

public class GenericDto {
    String id, name;

    public GenericDto(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
