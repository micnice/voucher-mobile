package morris.com.voucher.util;

import morris.com.voucher.model.ClientAssessment;

/**
 * Created by morris on 2019/01/05.
 */

public class CalculationsUtil {

    public static int getPovertyScore(ClientAssessment assessment) {
        int povertyScore=0;
        if(assessment.isPart1()){
            povertyScore = povertyScore+1;

        }
        if(assessment.isPart2()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart3()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart4()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart5()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart6()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart7()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart8()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart9()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart10()){
            povertyScore = povertyScore+1;
        }
        if(assessment.isPart11()){
            povertyScore = povertyScore+1;
        }

        return povertyScore;
    }
}
