import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 Created by scot on 11/21/15.
 */
public class MonthController
{
   private CalendarModel model;
   private MonthView view;

   public MonthController(CalendarModel cm, MonthView mv)
   {
      model = cm;
      view = mv;

      view.previousButtonListener(e -> {
         int currentMonth = model.getCurrentMonth();
         int currentYear = model.getCurrentYear();
         if (currentMonth > 0)
         {
            --currentMonth;
         }
         else
         {
            currentMonth = 11;
            --currentYear;
            model.setCurrentYear(currentYear);
         }
         model.setCurrentMonth(currentMonth);
         model.setDaysInMonth(currentMonth, currentYear);
         model.setStartOfMonth(currentMonth, currentYear);
         model.notifyObservers();
      });

      view.nextButtonListener(e -> {
         int currentMonth = model.getCurrentMonth();
         int currentYear = model.getCurrentYear();
         if (currentMonth < 11)
         {
            ++currentMonth;
         }
         else
         {
            currentMonth = 0;
            ++currentYear;
         }
         model.setCurrentMonth(currentMonth);
         model.setCurrentYear(currentYear);
         model.setDaysInMonth(currentMonth, currentYear);
         model.setStartOfMonth(currentMonth, currentYear);
         model.notifyObservers();
      });

      view.cellMouseListener(new MouseAdapter()
      {
         @Override
         public void mouseClicked(final MouseEvent e)
         {
            JTable target = (JTable) e.getSource();
            int row = target.getSelectedRow();
            int column = target.getSelectedColumn();
            int day = (int) target.getValueAt(row, column);
            int month = model.getCurrentMonth();
            int year = model.getCurrentYear();

            model.setCurrentDay(day);
            model.setSelectedDay(day);
            model.setSelectedMonth(month);
            model.setSelectedYear(year);
            model.notifyObservers();
         }
      });
   }
}
