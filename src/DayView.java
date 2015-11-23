import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 Created by scot on 11/21/15.
 */
public class DayView extends JPanel implements Observer
{
   private CalendarModel calendarModel;
   private JButton previous;
   private JButton next;
   private String dateText;
   private JLabel date;
   private JLabel day;
   private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday",
      "Thursday", "Friday", "Saturday"};
   private JTextArea events;
   private JScrollPane scrollableEvents;
   private ArrayList<Event> eventsList;
   private Date selectedDate;


   /**
    Constructor method for DayView
    @param m a model to represent
    */
   public DayView(CalendarModel m)
   {
      calendarModel = m;

      setLayout(null);
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

      final int PANEL_WIDTH = 348;
      final int PANEL_HEIGHT = 348;
      final int BUTTON_WIDTH = 50;
      final int BUTTON_HEIGHT = 25;
      final String PREVIOUS_DAY = "<";
      final String NEXT_DAY = ">";
      final int EVENTS_WIDTH = 300;
      final int EVENTS_HEIGHT = 250;

      previous = new JButton(PREVIOUS_DAY);
      previous.setFocusPainted(false);
      next = new JButton(NEXT_DAY);
      next.setFocusPainted(false);
      day = new JLabel(days[calendarModel.getDayOfWeek()-1]);
      String dateText = String.valueOf(calendarModel.getSelectedMonth()+1) + "/" +
         calendarModel.getSelectedDay() + "/" + calendarModel.getSelectedYear();
      date = new JLabel(dateText);
      events = new JTextArea();
      events.setFocusable(false);
      scrollableEvents = new JScrollPane(events);
      selectedDate = null;

      // Set bounds.
      setBounds(PANEL_WIDTH, 0, PANEL_WIDTH, PANEL_HEIGHT);
      previous.setBounds((PANEL_WIDTH-previous.getPreferredSize().width)/2-BUTTON_WIDTH , 15, BUTTON_WIDTH, BUTTON_HEIGHT);
      next.setBounds((PANEL_WIDTH-next.getPreferredSize().width)/2+BUTTON_WIDTH, 15, BUTTON_WIDTH, BUTTON_HEIGHT);
      day.setBounds((PANEL_WIDTH-day.getPreferredSize().width)/2-55, 50, 100, 25);
      date.setBounds((PANEL_WIDTH-date.getPreferredSize().width)/2+55, 50, 100, 25);
      scrollableEvents.setBounds((PANEL_WIDTH-EVENTS_WIDTH)/2, 80, EVENTS_WIDTH, EVENTS_HEIGHT);

      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      try
      {
         selectedDate = sdf.parse(dateText);
      }
      catch (ParseException pe)
      {
         pe.printStackTrace();
      }
      sdf.applyPattern("HH:mm");
      events.setText("");
      if (calendarModel.eventsScheduled(selectedDate))
      {
         eventsList = calendarModel.getDayEvents(selectedDate);
         for (Event e : eventsList)
         {
            String start = sdf.format(e.getStart().getTime());
            String end = sdf.format(e.getEnd().getTime());
            events.append(e.getTitle() + "\n");
            events.append("Start: " + start + "\n");
            events.append("End: " + end + "\n\n");
         }
      }
      else
      {
         events.append("No events scheduled for today.");
      }

      // Add components to this JPanel
      add(previous);
      add(next);
      add(day);
      add(date);
      add(scrollableEvents);
   }

   /**
    Event listener to view the previous month
    @param listenForButton a button listener
    */
   public void previousButtonListener(ActionListener listenForButton)
   {
      previous.addActionListener(listenForButton);
   }

   /**
    Event listener to view the next month
    @param listenForButton a button listener
    */
   public void nextButtonListener(ActionListener listenForButton)
   {
      next.addActionListener(listenForButton);
   }

   /**
    Updates the view with the current model
    @param o an Observable object
    @param arg an argument passed to notify the notifyObservers method
    */
   @Override
   public void update(Observable o, Object arg)
   {
      if (calendarModel.getCurrentMonth() != calendarModel.getSelectedMonth() ||
         calendarModel.getCurrentYear() != calendarModel.getSelectedYear())
      {
         next.setEnabled(false);
         previous.setEnabled(false);
      }
      else
      {
         next.setEnabled(true);
         previous.setEnabled(true);
      }

      day.setText(days[calendarModel.getDayOfWeek()-1]);
      dateText = String.valueOf(calendarModel.getSelectedMonth()+1) + "/" +
         calendarModel.getSelectedDay() + "/" + calendarModel.getSelectedYear();
      date.setText(dateText);

      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      try
      {
         selectedDate = sdf.parse(dateText);
      }
      catch (ParseException pe)
      {
         pe.printStackTrace();
      }
      sdf.applyPattern("hh:mm a");
      events.setText("");
      if (calendarModel.eventsScheduled(selectedDate))
      {
         eventsList = calendarModel.getDayEvents(selectedDate);
         for (Event e : eventsList)
         {
            String start = sdf.format(e.getStart().getTime());
            String end = sdf.format(e.getEnd().getTime());
            events.append(e.getTitle() + "\n");
            events.append("Start: " + start + "\n");
            events.append("End: " + end + "\n\n");
         }
      }
      else
      {
         events.append("No events scheduled for today.");
      }
   }
}
