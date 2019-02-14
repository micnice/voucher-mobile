package morris.com.voucher.model;

import java.util.Set;

/**
 * Created by morris on 2019/02/13.
 */

public class LoginDetails {

    private String userName;
    private Set<String> roles;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
