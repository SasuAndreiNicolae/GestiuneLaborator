package Utils;

import com.itextpdf.text.*;

import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.FileOutputStream;

public class MyPdfWriter
{
    private Document document;
    private PdfWriter writer;
    private PdfPTable table;
    private String fileName;

    /*
        initialize my pdf writer to write document with given filename
     */
    public MyPdfWriter(String fileName)
    {
        this.fileName=fileName;
        document= new Document();
        try
        {
            writer =  PdfWriter.getInstance(document,new FileOutputStream(fileName));
            document.open();
        }
        catch (Exception e)
        {
        }
    }
    /*
        adds a title to pdf
     */
    public void addTitle(String title)
    {
        Paragraph t = new Paragraph();


        Font f = new Font();
        f.setStyle(Font.ITALIC);
        f.setSize(22);
        f.setColor(BaseColor.BLUE);
        t.setFont(f);

        t.add(title);

        t.setAlignment(Element.ALIGN_CENTER);
        try
        {
            document.add(t);
            document.add(Chunk.NEWLINE);
        }
        catch (Exception e )
        {

        }
    }
    /*
        adds simple text to pdf
     */
    public void addText(String text)
    {
        try
        {
            document.add(Chunk.NEWLINE);
            Phrase ph = new Phrase(text);
            document.add(ph);
            document.add(Chunk.NEWLINE);
        }
        catch (Exception e )
        {
        }
    }
    /*
        creates the table
     */
    public void createTable(int nrRows)
    {
        table = new PdfPTable(nrRows);
        table.setWidthPercentage(97);
    }
    /*
        add table header
     */
    public void addTableHeader(String[] headers)
    {
        if (table==null)
            return;
        for (String h :headers)
        {
            PdfPCell head= new PdfPCell(new Phrase(h));
            head.setBorderWidth(3);
            head.setBackgroundColor(BaseColor.LIGHT_GRAY);
            head.setPadding(2);
            table.addCell(head);
        }
    }
    /*
         adds row to table
     */
    public void addRow(String[] row)
    {
        if(table==null)
            return;
        for (String s :row)
        {
            PdfPCell cell= new PdfPCell(new Phrase(s));
            cell.setPadding(2);
            table.addCell(cell);
        }
    }
    /*
        writes table to pdf document
     */
    public void addTable()
    {
        try
        {
            document.add(table);
        }
        catch (Exception e )
        {
        }
    }
    /*
        closes the pdf document to be ready to use
     */
    public void close()
    {
        //writer.close();
        document.close();
    }

    /*
        adds image to document
     */
    public void addImage(String imgPath)
    {
        try
        {
            com.itextpdf.text.Image i = com.itextpdf.text.Image.getInstance(imgPath);
            PdfPTable ta= new PdfPTable(1);
            ta.setWidthPercentage(45);
            ta.addCell(new PdfPCell(i,true));
            document.add(Chunk.NEWLINE);
            document.add(ta);
            document.add(Chunk.NEWLINE);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }


}
