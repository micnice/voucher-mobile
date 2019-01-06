package morris.com.voucher.enums;

import java.util.EnumMap;

public enum EducationStatus {

    OLEVEL(0),
    ALEVEL(1),
    NATIONAL_CERTIFICATE(2),
    NATIONAL_DIPLOMA(3),
    HIGHER_NATIONAL_DIPLOMA(4),
    DEGREE(5),
    MASTERS(6),
    PHD(7);


    private final int code;

    EducationStatus(int code) {
        this.code = code;
    }


    public int getCode() {
        return this.code;
    }

    public static EducationStatus get(Integer code) {
        switch (code) {
            case 0:
                return OLEVEL;
            case 1:
                return ALEVEL;
            case 2:
                return NATIONAL_CERTIFICATE;
            case 3:
                return NATIONAL_DIPLOMA;
            case 4:
                return HIGHER_NATIONAL_DIPLOMA;
            case 5:
                return DEGREE;
            case 6:
                return MASTERS;
            case 7:
                return PHD;
            default:
                throw new IllegalArgumentException("Illegal parameter passed to method :" + code);
        }
    }
    public static EducationStatus get(String name) {
        switch (name) {
            case "O Level":
                return OLEVEL;
            case "A Level":
                return ALEVEL;
            case "National Certificate":
                return NATIONAL_CERTIFICATE;
            case "National Diploma":
                return NATIONAL_DIPLOMA;
            case "Higher National Diploma":
                return HIGHER_NATIONAL_DIPLOMA;
            case "Degree":
                return DEGREE;
            case "Masters":
                return DEGREE;
            case "PHD":
                return PHD;
            default:
                throw new IllegalArgumentException("Illegal parameter passed to method :" + name);
        }
    }

    public static EnumMap<EducationStatus,String> getEnumMap(){

        EnumMap<EducationStatus, String> enumMap = new EnumMap<>(EducationStatus.class);

        enumMap.put(EducationStatus.OLEVEL,"O Level");
        enumMap.put(EducationStatus.ALEVEL,"A Level");
        enumMap.put(EducationStatus.NATIONAL_CERTIFICATE,"National Certificate");
        enumMap.put(EducationStatus.NATIONAL_DIPLOMA,"National Diploma");
        enumMap.put(EducationStatus.HIGHER_NATIONAL_DIPLOMA,"Higher National Diploma");
        enumMap.put(EducationStatus.DEGREE,"Degree");
        enumMap.put(EducationStatus.MASTERS,"Masters");
        enumMap.put(EducationStatus.PHD,"PHD");

        return enumMap;
    }

}
