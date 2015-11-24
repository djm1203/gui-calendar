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

 COPYRIGHT (C) 2015 Scot Matson. All Rights Reserved.

 Class to display events for a given day

 Solves CS151 homework assignment #4

 @author Scot Matson

 @version 1.01 2015/11/23

 */
public class DayView extends JPanel implements Observer
{
   private final int EVENTS_WIDTH = 300;
   private CalendarModel calendarModel;
   private JButton previous;
   private JButton next;
   private JLabel date;
   private JLabel day;
   private String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday",
      "Thursday", "Friday", "Saturday"};
   private ArrayList<Event> eventsList;
   private Date selectedDate;
   private JPanel eventsPanel;
   private Color oddEvents;
   private Color evenEvents;

   /**
    Constructor method for DayView
    @param m a model to represent
    */
   public DayView(CalendarModel m)
   {
      calendarModel = m;

      setLayout(null);
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

      final int PANEL_HEIGHT = 348;
      final int BUTTON_WIDTH = 50;
      final int BUTTON_HEIGHT = 25;
      final String PREVIOUS_DAY = "<";
      final String NEXT_DAY = ">";
      final int EVENTS_HEIGHT = 250;
      final int PANEL_WIDTH = 348;

      previous = new JButton(PREVIOUS_DAY);
      previous.setFocusPainted(false);
      next = new JButton(NEXT_DAY);
      next.setFocusPainted(false);
      day = new JLabel(days[calendarModel.getDayOfWeek()-1]);
      String dateText = String.valueOf(calendarModel.getSelectedMonth()+1) + "/" +
         calendarModel.getSelectedDay() + "/" + calendarModel.getSelectedYear();
      date = new JLabel(dateText);
      selectedDate = null;
      eventsPanel = new JPanel();
      eventsPanel.setLayout(new BoxLayout(eventsPanel, BoxLayout.Y_AXIS));
      eventsPanel.setFocusable(false);
      eventsPanel.setBackground(this.getBackground());
      JScrollPane scrollableEventsPanel = new JScrollPane(eventsPanel);
      oddEvents = new Color(170, 255, 147);
      evenEvents = new Color(135, 201, 148);

      // Set bounds.
      setBounds(PANEL_WIDTH, 0, PANEL_WIDTH, PANEL_HEIGHT);
      previous.setBounds((PANEL_WIDTH-previous.getPreferredSize().width)/2-BUTTON_WIDTH , 15, BUTTON_WIDTH, BUTTON_HEIGHT);
      next.setBounds((PANEL_WIDTH-next.getPreferredSize().width)/2+BUTTON_WIDTH, 15, BUTTON_WIDTH, BUTTON_HEIGHT);
      day.setBounds((PANEL_WIDTH-day.getPreferredSize().width)/2-55, 50, 100, 25);
      date.setBounds((PANEL_WIDTH-date.getPreferredSize().width)/2+55, 50, 100, 25);
      scrollableEventsPanel.setBounds((PANEL_WIDTH - EVENTS_WIDTH) / 2, 80, EVENTS_WIDTH, EVENTS_HEIGHT);

      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      try
      {
         selectedDate = sdf.parse(dateText);
      }
      catch (ParseException pe)
      {
         pe.printStackTrace();
      }

      sdf.applyPattern("HH:mm a");
      if (calendarModel.eventsScheduled(selectedDate))
      {
         eventsList = calendarModel.getDayEvents(selectedDate);
         boolean colorSwap = false;
         for (Event e : eventsList)
         {
            String start = sdf.format(e.getStart().getTime());
            String end = sdf.format(e.getEnd().getTime());

            JTextArea eventArea = new JTextArea(
               e.getTitle() + "\n" +
                  "Start: " + start + "\n" +
                  "End: " + end
            );

            if (colorSwap)
            {
               eventArea.setBackground(evenEvents);
            }
            else {
               eventArea.setBackground(oddEvents);
            }
            colorSwap = !colorSwap;

            eventArea.setMaximumSize(new Dimension(EVENTS_WIDTH, eventArea.getPreferredSize().height+10));
            eventArea.setMargin(new Insets(5, 5, 5, 5));
            eventArea.setLineWrap(true);
            eventArea.setFocusable(false);
            eventArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            eventsPanel.add(eventArea);
         }
      }
      else
      {
         JTextArea eventArea = new JTextArea("No events scheduled for today.");
         eventArea.setMaximumSize(new Dimension(EVENTS_WIDTH, eventArea.getPreferredSize().height+10));
         eventArea.setMargin(new Insets(5, 5, 5, 5));
         eventArea.setBackground(oddEvents);
         eventArea.setLineWrap(true);
         eventArea.setFocusable(false);
         eventArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
         eventsPanel.add(eventArea);
      }

      // Add components to this JPanel
      add(previous);
      add(next);
      add(day);
      add(date);
      add(scrollableEventsPanel);
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
      String dateText = String.valueOf(calendarModel.getSelectedMonth() + 1) + "/" +
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
      eventsPanel.removeAll();
      eventsPanel.validate();
      eventsPanel.repaint();
      if (calendarModel.eventsScheduled(selectedDate))
      {
         eventsList = calendarModel.getDayEvents(selectedDate);
         boolean colorSwap = false;
         for (Event e : eventsList)
         {
            String start = sdf.format(e.getStart().getTime());
            String end = sdf.format(e.getEnd().getTime());

            JTextArea eventArea = new JTextArea(
               e.getTitle() + "\n" +
               "Start: " + start + "\n" +
               "End: " + end
            );

            if (colorSwap)
            {
               eventArea.setBackground(evenEvents);
            }
            else {
               eventArea.setBackground(oddEvents);
            }
            colorSwap = !colorSwap;

            eventArea.setMaximumSize(new Dimension(EVENTS_WIDTH, eventArea.getPreferredSize().height+10));
            eventArea.setMargin(new Insets(5, 5, 5, 5));
            eventArea.setLineWrap(true);
            eventArea.setFocusable(false);
            eventArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            eventsPanel.add(eventArea);
         }
      }
      else
      {
         JTextArea eventArea = new JTextArea("No events scheduled for today.");
         eventArea.setMaximumSize(new Dimension(EVENTS_WIDTH, eventArea.getPreferredSize().height+10));
         eventArea.setMargin(new Insets(5, 5, 5, 5));
         eventArea.setBackground(oddEvents);
         eventArea.setLineWrap(true);
         eventArea.setFocusable(false);
         eventArea.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
         eventsPanel.add(eventArea);

      }
      eventsPanel.validate();
      eventsPanel.repaint();
   }
}