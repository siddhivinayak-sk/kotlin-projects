/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pdfboxtest;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Sandeep
 */
public class PDFBoxTest {

    /**
     * Merge multiple PDF files into a single PDF
     * @param inputPaths Array of absolute paths to PDF files to merge
     * @param outputPath Absolute path for the merged output PDF
     */
    public static void mergePDFs(String[] inputPaths, String outputPath) throws IOException {
        PDFMergerUtility merger = new PDFMergerUtility();
        for (String path : inputPaths) {
            merger.addSource(new File(path));
        }
        merger.setDestinationFileName(outputPath);
        merger.mergeDocuments(null);
        System.out.println("PDFs merged successfully to: " + outputPath);
    }

    /**
     * Split a PDF into individual single-page PDFs
     * @param inputPath Absolute path to the PDF file to split
     * @param outputDir Absolute path to the directory where split PDFs will be saved
     */
    public static void splitPDF(String inputPath, String outputDir) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));
        Splitter splitter = new Splitter();
        List<PDDocument> pages = splitter.split(document);

        int pageNum = 1;
        for (PDDocument page : pages) {
            String outputPath = outputDir + File.separator + "page_" + pageNum++ + ".pdf";
            page.save(outputPath);
            page.close();
        }
        document.close();
        System.out.println("PDF split into " + (pageNum - 1) + " pages in: " + outputDir);
    }

    /**
     * Split a PDF into chunks of specified page count
     * @param inputPath Absolute path to the PDF file to split
     * @param outputDir Absolute path to the directory where split PDFs will be saved
     * @param splitSize Number of pages per output PDF
     */
    public static void splitPDFByPageCount(String inputPath, String outputDir, int splitSize) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));
        Splitter splitter = new Splitter();
        splitter.setSplitAtPage(splitSize);
        List<PDDocument> splitDocs = splitter.split(document);

        int docNum = 1;
        for (PDDocument doc : splitDocs) {
            String outputPath = outputDir + File.separator + "split_" + docNum++ + ".pdf";
            doc.save(outputPath);
            doc.close();
        }
        document.close();
        System.out.println("PDF split into " + (docNum - 1) + " documents in: " + outputDir);
    }

    /**
     * Extract a range of pages from a PDF
     * @param inputPath Absolute path to the source PDF file
     * @param outputPath Absolute path for the output PDF
     * @param fromPage Starting page number (1-based)
     * @param toPage Ending page number (1-based, inclusive)
     */
    public static void extractPages(String inputPath, String outputPath, int fromPage, int toPage) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));
        PDDocument newDoc = new PDDocument();

        for (int i = fromPage - 1; i < toPage && i < document.getNumberOfPages(); i++) {
            newDoc.addPage(document.getPage(i));
        }

        newDoc.save(outputPath);
        newDoc.close();
        document.close();
        System.out.println("Extracted pages " + fromPage + " to " + toPage + " to: " + outputPath);
    }

    /**
     * Convert PDF pages to images
     * @param inputPath Absolute path to the PDF file
     * @param outputDir Absolute path to the directory where images will be saved
     * @param dpi Resolution for the images (e.g., 300)
     * @param imageFormat Image format (e.g., "jpg", "png")
     */
    public static void convertToImages(String inputPath, String outputDir, int dpi, String imageFormat) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));
        PDFRenderer pdfRenderer = new PDFRenderer(document);

        for (int pageIndex = 0; pageIndex < document.getNumberOfPages(); pageIndex++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, dpi, ImageType.RGB);
            String outputPath = outputDir + File.separator + "page_" + (pageIndex + 1) + "." + imageFormat;
            ImageIOUtil.writeImage(image, outputPath, dpi);
        }

        document.close();
        System.out.println("Converted " + document.getNumberOfPages() + " pages to images in: " + outputDir);
    }

    /**
     * Extract text from a PDF
     * @param inputPath Absolute path to the PDF file
     * @param outputPath Absolute path for the output text file
     */
    public static void extractText(String inputPath, String outputPath) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);

        java.nio.file.Files.write(new File(outputPath).toPath(), text.getBytes());
        document.close();
        System.out.println("Text extracted to: " + outputPath);
    }

    /**
     * Extract text from specific pages of a PDF
     * @param inputPath Absolute path to the PDF file
     * @param outputPath Absolute path for the output text file
     * @param fromPage Starting page number (1-based)
     * @param toPage Ending page number (1-based, inclusive)
     */
    public static void extractTextFromPages(String inputPath, String outputPath, int fromPage, int toPage) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));
        PDFTextStripper stripper = new PDFTextStripper();
        stripper.setStartPage(fromPage);
        stripper.setEndPage(toPage);
        String text = stripper.getText(document);

        java.nio.file.Files.write(new File(outputPath).toPath(), text.getBytes());
        document.close();
        System.out.println("Text extracted from pages " + fromPage + " to " + toPage + " to: " + outputPath);
    }

    /**
     * Encrypt a PDF with password protection
     * @param inputPath Absolute path to the PDF file to encrypt
     * @param outputPath Absolute path for the encrypted output PDF
     * @param userPassword Password required to open the PDF
     * @param ownerPassword Password required to change permissions
     */
    public static void encryptPDF(String inputPath, String outputPath, String userPassword, String ownerPassword) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));

        AccessPermission accessPermission = new AccessPermission();
        StandardProtectionPolicy spp = new StandardProtectionPolicy(ownerPassword, userPassword, accessPermission);
        spp.setEncryptionKeyLength(128);
        spp.setPermissions(accessPermission);

        document.protect(spp);
        document.save(outputPath);
        document.close();
        System.out.println("PDF encrypted and saved to: " + outputPath);
    }

    /**
     * Decrypt a password-protected PDF
     * @param inputPath Absolute path to the encrypted PDF file
     * @param outputPath Absolute path for the decrypted output PDF
     * @param password Password to decrypt the PDF
     */
    public static void decryptPDF(String inputPath, String outputPath, String password) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath), password);

        document.setAllSecurityToBeRemoved(true);
        document.save(outputPath);
        document.close();
        System.out.println("PDF decrypted and saved to: " + outputPath);
    }

    /**
     * Remove specific pages from a PDF
     * @param inputPath Absolute path to the PDF file
     * @param outputPath Absolute path for the output PDF
     * @param pageNumbers Array of page numbers to remove (1-based)
     */
    public static void removePages(String inputPath, String outputPath, int[] pageNumbers) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));

        // Sort in descending order to avoid index shifting issues
        java.util.Arrays.sort(pageNumbers);
        for (int i = pageNumbers.length - 1; i >= 0; i--) {
            int pageIndex = pageNumbers[i] - 1;
            if (pageIndex >= 0 && pageIndex < document.getNumberOfPages()) {
                document.removePage(pageIndex);
            }
        }

        document.save(outputPath);
        document.close();
        System.out.println("Pages removed and saved to: " + outputPath);
    }

    /**
     * Rotate pages in a PDF
     * @param inputPath Absolute path to the PDF file
     * @param outputPath Absolute path for the output PDF
     * @param rotation Rotation angle (90, 180, 270)
     */
    public static void rotatePages(String inputPath, String outputPath, int rotation) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));

        for (PDPage page : document.getPages()) {
            page.setRotation(rotation);
        }

        document.save(outputPath);
        document.close();
        System.out.println("Pages rotated by " + rotation + " degrees and saved to: " + outputPath);
    }

    /**
     * Get information about a PDF
     * @param inputPath Absolute path to the PDF file
     */
    public static void getPDFInfo(String inputPath) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));

        System.out.println("PDF Information for: " + inputPath);
        System.out.println("Number of pages: " + document.getNumberOfPages());
        System.out.println("Is encrypted: " + document.isEncrypted());
        System.out.println("PDF Version: " + document.getVersion());

        if (document.getDocumentInformation() != null) {
            System.out.println("Title: " + document.getDocumentInformation().getTitle());
            System.out.println("Author: " + document.getDocumentInformation().getAuthor());
            System.out.println("Subject: " + document.getDocumentInformation().getSubject());
            System.out.println("Keywords: " + document.getDocumentInformation().getKeywords());
            System.out.println("Creator: " + document.getDocumentInformation().getCreator());
            System.out.println("Producer: " + document.getDocumentInformation().getProducer());
        }

        document.close();
    }

    /**
     * Add a blank page to a PDF
     * @param inputPath Absolute path to the PDF file
     * @param outputPath Absolute path for the output PDF
     */
    public static void addBlankPage(String inputPath, String outputPath) throws IOException {
        PDDocument document = Loader.loadPDF(new File(inputPath));

        PDPage blankPage = new PDPage();
        document.addPage(blankPage);

        document.save(outputPath);
        document.close();
        System.out.println("Blank page added and saved to: " + outputPath);
    }

    // Main method for testing
    public static void main(String[] args) {
        try {
            // Example usage - uncomment to test
            mergePDFs(new String[]{
//                    "C:\\sandeep\\daily\\20241027\\1.pdf",
//                    "C:\\sandeep\\daily\\20241027\\2.pdf",
//                    "C:\\sandeep\\daily\\20241027\\3.pdf",
//                    "C:\\sandeep\\daily\\20241027\\4.pdf",
                    "C:\\sandeep\\daily\\20241027\\5.pdf",
                    "C:\\sandeep\\daily\\20241027\\6.pdf",
            }, "C:\\sandeep\\daily\\20241027\\binder-3.pdf");
            // splitPDF("C:\\path\\input.pdf", "C:\\path\\output");
            // extractPages("C:\\path\\input.pdf", "C:\\path\\output.pdf", 1, 5);
            // convertToImages("C:\\path\\input.pdf", "C:\\path\\images", 300, "jpg");
            // extractText("C:\\path\\input.pdf", "C:\\path\\output.txt");
            // getPDFInfo("C:\\path\\input.pdf");

            System.out.println("PDFBox Tools Ready!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
