package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.File;
import at.ac.tuwien.sepm.groupphase.backend.entity.Ticket;
import com.itextpdf.text.Anchor;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PdfService {
    private Document globalDocument;
    private ByteArrayOutputStream stream = new ByteArrayOutputStream();
    private ApplicationUser user;


    private static Font bigFont = new Font(Font.FontFamily.TIMES_ROMAN, 18);
    private static Font normalFont = new Font(Font.FontFamily.TIMES_ROMAN, 12);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 10);

    /*
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
        Font.BOLD);
    private static Font smallFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
        Font.BOLD);*/

    public PdfService(ApplicationUser user) {
        try {
            this.user = user;
            globalDocument = new Document();
            // PdfWriter.getInstance(globalDocument, stream);
            PdfWriter.getInstance(globalDocument, new FileOutputStream("C:/temp/test.pdf"));
            globalDocument.open();
            addHeaderData();
            createInvoice(null, 1);
            globalDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        globalDocument.close();
        File returnFile = new File(stream.toByteArray(), File.Type.IMAGE_JPEG);
        return returnFile;
    }

    private void addHeaderData() throws DocumentException {
        //COMPANY ADDRESS
        String addressString = "ticketline GmbH\n";
        addressString += "Karlsplatz 13\n";
        addressString += "1040 Wien\n";
        addressString += "E-Mail: test@mail.com\n";
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

        globalDocument.add(address);
        globalDocument.add(user);
    }

    public void createInvoice(Ticket[] tickets, int previous) throws DocumentException {
        //INVOICE NUMBER
        Paragraph number = new Paragraph("Invoice No " + (previous+ 1), bigFont);
        number.setAlignment(Element.ALIGN_LEFT);

        //DATE
        Paragraph date = new Paragraph("Date of invoice: " + LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.YYYY")), normalFont);
        date.setAlignment(Element.ALIGN_RIGHT);

        //TICKETS
        PdfPTable table = new PdfPTable(4);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell headerCell = new PdfPCell(new Phrase("Description"));
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Details"));
        headerCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(headerCell);

        headerCell = new PdfPCell(new Phrase("Gross Price"));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(headerCell);
        table.setHeaderRows(1);

        headerCell = new PdfPCell(new Phrase("Tax rate"));
        headerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(headerCell);
        table.setHeaderRows(1);

        for (Ticket ticket: tickets) {
            PdfPCell perfCell = new PdfPCell(new Phrase(ticket.getPerformance().getTitle()));
            perfCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell detailCell = new PdfPCell(new Phrase(ticket.getTicketType().getSector().getName() + " " + ticket.getTicketType().getTitle()));
            detailCell.setHorizontalAlignment(Element.ALIGN_LEFT);

            PdfPCell priceCell = new PdfPCell(new Phrase(ticket.getTicketType().getPrice()/100  + "€"));
            priceCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell taxCell = new PdfPCell(new Phrase("13%"));
            taxCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        }


        addEmptyLine(date, 10);

        //DATE DISCLAIMER
        Paragraph dateDis = new Paragraph("Date of invoice is same as date of fulfillment ", smallFont);
        dateDis.setAlignment(Element.ALIGN_LEFT);

        globalDocument.add(number);
        globalDocument.add(date);
        globalDocument.add(dateDis);
    }

    public void createStorno() {

    }

    public void createTicket() {

    }

    /*
    private void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // add a table
        createTable(subCatPart);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);

    }

    private void createTable(Section subCatPart)
        throws BadElementException {
        PdfPTable table = new PdfPTable(3);

        // t.setBorderColor(BaseColor.GRAY);
        // t.setPadding(4);
        // t.setSpacing(4);
        // t.setBorderWidth(1);

        PdfPCell c1 = new PdfPCell(new Phrase("Table Header 1"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 2"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Table Header 3"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        table.addCell("1.0");
        table.addCell("1.1");
        table.addCell("1.2");
        table.addCell("2.1");
        table.addCell("2.2");
        table.addCell("2.3");

        subCatPart.add(table);

    }

    private void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }
    */

    private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}