/**
 Created by scot on 11/21/15.
 */
public class DayController
{
   private CalendarModel model;
   private DayView view;

   public DayController(CalendarModel cm, DayView dv)
   {
      model = cm;
      view = dv;

      view.previousButtonListener(e-> {
         int selectedDay = model.getSelectedDay();
         int selectedMonth = model.getSelectedMonth();
         int selectedYear = model.getSelectedYear();
         if (selectedDay > 1)
         {
            --selectedDay;
         }
         else
         {
            if (selectedMonth > 0)
            {
               --selectedMonth;
            }
            else
            {
               selectedMonth = 11;
               --selectedYear;
               model.setCurrentYear(selectedYear);
               model.setSelectedYear(selectedYear);
            }
            model.setCurrentMonth(selectedMonth);
            model.setSelectedMonth(selectedMonth);
            model.setDaysInMonth(selectedMonth, selectedYear);
            model.setStartOfMonth(selectedMonth, selectedYear);

            selectedDay = model.getDaysInMonth();
         }
         model.setCurrentDay(selectedDay);
         model.setSelectedDay(selectedDay);
         model.notifyObservers();
      });

      view.nextButtonListener(e-> {
         int selectedDay = model.getSelectedDay();
         int selectedMonth = model.getSelectedMonth();
         int selectedYear = model.getSelectedYear();
         int daysInMonth = model.getDaysInMonth();

         if (selectedDay < daysInMonth)
         {
            ++selectedDay;
         }
         else
         {
            if (selectedMonth < 11)
            {
               ++selectedMonth;
            }
            else
            {
               selectedMonth = 0;
               ++selectedYear;
            }
            model.setCurrentMonth(selectedMonth);
            model.setSelectedMonth(selectedMonth);
            model.setCurrentYear(selectedYear);
            model.setSelectedYear(selectedYear);
            model.setDaysInMonth(selectedMonth, selectedYear);
            model.setStartOfMonth(selectedMonth, selectedYear);
            selectedDay = 1;
         }
         model.setCurrentDay(selectedDay);
         model.setSelectedDay(selectedDay);
         model.notifyObservers();
      });
   }
}
