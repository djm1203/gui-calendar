import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.Observable;
import java.util.Observer;

/**
 Created by scot on 11/21/15.
 */
public class MonthView extends JPanel implements Observer
{
   private final int CALENDAR_ROWS = 6;
   private final int CALENDAR_COLUMNS = 7;

   CalendarModel calendarModel;
   JButton previous;
   JButton next;
   JLabel year;
   JLabel month;
   JTable calendar;
   JScrollPane scrollableCalendar;
   DefaultTableModel calModel;
   //JButton create;
   final String[] months = {"January", "February", "March", "April", "May", "June",
      "July", "August", "September", "October", "November", "December"};

   /**
    Constructor method for MonthView
    @param m a model to represent
    */
   public MonthView(CalendarModel m)
   {
      calendarModel = m;

      setLayout(null);
      setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

      final int PANEL_WIDTH = 348;
      final int PANEL_HEIGHT = 348;
      final int BUTTON_WIDTH = 50;
      final int BUTTON_HEIGHT = 25;
      final int CALENDAR_WIDTH = 300;
      final int CALENDAR_HEIGHT = 250;
      final String PREVIOUS_MONTH = "<";
      final String NEXT_MONTH = ">";
      //final String CREATE_EVENT = "Create";

      previous = new JButton(PREVIOUS_MONTH);
      previous.setFocusPainted(false);
      month = new JLabel(months[calendarModel.getCurrentMonth()]);
      year = new JLabel(String.valueOf(calendarModel.getCurrentYear()));
      next = new JButton(NEXT_MONTH);
      next.setFocusPainted(false);
      //create = new JButton(CREATE_EVENT);
      calModel = new DefaultTableModel()
      {
         public boolean isCellEditable(int rowIndex, int columnIndex)
         {
            return false;
         }
      };
      calendar = new JTable(calModel);
      scrollableCalendar = new JScrollPane(calendar);

      // Set bounds.
      setBounds(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
      previous.setBounds((PANEL_WIDTH-previous.getPreferredSize().width)/2-BUTTON_WIDTH , 15, BUTTON_WIDTH, BUTTON_HEIGHT);
      month.setBounds((PANEL_WIDTH-month.getPreferredSize().width)/2 -30, 50, 100, 25);
      year.setBounds((PANEL_WIDTH-year.getPreferredSize().width)/2 +30, 50, 100, 25);
      next.setBounds((PANEL_WIDTH-next.getPreferredSize().width)/2+BUTTON_WIDTH, 15, BUTTON_WIDTH, BUTTON_HEIGHT);
      //create.setBounds((PANEL_WIDTH - (BUTTON_WIDTH+40))/2, PANEL_HEIGHT-60, BUTTON_WIDTH+40, BUTTON_HEIGHT);
      scrollableCalendar.setBounds((PANEL_WIDTH-CALENDAR_WIDTH)/2, 80, CALENDAR_WIDTH, CALENDAR_HEIGHT);

      previous.getAccessibleContext().setAccessibleName("Previous Month");
      previous.getAccessibleContext().setAccessibleName("Next Month");

      String[] calHeaders = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
      for (int i = 0; i < CALENDAR_COLUMNS; ++i)
      {
         calModel.addColumn(calHeaders[i]);
      }

      // Extend the background of the calendar to blend with the Panel
      setBackground(scrollableCalendar.getBackground());

      // Prevent resizing and reordering of cells.
      calendar.getTableHeader().setResizingAllowed(false);
      calendar.getTableHeader().setReorderingAllowed(false);

      // Allow selection of cells
      calendar.setColumnSelectionAllowed(true);
      calendar.setRowSelectionAllowed(true);
      calendar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

      // Set calendar dimensions
      calendar.setRowHeight(38);
      calModel.setColumnCount(CALENDAR_COLUMNS);
      calModel.setRowCount(CALENDAR_ROWS);

      // Position and display text
      DefaultTableCellRenderer textRender = new DefaultTableCellRenderer();
      textRender.setHorizontalAlignment(SwingConstants.RIGHT);
      textRender.setVerticalAlignment(SwingConstants.TOP);
      for (int i = 0; i < CALENDAR_COLUMNS; ++i)
      {
         calendar.getColumnModel().getColumn(i).setCellRenderer(textRender);
      }

      populateCalendar();

      // Add components to the this JPanel
      add(previous);
      add(month);
      add(year);
      add(next);
      add(scrollableCalendar);
      //add(create);
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
    Event listener for selecting calendar cells
    @param listenForMouse a mouse listener
    */
   public void cellMouseListener(MouseAdapter listenForMouse)
   {
      calendar.addMouseListener(listenForMouse);
   }

   /**
    Clears the calendar of data
    */
   public void clearCalendar()
   {
      for (int i = 0; i < CALENDAR_ROWS; ++i)
      {
         for (int j = 0; j < CALENDAR_COLUMNS; ++j)
         {
            calModel.setValueAt(null, i, j);
         }
      }
      calendar.clearSelection();
   }

   /**
    Populates the calendar with data
    */
   public void populateCalendar()
   {
      int daysInMonth = calendarModel.getDaysInMonth();
      int firstDayOfMonth = calendarModel.getFirstDayOfMonth();

      for (int i = 1; i <= daysInMonth; ++i)
      {
         int row = (i + firstDayOfMonth - 2) / 7;
         int column = (i + firstDayOfMonth - 2) % 7;

         calModel.setValueAt(i, row, column);

         // Highlight the current day.
         if (i == calendarModel.getSelectedDay() &&
            calendarModel.getCurrentMonth() == calendarModel.getSelectedMonth() &&
            calendarModel.getCurrentYear() == calendarModel.getSelectedYear())
         {
            calendar.changeSelection(row, column, false, false);
         }
      }
   }

   /**
    Updates the view with the current model
    @param o an Observable object
    @param arg an argument passed to notify the notifyObservers method
    */
   @Override
   public void update(Observable o, Object arg)
   {
      month.setText(months[calendarModel.getCurrentMonth()]);
      year.setText(String.valueOf(calendarModel.getCurrentYear()));
      clearCalendar();
      populateCalendar();
   }
}