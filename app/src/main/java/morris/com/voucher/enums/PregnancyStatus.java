package morris.com.voucher.enums;

import java.util.EnumMap;

public enum PregnancyStatus {

    YES(0),
    NO(1),
    NA(2);


    private final int code;

    PregnancyStatus(int code) {
        this.code = code;
    }


    public int getCode() {
        return this.code;
    }

    public static PregnancyStatus get(Integer code) {
        switch (code) {
            case 0:
                return YES;
            case 1:
                return NO;
            case 2:
                return NA;
            default:
                throw new IllegalArgumentException("Illegal parameter passed to method :" + code);
        }
    }
    public static PregnancyStatus get(String name) {
        switch (name) {
            case "YES":
                return YES;
            case "NO":
                return NO;
            case "N/A":
                return NA;
            default:
                throw new IllegalArgumentException("Illegal parameter passed to method :" + name);
        }
    }


    public static EnumMap<PregnancyStatus,String> getEnumMap(){

        EnumMap<PregnancyStatus, String> enumMap = new EnumMap<>(PregnancyStatus.class);

        enumMap.put(PregnancyStatus.YES,"YES");
        enumMap.put(PregnancyStatus.NO,"NO");
        enumMap.put(PregnancyStatus.NA,"N/A");

        return enumMap;
    }

}
