import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;

/**
 * Created by jz2438mu on 3/24/2015.
 */
public class TicketManager extends JFrame {
    private JPanel rootPanel;
    private JTextField problemText;
    private JTextField reportedByText;
    private JComboBox priorityCombo;
    private JButton addTicketButton;
    private JButton clearFieldsButton;
    private JList <Ticket> ticketList;
    private JButton deleteTicketButton;
    private JButton resolveTicketButton;
    private JButton quitButton;

    DefaultListModel<Ticket> ticketListModel;

    final String high = "1: High";
    final String medium = "2: Medium";
    final String low = "3: Low";



    public TicketManager() throws IOException {
        super("Ticket Manager");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(new Dimension(300,500));


        ticketListModel = new DefaultListModel<Ticket>();
        ticketList.setModel(ticketListModel);
        ticketList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        priorityCombo.addItem(high);
        priorityCombo.addItem(medium);
        priorityCombo.addItem(low);


        Date todayDate= getTodayDate();
        String dateString = todayDate.toString();
        dateString = dateString.replace(' ', '_');
        dateString = dateString.replace(':', '-');

        addTicketFromFile(ticketListModel); //add tickets from open_tickets file to the List display in the UI

        FileWriter openWriter = new FileWriter ("open_tickets.txt");
        final BufferedWriter openBufWriter = new BufferedWriter(openWriter);    //create file for tickets without resolutions

        FileWriter resolvedWriter = new FileWriter("resolved_ticket_as_of" + dateString + ".txt");
        final BufferedWriter resolvedBufWriter = new BufferedWriter(resolvedWriter); //create file for resolved tickets



        addTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO PRIORITIZE TICKETS BY PRIORITY

                String description = problemText.getText();
                String reporter = reportedByText.getText();
                int priority = 0;

                if (priorityCombo.getSelectedItem().equals(high)){
                    priority = 1;

                } else if (priorityCombo.getSelectedItem().equals(medium)){
                    priority = 2;
                } else {
                    priority = 3;
                }

                Date dateReported = new Date();

                Ticket t = new Ticket(priority, reporter, description, dateReported);

                addTicketToFile(openBufWriter, t);  //add the ticket to the open_tickets file
                TicketManager.this.ticketListModel.addElement(t);  //add the ticket to the UI List display
            }
        });

        clearFieldsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                problemText.setText("");
                reportedByText.setText("");
            }
        });

        deleteTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket toDelete = TicketManager.this.ticketList.getSelectedValue();
                TicketManager.this.ticketListModel.removeElement(toDelete);
            }
        });

        resolveTicketButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Ticket resolvedTicket = TicketManager.this.ticketList.getSelectedValue();
                String resolution = JOptionPane.showInputDialog("How was this issue resolved?");
                Date resolvedDate = new Date();

                resolvedTicket.setResolution(resolution);
                resolvedTicket.setResolutionDate(resolvedDate);

                try {
                    addResolvedTicketToFile(resolvedTicket, resolvedBufWriter);
                } catch(IOException io) {
                    io.printStackTrace();
                }

                TicketManager.this.ticketListModel.removeElement(resolvedTicket);

            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeReaders(resolvedBufWriter, openBufWriter);  //close both the files for editing
                System.exit(0);
            }
        });

    }

    private static Date getTodayDate() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(df.format(date));

        return date;
    }

    private void addTicketFromFile(DefaultListModel<Ticket> ticketListModel) throws IOException {
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader("open_tickets.txt"));
            String line;
            ArrayList<String> words = new ArrayList<String>();


            while ((line = bufReader.readLine()) != null) {
                String[] split = line.split("= ");
                for (int i = 0; i < split.length; i++) {
                    words.add(split[i]);
                    if (split[i].endsWith("2015")) {
                        String arrayPriority = words.get(3);
                        String description = words.get(5);
                        String reportedBy = words.get(7);
                        String arrayDate = words.get(9);


                        int priority = Integer.valueOf(arrayPriority);
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
                        try {
                            Date date = formatter.parse(arrayDate);
                            Ticket ticket = new Ticket(priority, reportedBy, description, date);
                            TicketManager.this.ticketListModel.addElement(ticket);
                            words.clear();  //empty the arrayList to make room for next set

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return;
        }
        catch (FileNotFoundException ffe){
            System.out.println(ffe);
            System.out.println("There are no Open Ticket files in the provided directory.");
            return;
        }
    }

    private void addResolvedTicketToFile(Ticket resolvedTicket, BufferedWriter resolvedBufWriter) throws IOException{

        resolvedBufWriter.write(resolvedTicket.toString());
    }

    private void addTicketToFile(BufferedWriter openBufWriter, Ticket ticket){
        try {
            openBufWriter.write("ID = " + ticket.getTicketID());
            openBufWriter.newLine();
            openBufWriter.write(" Priority = " + ticket.getPriority());
            openBufWriter.newLine();
            openBufWriter.write("Issued = " + ticket.getDescription());
            openBufWriter.newLine();
            openBufWriter.write("Reported By = " + ticket.getReporter());
            openBufWriter.newLine();
            openBufWriter.write(" Reported on = " + ticket.getDateReported());
            openBufWriter.newLine();

        } catch (IOException io){
            io.printStackTrace();
        }

    }

    private void closeReaders(BufferedWriter resolvedBufWriter, BufferedWriter openBufWriter){
        try {
            openBufWriter.close();
            resolvedBufWriter.close();
        } catch (IOException io){
            io.printStackTrace();
        }

    }


}
