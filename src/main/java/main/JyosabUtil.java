package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

public class JyosabUtil {

	public JyosabUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fileChooser.setMultiSelectionEnabled(false);
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "All Files";
			}

			@Override
			public boolean accept(File f) {
				if (f.getName().endsWith(".png")||f.getName().endsWith(".jpg")||f.getName().endsWith(".jpeg")) {
					return true;
				}

				return false;
			}
		});

		int option = fileChooser.showSaveDialog(null);
		if (option != JFileChooser.APPROVE_OPTION) {
			return;
		}

		File file = fileChooser.getSelectedFile();
		Image img = null;
		try {
			img = Image.getInstance(file.getAbsolutePath());
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FileOutputStream outputStream = null;
		if(img!=null) { 
			try {
				File dir = new File("Export");
				if(!dir.exists())
					dir.mkdirs();
				try {
				outputStream = new FileOutputStream("Export\\"+file.getName().substring(0,file.getName().indexOf("."))+".pdf");
				Rectangle A4 = PageSize.A4;

				float scalePortrait = Math.min(A4.getWidth() / img.getWidth(),
						A4.getHeight() / img.getHeight());

				float scaleLandscape = Math.min(A4.getHeight() / img.getWidth(),
						A4.getWidth() / img.getHeight());

				// We try to occupy as much space as possible
				// Sportrait = (w*scalePortrait) * (h*scalePortrait)
				// Slandscape = (w*scaleLandscape) * (h*scaleLandscape)

				// therefore the bigger area is where we have bigger scale
				boolean isLandscape = scaleLandscape > scalePortrait;

				float w;
				float h;
				if (isLandscape) {
					A4 = A4.rotate();
					w = img.getWidth() * scaleLandscape;
					h = img.getHeight() * scaleLandscape;
				} else {
					w = img.getWidth() * scalePortrait;
					h = img.getHeight() * scalePortrait;
				}

				Document document = new Document(A4, 10, 10, 10, 10);

				try {
					PdfWriter.getInstance(document, outputStream);
				} catch (DocumentException e) {
					throw new IOException(e);
				}
				document.open();
				try {
					img.scaleAbsolute(w, h);
					float posH = (A4.getHeight() - h) / 2;
					float posW = (A4.getWidth() - w) / 2;

					img.setAbsolutePosition(posW, posH);
					img.setBorder(Image.NO_BORDER);
					img.setBorderWidth(0);

					try {
						document.newPage();
						document.add(img);
					} catch (DocumentException de) {
						throw new IOException(de);
					}
				} finally {
					document.close();
				}
			} finally {
				outputStream.close();
			}
		
	
			}catch(Exception ex) {
				
			}

}

}

}
