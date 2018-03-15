package edu.kai.downloader;

import com.itextpdf.text.*;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.Image;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.List;


public class Downloader {

    private void getPdf(String resourceId) throws IOException, DocumentException {
        getPdf(resourceId, "download" + System.nanoTime());
    }

    private void getPdf(String resourceId, String fileName) throws IOException, DocumentException {
        File pdfFile = new File(fileName + ".pdf");
        List<Image> images = new ArrayList<Image>();
        Dimension pageSize = new Dimension();

        HttpClient client = HttpClientBuilder.create().build();
        int pageCount = Integer.MAX_VALUE;
        for (int i = 1; i < pageCount; i++) {
            HttpGet request = new HttpGet("http://e-library.kai.ru/reader/hu/flipping/Resource-" + resourceId + "/xxx.pdf/pages/" + i);
            HttpResponse response = client.execute(request);
            InputStream inputStream = response.getEntity().getContent();
            Image image = ImageIO.read(inputStream);
            if (image != null) {
                pageSize = getMaxDimension(pageSize, image);
                images.add(image);
            } else {
                System.out.println("Progress: book downloading complete.");
                break;
            }
            System.out.println("Progress: " + i + " page downloading complete.");
            inputStream.close();
        }
        System.out.println("Progress:" + " creating document.");
        createDocument(pdfFile, images, pageSize);
        System.out.println("Progress:" + " creating complete.");
        System.out.println(pdfFile.getName() + " ready.");
    }

    private Dimension getMaxDimension(Dimension size, Image image) {
        if (image.getWidth(null) + image.getHeight(null) > size.getWidth() + size.getHeight()) {
            size = new Dimension(image.getWidth(null), image.getHeight(null));
        }
        return size;
    }

    private void createDocument(File pdfFile, List<Image> images, Dimension size) throws DocumentException, IOException {
        Document document = new Document(new Rectangle(size.width, size.height));
        PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
        document.open();

        for (Image image : images) {
            com.itextpdf.text.Image element = com.itextpdf.text.Image.getInstance(image, Color.blue);
            document.add(element);
            document.newPage();
        }

        document.close();
    }

    public static void main(String[] args) throws IOException, DocumentException {
        String fileName = null;
        String resourceId = null;
        Downloader downloader = new Downloader();

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-id".equals(arg)) {
                i++;
                resourceId = args[i];
            } else if ("-f".equals(arg)) {
                i++;
                fileName = args[i];
            } else if ("-help".equals(arg)) {
                System.out.println("Аргументы:");
                System.out.println("-id : id книги");
                System.out.println("-f  : название файла");
                System.exit(0);
            }
        }

        if ("".equals(resourceId) || resourceId == null) {
            System.out.println("Недостаточно аргументов. Не введен id документа.");
        } else if ("".equals(fileName) || fileName == null) {
            downloader.getPdf(resourceId);
        } else {
            downloader.getPdf(resourceId, fileName);
        }
    }
}
