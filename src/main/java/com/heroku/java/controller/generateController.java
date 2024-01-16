package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.admission;
import com.heroku.java.model.precord;
import com.heroku.java.model.users;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ModelAttribute;
import javax.sql.DataSource;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Connection;
// import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

// import com.heroku.java.HELPER.PdfGenerator;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.CMYKColor;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
@Controller
public class generateController {
  private final DataSource dataSource;

  @Autowired
  public generateController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

    @PostMapping("/export-to-pdf")
    public void generatePdfFile(HttpServletResponse response)
      throws DocumentException, IOException, SQLException {

    response.setContentType("application/pdf");
    DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
    String currentDateTime = dateFormat.format(new Date());
    String headerkey = "Content-Disposition";
    String headervalue = "attachment; filename=student" + currentDateTime + ".pdf";
    response.setHeader(headerkey, headervalue);

    // List < Items > listofStudents = new ArrayList<>();
    Connection connection = dataSource.getConnection();
    final var statement = connection.createStatement();
    final var resultSet = statement.executeQuery(
                    "SELECT * FROM admission a JOIN patient p ON (a.patientid = p.patientid) ORDER BY a.admissionid;");
    ArrayList<admission> listofAdmissions = new ArrayList<>();
    // Creating the Object of Document
            while (resultSet.next()) {
                int admissionid = resultSet.getInt("admissionid");
                int Pid = resultSet.getInt("patientid");
                int Tid = resultSet.getInt("id");
                String Pname = resultSet.getString("patientname");
                java.sql.Date InDate = resultSet.getDate("admissionindate");
                java.sql.Date OutDate = resultSet.getDate("admissionoutdate");
    
                admission admissons = new admission(admissionid, Pid, Tid, Pname, InDate, OutDate);
                listofAdmissions.add(admissons);
            }

    Document document = new Document(PageSize.A4);
    // Getting instance of PdfWriter
    PdfWriter.getInstance(document, response.getOutputStream());
    // Opening the created document to change it
    document.open();
    // Creating font
    // Setting font style and size
    Font fontTiltle = FontFactory.getFont(FontFactory.COURIER_BOLD);
    fontTiltle.setSize(20);
    // Creating paragraph
    Paragraph paragraph1 = new Paragraph("PMS Record", fontTiltle);
    // Aligning the paragraph in the document
    paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
    // Adding the created paragraph in the document
    document.add(paragraph1);
    // Creating a table of the 4 columns
    PdfPTable table = new PdfPTable(6);
    // Setting width of the table, its columns and spacing
    table.setWidthPercentage(100);
    table.setWidths(new int[] { 2, 2, 2, 2, 2, 2 });
    table.setSpacingBefore(5);
    // Create Table Cells for the table header
    PdfPCell cell = new PdfPCell();
    // Setting the background color and padding of the table cell
    cell.setBackgroundColor(CMYKColor.GREEN);
    cell.setPadding(5);
    // Creating font
    // Setting font style and size
    Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
    font.setColor(CMYKColor.BLACK);
    // Adding headings in the created table cell or header
    // Adding Cell to table
    cell.setPhrase(new Phrase("Admission ID", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Patient ID", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Name", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Therapist ID", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("In date", font));
    table.addCell(cell);
    cell.setPhrase(new Phrase("Out date", font));
    table.addCell(cell);
    // cell.setPhrase(new Phrase("Mobile No", font));
    // table.addCell(cell);
    // Iterating the list of students
    for (admission record : listofAdmissions) {
      // Adding student id
      table.addCell(String.valueOf(record.getAdmissionid()));
      // Adding student name
      table.addCell(String.valueOf(record.getPid()));
      // Adding student name  
      table.addCell(record.getPname());
      // Adding student email
      table.addCell(String.valueOf(record.getTid()));
      // Adding student mobile
      table.addCell(String.valueOf(record.getInDate()));
      // table.addCell(student.getMobileNo());
      table.addCell(String.valueOf(record.getOutDate()));
      // table.addCell(student.getMobileNo());
    }
    // Adding the created table to the document
    document.add(table);
    // Closing the document
    document.close();
    //  this.generate(listofAdmissions, response);
  }

   
    // PdfGenerator generator = new PdfGenerator();

//     public void generate(List<admission> itemsList,
// jakarta.servlet.http.HttpServletResponse response)
// throws DocumentException, IOException {
// // Creating the Object of Document
// Document document = new Document(PageSize.A4);
// // Getting instance of PdfWriter
// PdfWriter.getInstance(document, response.getOutputStream());
// // Opening the created document to change it
// document.open();
// // Creating font
// // Setting font style and size
// Font fontTiltle = FontFactory.getFont(FontFactory.COURIER_BOLD);
// fontTiltle.setSize(20);
// // Creating paragraph
// Paragraph paragraph1 = new Paragraph("VFIM Report", fontTiltle);
// // Aligning the paragraph in the document
// paragraph1.setAlignment(Paragraph.ALIGN_CENTER);
// // Adding the created paragraph in the document
// document.add(paragraph1);
// // Creating a table of the 4 columns
// PdfPTable table = new PdfPTable(4);
// // Setting width of the table, its columns and spacing
// table.setWidthPercentage(100);
// table.setWidths(new int[] { 3, 3, 3, 3 });
// table.setSpacingBefore(5);
// // Create Table Cells for the table header
// PdfPCell cell = new PdfPCell();
// // Setting the background color and padding of the table cell
// cell.setBackgroundColor(CMYKColor.BLUE);
// cell.setPadding(5);
// // Creating font
// // Setting font style and size
// Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN);
// font.setColor(CMYKColor.WHITE);
// // Adding headings in the created table cell or header
// // Adding Cell to table
// cell.setPhrase(new Phrase("ID", font));
// table.addCell(cell);
// cell.setPhrase(new Phrase("Items Name", font));
// table.addCell(cell);
// cell.setPhrase(new Phrase("Quantity", font));
// table.addCell(cell);
// cell.setPhrase(new Phrase("Category", font));
// table.addCell(cell);
// // cell.setPhrase(new Phrase("Mobile No", font));
// // table.addCell(cell);
// // Iterating the list of students
// for (admission items : listofAdmissions) {
// // Adding student id
// table.addCell(String.valueOf(items.getId()));
// // Adding student name
// table.addCell(items.getName());
// // Adding student email
// table.addCell(String.valueOf(items.getQuantity()));
// // Adding student mobile
// table.addCell(String.valueOf(items.getCategory()));
// // table.addCell(student.getMobileNo());
// }
// // Adding the created table to the document
// document.add(table);
// // Closing the document
// document.close();
}
  

