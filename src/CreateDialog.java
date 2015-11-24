import javax.swing.*;
import javax.swing.text.DefaultFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**

 COPYRIGHT (C) 2015 Scot Matson. All Rights Reserved.

 Overrides the JDialog to create a custom dialog box
 for entering new events

 Solves CS151 homework assignment #4

 @author Scot Matson

 @version 1.01 2015/11/23

 */
public class CreateDialog extends JDialog
{
   private CalendarModel calendarModel;
   private JTextField eventName;
   private JTextField eventDate;
   private JSpinner startTime;
   private JSpinner endTime;

   /**
    Constructor method for CreateDialog
    @param p parent JFrame
    @param m a model
    */
   public CreateDialog(JFrame p, CalendarModel m) {
      super(p, "Create Event", true);
      calendarModel = m;

      // DIALOG PANEL
      final String EVENT_PLACEHOLDER = "Untitled Event";
      final String EVENT_TEXT = "Event Name:";
      final String DATE_TEXT = "Event Date:";
      final String START_TEXT = "Start Time:";
      final String END_TEXT = "End Time:";
      // Event
      JLabel eventLabel = new JLabel(EVENT_TEXT);
      eventName = new JTextField(EVENT_PLACEHOLDER);
      eventName.requestFocusInWindow();
      eventName.selectAll();
      // Date
      JLabel dateLabel = new JLabel(DATE_TEXT);
      String selectedDate = String.valueOf((calendarModel.getSelectedMonth()+1)+"/"+
         calendarModel.getSelectedDay()+"/"+ calendarModel.getSelectedYear());
      eventDate = new JTextField(selectedDate);
      eventDate.setEditable(false);
      eventDate.setHorizontalAlignment((int)JTextField.CENTER_ALIGNMENT);

      // Start
      JLabel startLabel = new JLabel(START_TEXT);
      SpinnerDateModel startModel = new SpinnerDateModel();
      startTime = new JSpinner(startModel);
      startTime.setEditor(new JSpinner.DateEditor(startTime, "hh:mm a"));
      ((DefaultFormatter)((JSpinner.DefaultEditor)startTime.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
      ((DefaultFormatter)((JSpinner.DefaultEditor)startTime.getEditor()).getTextField().getFormatter()).setOverwriteMode(true);

      // End
      JLabel endLabel = new JLabel(END_TEXT);
      SpinnerDateModel endModel = new SpinnerDateModel();
      endTime = new JSpinner(endModel);
      endTime.setEditor(new JSpinner.DateEditor(endTime, "hh:mm a"));
      ((DefaultFormatter)((JSpinner.DefaultEditor)endTime.getEditor()).getTextField().getFormatter()).setAllowsInvalid(false);
      ((DefaultFormatter)((JSpinner.DefaultEditor)endTime.getEditor()).getTextField().getFormatter()).setOverwriteMode(true);

      // Panel
      JPanel dialogPanel = new JPanel();
      GroupLayout layout = new GroupLayout(dialogPanel);
      dialogPanel.setLayout(layout);
      layout.setAutoCreateGaps(true);
      layout.setAutoCreateContainerGaps(true);
      GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
      hGroup.addGroup(layout.createParallelGroup().addComponent(eventLabel).addComponent(dateLabel).addComponent(startLabel).addComponent(startTime));
      hGroup.addGroup(layout.createParallelGroup().addComponent(eventName).addComponent(eventDate).addComponent(endLabel).addComponent(endTime));
      layout.setHorizontalGroup(hGroup);
      GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
      vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(eventLabel).addComponent(eventName));
      vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(dateLabel).addComponent(eventDate));
      vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(startLabel).addComponent(endLabel));
      vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(startTime).addComponent(endTime));
      layout.setVerticalGroup(vGroup);
      layout.linkSize(startTime, endTime);

      dialogPanel.add(eventLabel);
      dialogPanel.add(eventName);
      dialogPanel.add(dateLabel);
      dialogPanel.add(eventDate);
      dialogPanel.add(startLabel);
      dialogPanel.add(startTime);
      dialogPanel.add(endLabel);
      dialogPanel.add(endTime);

      // BUTTON PANEL //
      final String SAVE_BUTTON = "Save";
      final String CANCEL_BUTTON = "Cancel";
      JButton saveButton = new JButton(SAVE_BUTTON);
      saveButton.setFocusPainted(false);
      saveButton.addActionListener(new SaveButtonListener());
      JButton cancelButton = new JButton(CANCEL_BUTTON);
      cancelButton.setFocusPainted(false);
      cancelButton.addActionListener(new CancelButtonListener());
      JPanel buttonPanel = new JPanel();
      buttonPanel.add(saveButton, BorderLayout.LINE_START);
      buttonPanel.add(cancelButton, BorderLayout.LINE_END);

      getContentPane().add(dialogPanel, BorderLayout.PAGE_START);
      getContentPane().add(buttonPanel, BorderLayout.PAGE_END);
      setDefaultCloseOperation(DISPOSE_ON_CLOSE);
      pack();
      setLocationRelativeTo(p);
      setVisible(true);
   }

   /**
    Combines the date and time from two separate
    date objects.
    @param date the date
    @param time the time
    @return a combined Date object
    */
   public Date dateTime(Date date, Date time) {
      return new Date(
         date.getYear(), date.getMonth(), date.getDate(),
         time.getHours(), time.getMinutes(), time.getSeconds()
      );
   }

   /**
    Disposes of the JDialog and saves a new event
    */
   class SaveButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         SimpleDateFormat formatDate = new SimpleDateFormat("MM/dd/yyyy");
         SimpleDateFormat formatTime = new SimpleDateFormat("hh:mm a");

         Date sTime = new Date();
         Date sDate = new Date();
         try
         {
            sDate = formatDate.parse(eventDate.getText());
            sTime = formatTime.parse(formatTime.format(startTime.getValue()));
         }
         catch (ParseException pe)
         {
            pe.printStackTrace();
         }

         Calendar startCal = new GregorianCalendar(Locale.US);
         startCal.setTimeInMillis(dateTime(sDate, sTime).getTime());

         Date eTime = new Date();
         Date eDate = new Date();
         try
         {
            eDate = formatDate.parse(eventDate.getText());
            eTime = formatTime.parse(formatTime.format(endTime.getValue()));
         }
         catch (ParseException pe)
         {
            pe.printStackTrace();
         }

         Calendar endCal = new GregorianCalendar(Locale.US);
         endCal.setTimeInMillis(dateTime(eDate, eTime).getTime());

         if (startCal.before(endCal))
         {
            if (calendarModel.addNewEvent(startCal, endCal, eventName.getText()))
            {
               setVisible(false);
               dispose();
            }
            else
            {
               JOptionPane.showMessageDialog(null, "Warning! Could not save event!\n\n" +
                     "Events must not overlap.",
                  "Error",    JOptionPane.ERROR_MESSAGE);
            }
         }
         else
         {
            JOptionPane.showMessageDialog(null, "Warning! Could not save event!\n\n" +
               "Start times must preceed end times.",
               "Error",    JOptionPane.ERROR_MESSAGE);
         }
      }
   }

   /**
    Disposes of the JDialog without taking action
    */
   class CancelButtonListener implements ActionListener {
      public void actionPerformed(ActionEvent e) {
         setVisible(false);
         dispose();
      }
   }
}