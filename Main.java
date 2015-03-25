//package Marty.company;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        TicketManager ticketManager = new TicketManager();


        LinkedList<Ticket> ticketQueue = new LinkedList<Ticket>();
        LinkedList<Ticket> resolvedTicket = new LinkedList<Ticket>();

        Scanner scanner = new Scanner(System.in);

        addTicketFromFile(ticketQueue);


        while (true) {

            System.out.println("1. Enter Ticket\n" +
                    "2. Delete Ticket by ID\n" +
                    "3. Delete by Issue\n" +
                    "4. Search by Name\n" +
                    "5. Quit");
            int task = Integer.parseInt(scanner.nextLine());

            if (task == 1) {
                //Call addTickets, which will let us enter any number of new tickets
                addTickets(ticketQueue);

            } else if (task == 2) {
                //delete by ticketID
                deleteTicketById(ticketQueue, resolvedTicket);
            } else if (task == 3) {
                //delete by issue
                deleteByIssue(ticketQueue);
            } else if (task == 4) {
                searchByName(ticketQueue);
            } else if (task == 5) {
                //Quit. Future prototype may want to save all tickets to a file
                System.out.println("Quitting program");

                Date todayDate = getTodayDate();
                String dateString = todayDate.toString();
                dateString = dateString.replace(' ', '_');
                dateString = dateString.replace(':', '-');


                FileWriter resolvedWriter = new FileWriter("resolved_ticket_as_of" + dateString + ".txt");
                BufferedWriter resolvedBufWriter = new BufferedWriter(resolvedWriter);

                for (Ticket ticket : resolvedTicket) {
                    resolvedBufWriter.write(ticket.toString());
                }
                resolvedBufWriter.close();

                FileWriter openWriter = new FileWriter("open_tickets.txt");
                BufferedWriter openBufWriter = new BufferedWriter(openWriter);

                for (Ticket ticket : ticketQueue) {
                    //openBufWriter.write(ticket.toString());

                    openBufWriter.write("ID = " + ticket.ticketID);
                    openBufWriter.newLine();
                    openBufWriter.write(" Priority = " + ticket.getPriority());
                    openBufWriter.newLine();
                    openBufWriter.write("Issued = " + ticket.getDescription());
                    openBufWriter.newLine();
                    openBufWriter.write("Reported By = " + ticket.getReporter());
                    openBufWriter.newLine();
                    openBufWriter.write(" Reported on = " + ticket.getDateReported());
                    openBufWriter.newLine();
                }
                openBufWriter.close();

                break;
            } else {
                //Default will be print all tickets
                printAllTickets(ticketQueue);
            }
        }

        scanner.close();
    }

    protected static void printAllTickets(LinkedList<Ticket> tickets) {
        System.out.println("------All Tickets-----");

        for (Ticket t : tickets) {
            System.out.println(t);
        }

        System.out.println(" ------- End of ticket list ----------");
    }

    protected static void addTickets(LinkedList<Ticket> ticketQueue) {
        Scanner scanner = new Scanner(System.in);

        boolean moreProblems = true;
        String description;
        String reporter;
        //let's assume all tickets are created today, for testing. We can change this later if needed
        Date dateReported = new Date(); //Default constructor creates date with current date/time
        int priority;

        while (moreProblems) {
            System.out.println("Enter problem");
            description = scanner.nextLine();
            System.out.println("Who reported this issue?");
            reporter = scanner.nextLine();
            System.out.println("Enter priority of " + description);
            priority = Integer.parseInt(scanner.nextLine());

            Ticket t = new Ticket(priority, reporter, description, dateReported);
            //ticketQueue.add(t);
            addTicketInPriorityOrder(ticketQueue, t);

            //To test, let's print out all of the currently stored tickets
            printAllTickets(ticketQueue);

            System.out.println("More tickets to add?");
            String more = scanner.nextLine();
            if (more.equalsIgnoreCase("N")) {
                moreProblems = false;
            }
        }
    }

    protected static void addTicketInPriorityOrder(LinkedList<Ticket> tickets, Ticket newTicket) {
        if (tickets.size() == 0) {//Special case - if list is empty, add ticket and return
            tickets.add(newTicket);
            return;
        }

        //Tickets with the HIGHEST priority number go at the front of the list. (e.g. 5=server on fire)
        //Tickets with the LOWEST value of their priority number (so the lowest priority) go at the end

        int newTicketPriority = newTicket.getPriority();

        for (int x = 0; x < tickets.size(); x++) {

            //if newTicket is higher or equal priority than the this element, add it in front of this one, and return
            if (newTicketPriority >= tickets.get(x).getPriority()) {
                tickets.add(x, newTicket);
                return;
            }
        }
        tickets.addLast(newTicket);
    }


    protected static void deleteTicketById(LinkedList<Ticket> ticketQueue, LinkedList<Ticket> resolvedTicket) {
        printAllTickets(ticketQueue);

        if (ticketQueue.size() == 0) {
            System.out.println("No tickets to delete!\n");
            return;
        }

        Scanner deleteScanner = new Scanner(System.in);
        System.out.println("Enter ID of ticket to delete");
        int deleteID = getPositiveIntInput();

        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getTicketID() == deleteID) {
                found = true;
                System.out.println("How was this issue resolved?");
                String ticketResolution = deleteScanner.nextLine();
                Date resolutionDate = getTodayDate();

                ticket.setResolution(ticketResolution);
                ticket.setResolutionDate(resolutionDate);

                resolvedTicket.add(ticket);
                ticketQueue.remove(ticket);

                System.out.println(String.format("Ticket %d deleted", deleteID));
                break; //don't need loop any more.
            }
        }
        if (!found) {
            System.out.println("Ticket ID not found, no ticket deleted. Please reenter the ticketID.");
            deleteTicketById(ticketQueue, resolvedTicket);
        }
        printAllTickets(ticketQueue);  //print updated list
    }

    protected static void searchByName(LinkedList<Ticket> ticketQueue) {
        Scanner searchScanner = new Scanner(System.in);
        System.out.println("Enter the key word you'd like to search for.");
        String keyWord = searchScanner.nextLine();

        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getDescription().contains(keyWord)) {
                found = true;
                ticket.toString();
                System.out.println(ticket);
            }
        }
        if (!found) {
            System.out.println("Returned no results.");
        }
    }

    protected static void deleteByIssue(LinkedList<Ticket> ticketQueue) {
        Scanner dss = new Scanner(System.in);
        System.out.println("Enter the keyword of the issue you'd like to delete.");
        String keyWord = dss.nextLine();

        boolean found = false;
        for (Ticket ticket : ticketQueue) {
            if (ticket.getDescription().contains(keyWord)) {
                found = true;
                System.out.println(ticket);
            }
        }
        System.out.println("Enter ID of ticket to delete");
        int deleteID = getPositiveIntInput();
        for (Ticket ticket2 : ticketQueue) {
            if (ticket2.getTicketID() == deleteID) {
                found = true;
                ticketQueue.remove(ticket2);
                System.out.println(String.format("Ticket %d deleted", deleteID));
                break;
            }
        }
    }

    private static void addTicketFromFile(LinkedList<Ticket> ticketQueue) throws IOException {
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
                            ticketQueue.add(ticket);
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



    //Validation method

    private static int getPositiveIntInput() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                String stringInput = scanner.nextLine();
                int intInput = Integer.parseInt(stringInput);
                if (intInput >= 0) {
                    return intInput;
                } else {
                    System.out.println("Please enter a positive number");
                    continue;
                }
            } catch (NumberFormatException ime) {
                System.out.println("Please type a positive number");
                // String dumpRestOfInput = scanner.nextLine();  //Force scanner to throw away the last (invalid) input
            }
        }
    }

    private static Date getTodayDate() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        System.out.println(df.format(date));

        return date;
    }
}



