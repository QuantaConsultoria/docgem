package com.quantaconsultoria.docgem.util;

import java.io.*;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.quantaconsultoria.docgem.format.html.HtmlBuilder;

public class FileUtil {
	
	private static final Logger LOG = Logger.getLogger(HtmlBuilder.class.getName());
	
	public static void close(FileOutputStream out) throws IOException {
		if(out != null) {
			out.close();
		}
	}

	public static void close(FileLock lock) throws IOException {
		if(lock != null) {
			lock.release();
			lock.close();
		}
	}
	
	public static void close(InputStream stream) {
		try {
			if(stream != null) {
				stream.close();
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Can't close the InputStream.");
		}
	}
	
	public static void close(FileReader fielFileReader) {
		try {
			if(fielFileReader != null) {
				fielFileReader.close();
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Can't close the FileReader.");
		}
	}
	
	public static void close(BufferedReader bufferedReader) {
		try {
			if(bufferedReader != null) {
				bufferedReader.close();
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Can't close the BufferedReader.");
		}
	}
	
	public static void close(Writer writer) {
		try {
			if(writer != null) {
				writer.close();
			}
		} catch (IOException e) {
			LOG.log(Level.WARNING, "Can't close the Writer.");
		}
	}

}
