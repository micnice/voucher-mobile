package morris.com.voucher.enums;

import java.util.EnumMap;

public enum MaritalStatus {

    MARRIED(0),
    SINGLE(1),
    DIVORCED(2),
    WIDOWED(3);

    private final int code;

    MaritalStatus(int code) {
        this.code = code;
    }


    public int getCode() {
        return this.code;
    }

    public static MaritalStatus get(Integer code) {
        switch (code) {
            case 0:
                return MARRIED;
            case 1:
                return SINGLE;
            case 2:
                return DIVORCED;
            case 3:
                return WIDOWED;
            default:
                throw new IllegalArgumentException("Illegal parameter passed to method :" + code);
        }
    }

    public static EnumMap<MaritalStatus,String> getEnumMap(){

        EnumMap<MaritalStatus, String> enumMap = new EnumMap<>(MaritalStatus.class);

        enumMap.put(MaritalStatus.MARRIED,"Married");
        enumMap.put(MaritalStatus.SINGLE,"Single");
        enumMap.put(MaritalStatus.DIVORCED,"Divorced");
        enumMap.put(MaritalStatus.WIDOWED,"Widowed");

        return enumMap;
    }

}

