import java.util.ArrayList;

interface Mobile {
    void logBrand();
}

abstract class AndroidMobile implements Mobile {
    public void logBrand() {
        switch(getBrandCode()) {
        case 0x34:
            System.out.println("Samsung");
            break;
        case 0xDEADBEEF:
            System.out.println("Nokia");
            break;
        default:
            System.out.println("Generic");
            break;
        }
    }

    protected abstract int getBrandCode();
}

class SamsungMobile extends AndroidMobile {
    protected int getBrandCode() {
        return 0x34;
    }
}

class NokiaMobile extends AndroidMobile {
    protected int getBrandCode() {
        return 0xDEADBEEF;
    }
}

public class Main {
    public static void main(String[] args) {
        ArrayList<Mobile> mobiles = new ArrayList<>();
        mobiles.add(new SamsungMobile());
        mobiles.add(new NokiaMobile());
        for (Mobile mobile : mobiles) {
            mobile.logBrand();
        }
    }
}
