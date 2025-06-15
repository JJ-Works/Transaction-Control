import java.time.LocalDate;
import java.time.Month;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        LocalDate local = LocalDate.now();
        System.out.println(local);
        int dayOfMonth = local.getDayOfMonth();
        String month = String.valueOf(local.getMonth());
        int monthValue = local.getMonthValue();
        int year = local.getYear();

        System.out.println(dayOfMonth);
        System.out.println(monthValue);
        System.out.println(month);
        System.out.println(year);
    }
}
