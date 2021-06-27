package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.Address;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.Performance;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.exception.PdfException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.lang.invoke.MethodHandles;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class PdfService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private Document globalDocument;
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    private ApplicationUser user;


    private static Font bigFont = new Font(Font.FontFamily.TIMES_ROMAN, 18);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);


    public PdfService(ApplicationUser user) {
        try {
            this.user = user;
            globalDocument = new Document();
            PdfWriter.getInstance(globalDocument, stream);
            globalDocument.open();
            addHeaderData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        LOGGER.trace("getFile()");
        globalDocument.close();
        File returnFile = new File(stream.toByteArray(), File.Type.APPLICATION_PDF);
        return returnFile;
    }

    private void addHeaderData() {
        LOGGER.trace("addHeaderData()");
        //COMPANY ADDRESS
        String addressString = "ticketline GmbH\n";
        addressString += "Karlsplatz 13\n";
        addressString += "1040 Wien\n";
        addressString += "E-Mail: sepm.ticketline@gmail.com\n";
        addressString += "Website: localhost:4200\n";
        addressString += "UID: ATU00000001";

        Paragraph address = new Paragraph(addressString, normalFont);
        address.setAlignment(Element.ALIGN_RIGHT);

        //USER ADDRESS
        String userString = this.user.getFirstName() + " " + this.user.getLastName() + "\n";
        userString += this.user.getAddress().getLineOne() + "\n";
        userString += this.user.getAddress().getPostcode() + " " + this.user.getAddress().getCity() + "\n";
        userString += this.user.getAddress().getCountry();

        Paragraph user = new Paragraph(userString, normalFont);
        addEmptyLine(user, 1);

        try {
            globalDocument.add(address);
            globalDocument.add(user);
        } catch (Exception e) {
            throw new PdfException("Pdf Header creation failed");
        }
    }

    public void createInvoice(Set<Ticket> tickets, int previous) {
        LOGGER.trace("createInvoice({}, {})", tickets, previous);
        //INVOICE NUMBER
        Paragraph number = new Paragraph("Invoice No " + (previous + 1), bigFont);
        number.setAlignment(Element.ALIGN_LEFT);

        //DATE
        Paragraph date = new Paragraph("Date of invoice: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")), normalFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        addEmptyLine(date, 1);

        //TICKETS
        PdfPTable table = new PdfPTable(4);

        PdfPCell headerCell = new PdfPCell(new Phrase("Description"));
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Details"));
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Gross Price"));
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(headerCell);
        table.setHeaderRows(1);

        headerCell = new PdfPCell(new Phrase("Tax rate"));
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(headerCell);
        table.setHeaderRows(1);

        Long totalGrossPrice = 0L;

        for (Ticket ticket : tickets) {
            PdfPCell perfCell = new PdfPCell(new Phrase(ticket.getPerformance().getTitle()));
            perfCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell detailCell = new PdfPCell(new Phrase(ticket.getTicketType().getSector().getName() + " " + ticket.getTicketType().getTitle()));
            detailCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            totalGrossPrice += ticket.getTicketType().getPrice();
            PdfPCell priceCell = new PdfPCell(new Phrase(ticket.getTicketType().getPrice() / 100  + "€"));
            priceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            PdfPCell taxCell = new PdfPCell(new Phrase("13%"));
            taxCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

            table.addCell(perfCell);
            table.addCell(detailCell);
            table.addCell(priceCell);
            table.addCell(taxCell);
        }

        PdfPCell emptyCell = new PdfPCell(new Phrase(" "));
        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(new PdfPCell(new Phrase("Total Gross Price")));
        table.addCell(new PdfPCell(new Phrase(totalGrossPrice / 100 + "€")));

        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(new PdfPCell(new Phrase("Total Gross Price")));
        table.addCell(new PdfPCell(new Phrase(totalGrossPrice * 0.87 / 100 + "€")));

        table.addCell(emptyCell);
        table.addCell(emptyCell);
        table.addCell(new PdfPCell(new Phrase("Tax")));
        table.addCell(new PdfPCell(new Phrase(totalGrossPrice * 0.13 / 100 + "€")));

        //DATE DISCLAIMER
        Paragraph dateDis = new Paragraph("Date of invoice is same as date of fulfillment ", smallFont);
        dateDis.setAlignment(Element.ALIGN_LEFT);

        try {
            globalDocument.add(number);
            globalDocument.add(date);
            globalDocument.add(table);
            globalDocument.add(dateDis);
        } catch (Exception e) {
            throw new PdfException("Pdf invoice creation failed");
        }
    }

    public void createStorno(Set<Ticket> tickets, int previous) {
        LOGGER.trace("createStorno({}, {})", tickets, previous);
        //INVOICE NUMBER
        Paragraph number = new Paragraph("Storno No " + (previous + 1), bigFont);
        number.setAlignment(Element.ALIGN_LEFT);

        //DATE
        Paragraph date = new Paragraph("Date of storno: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")), normalFont);
        date.setAlignment(Element.ALIGN_RIGHT);
        addEmptyLine(date, 1);

        //Text
        Long totalGrossPrice = 0L;

        for (Ticket ticket : tickets) {
            totalGrossPrice += ticket.getTicketType().getPrice();
        }

        String textString = "Your order has been cancelled. " + totalGrossPrice / 100 + " € will be send back to your account";
        Paragraph text = new Paragraph(textString, normalFont);

        try {
            globalDocument.add(number);
            globalDocument.add(date);
            globalDocument.add(text);
        } catch (Exception e) {
            throw new PdfException("Pdf storno creation failed");
        }
    }

    public void createTicket(List<Ticket> tickets) {
        LOGGER.trace("createTicket({})", tickets);
        if (tickets.size() != 0) {
            //PERFORMANCE
            Performance perf = tickets.get(0).getPerformance();
            Address address = perf.getVenue().getAddress();
            String perfString = "\n\n\n" + perf.getTitle() + "\n";
            perfString += perf.getVenue().getName() + "\n";
            perfString += address.getLineOne() + " " + address.getCity() + "\n";
            perfString += "Start: " + perf.getDate().format(DateTimeFormatter.ofPattern("dd.MM.YYYY hh:mm")) + "\n";

            Paragraph performance = new Paragraph(perfString, normalFont);
            addEmptyLine(performance, 2);


            //TICKETS
            PdfPTable table = new PdfPTable(2);

            for (Ticket ticket : tickets) {
                PdfPCell sectorCell = new PdfPCell(new Phrase(ticket.getTicketType().getSector().getName() + " " + ticket.getTicketType().getTitle()));
                sectorCell.setHorizontalAlignment(Element.ALIGN_LEFT);

                PdfPCell seatCell = new PdfPCell(new Phrase("Seat " + ticket.getSeat().getCustomLabel()));
                seatCell.setHorizontalAlignment(Element.ALIGN_RIGHT);

                table.addCell(sectorCell);
                table.addCell(seatCell);
            }

            //QR CODE
            Paragraph qr = new Paragraph("\n\n\nLink to Confirmation: http://localhost:4200/confirmation/" + user.getId() + "/" + perf.getId());

            try {
                globalDocument.add(performance);
                globalDocument.add(table);
                globalDocument.add(qr);
            } catch (Exception e) {
                throw new PdfException("Pdf ticket creation failed");
            }
        }

    }

    private void addEmptyLine(Paragraph paragraph, int number) {
        LOGGER.trace("addEmptyLine({}, {})", paragraph, number);
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}