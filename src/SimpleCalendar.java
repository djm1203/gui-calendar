import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**

 COPYRIGHT (C) 2015 Scot Matson. All Rights Reserved.

 Class to launch Calendar application

 Solves CS151 homework assignment #4

 @author Scot Matson

 @version 1.01 2015/11/23

 */
public class SimpleCalendar extends JFrame
{
   /**
    Constructor method for the SimpleCalendar class.
    */
   SimpleCalendar()
   {
      try
      {
         UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      }
      catch (ClassNotFoundException | UnsupportedLookAndFeelException |
         IllegalAccessException | InstantiationException e)
      {
         e.printStackTrace();
      }

      final String FRAME_TITLE = "Simple Calendar";

      setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      setTitle(FRAME_TITLE);

      CalendarModel model = new CalendarModel(new GregorianCalendar(Locale.US));
      loadEvents(model);
      MonthView monthView = new MonthView(model);
      DayView dayView = new DayView(model);
      ActionView actionView = new ActionView(model);

      model.addObserver(monthView);
      model.addObserver(dayView);

      new MonthController(model, monthView);
      new DayController(model, dayView);
      new ActionController(model, actionView);

      add(actionView);
      add(dayView);
      add(monthView);
   }

   /**
    Loads stored events from a plain text file.
    @param model a model load data into
    */
   public void loadEvents(CalendarModel model)
   {
      // Load events from the file
      // and turn them into objects
      FileReader fr = null;
      BufferedReader br = null;
      try
      {
         fr = new FileReader(new File("events.txt"));
         br = new BufferedReader(fr);
         String line;

         // File is stored as 3 line data blocks.
         while ((line = br.readLine()) != null)
         {
            Calendar start = Calendar.getInstance();
            start.setTimeInMillis(Long.parseLong(line));

            line = br.readLine();
            Calendar end = Calendar.getInstance();
            end.setTimeInMillis(Long.parseLong(line));

            line = br.readLine();
            String title = line;

            model.addNewEvent(start, end, title);
         }

         System.out.printf("File has successfully been loaded.");
      }
      catch (FileNotFoundException fnfe)
      {
         System.out.printf("File does not exist.\n");
      }
      catch (IOException ioe)
      {
         ioe.printStackTrace();
      }
      finally
      {
         try
         {
            if (fr != null)
            {
               fr.close();
            }
            if (br != null)
            {
               br.close();
            }
         }
         catch (IOException ioe)
         {
            ioe.printStackTrace();
         }
      }
   }

   /**
    The main method.
    @param args user-defined arguments (Not used).
    */
   public static void main(String[] args)
   {
      SwingUtilities.invokeLater(() -> {
         try
         {
            SimpleCalendar frame = new SimpleCalendar();
            frame.setSize(700, 400);
            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
            frame.setLocation(dim.width / 2 - frame.getSize().width / 2, dim.height / 2 - frame.getSize().height / 2);
            frame.setResizable(false);
            frame.setVisible(true);
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
      });
   }
}