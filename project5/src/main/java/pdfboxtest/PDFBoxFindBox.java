/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdfboxtest;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import java.util.*;
import java.io.*;


/**
 *
 * @author Sandeep
 */
public class PDFBoxFindBox {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        PDDocument newDoc = new PDDocument();
        PDDocument document = Loader.loadPDF(new File(args[0]));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for(int i = 0; i < document.getNumberOfPages(); i++) {
            PDPage page = (PDPage)document.getDocumentCatalog().getPages().get(i);
            System.out.println(i + "findCropBox: " + page.getCropBox().getLowerLeftX() + " " + page.getCropBox().getLowerLeftY() + " " + page.getCropBox().getWidth() + " " + page.getCropBox().getHeight());
            System.out.println(i + "findMediaBox: " + page.getMediaBox().getLowerLeftX() + " " + page.getMediaBox().getLowerLeftY() + " " + page.getMediaBox().getWidth() + " " + page.getMediaBox().getHeight());
            System.out.println(i + "getCropBox: " + page.getCropBox().getLowerLeftX() + " " + page.getCropBox().getLowerLeftY() + " " + page.getCropBox().getWidth() + " " + page.getCropBox().getHeight());
            System.out.println(i + "getMediaBox: " + page.getMediaBox().getLowerLeftX() + " " + page.getMediaBox().getLowerLeftY() + " " + page.getMediaBox().getWidth() + " " + page.getMediaBox().getHeight());
            System.out.println(i + "getArtBox: " + page.getArtBox().getLowerLeftX() + " " + page.getArtBox().getLowerLeftY() + " " + page.getArtBox().getWidth() + " " + page.getArtBox().getHeight());
            System.out.println(i + "getBleedBox: " + page.getBleedBox().getLowerLeftX() + " " + page.getBleedBox().getLowerLeftY() + " " + page.getBleedBox().getWidth() + " " + page.getBleedBox().getHeight());
            var image = pdfRenderer.renderImage(i);
            ImageIOUtil.writeImage(image, ("d:\\Test\\20-01-2015\\images\\" + i + ".jpg"), 600);
            page.setCropBox(page.getTrimBox());
            newDoc.addPage(page);
        }
        newDoc.save("d:\\Test\\20-01-2015\\Test.pdf");
    }

}
