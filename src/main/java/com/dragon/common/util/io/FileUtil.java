package com.dragon.common.util.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;

public class FileUtil {

	public static final int BUFFER_SIZE = 1024;

	/**
	 * Delete file or folder.
	 * 
	 * @param root
	 *            of the file or folder path to delete
	 * @return <code>true</code> if the <code>File</code> was deleted,
	 * @throws IOException
	 *             in case of I/O errors or input path is null.
	 */
	public static boolean delFileOrFolder(String path) throws IOException {

		if (path == null) {
			throw new IOException("The path must not be null");
		}
		return deleteRecursively(new File(path));
	}

	/**
	 * Copy file.
	 * 
	 * @param source
	 * @param dest
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copyFileOrFolder(String source, String dest)
			throws IOException {

		if (source == null) {
			throw new IOException("Source File Path must not be null");
		}
		if (dest == null) {
			throw new IOException("Destination File Path must not be null");
		}
		copyRecursively(new File(source), new File(dest));
	}

	/**
	 * Delete the supplied {@link File} - for directories, recursively delete
	 * any nested directories or files as well.
	 * 
	 * @param root
	 *            the root <code>File</code> to delete
	 * @return <code>true</code> if the <code>File</code> was deleted, otherwise
	 *         <code>false</code>
	 */
	public static boolean deleteRecursively(File root) {
		if (root == null && !root.exists()) {
			return false;
		}
		if (root.isDirectory()) {
			File[] children = root.listFiles();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					deleteRecursively(children[i]);
				}
			}
		}
		return root.delete();
	}

	/**
	 * Recursively copy the contents of the <code>src</code> file/directory to
	 * the <code>dest</code> file/directory.
	 * 
	 * @param src
	 *            the source directory
	 * @param dest
	 *            the destination directory
	 * @throws IOException
	 *             in the case of I/O errors
	 */

	public static void copyRecursively(File src, File dest) throws IOException {
		if (src == null && (!src.isDirectory() || !src.isFile())) {
			throw new IOException("Source File must denote a directory or file");
		}
		if (dest == null) {
			throw new IOException("Destination File must not be null");
		}
		doCopyRecursively(src, dest);
	}

	/**
	 * Actually copy the contents of the <code>src</code> file/directory to the
	 * <code>dest</code> file/directory.
	 * 
	 * @param src
	 *            the source directory
	 * @param dest
	 *            the destination directory
	 * @throws IOException
	 *             in the case of I/O errors
	 */
	private static void doCopyRecursively(File src, File dest)
			throws IOException {
		if (src.isDirectory()) {
			dest.mkdir();
			File[] entries = src.listFiles();
			if (entries == null) {
				throw new IOException("Could not list files in directory: "
						+ src);
			}
			for (int i = 0; i < entries.length; i++) {
				doCopyRecursively(entries[i],
						new File(dest, entries[i].getName()));
			}

		} else if (src.isFile()) {
			try {
				dest.createNewFile();
			} catch (IOException ex) {
				IOException ioex = new IOException("Failed to create file: "
						+ dest);
				ioex.initCause(ex);
				throw ioex;
			}
			copy(src, dest);
		} else {
			// Special File handle: neither a file not a directory.
			// Simply skip it when contained in nested directory...
		}
	}

	/**
	 * Copy the contents of the given input File to the given output File.
	 * 
	 * @param in
	 *            the file to copy from
	 * @param out
	 *            the file to copy to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static int copy(File in, File out) throws IOException {

		return copy(new BufferedInputStream(new FileInputStream(in)),
				new BufferedOutputStream(new FileOutputStream(out)));
	}

	/**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Closes both streams when done.
	 * 
	 * @param in
	 *            the stream to copy from
	 * @param out
	 *            the stream to copy to
	 * @return the number of bytes copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static int copy(InputStream in, OutputStream out) throws IOException {

		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given byte array to the given OutputStream.
	 * Closes the stream when done.
	 * 
	 * @param in
	 *            the byte array to copy from
	 * @param out
	 *            the OutputStream to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(byte[] in, OutputStream out) throws IOException {

		if (in == null) {
			throw new IOException("No input byte array specified");
		}
		if (out == null) {
			throw new IOException("No OutputStream specified");
		}
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given Reader to the given Writer. Closes both
	 * when done.
	 * 
	 * @param in
	 *            the Reader to copy from
	 * @param out
	 *            the Writer to copy to
	 * @return the number of characters copied
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static int copy(Reader in, Writer out) throws IOException {
		if (in == null) {
			throw new IOException("No Reader specified");
		}
		if (out == null) {
			throw new IOException("No Writer specified");
		}
		try {
			int byteCount = 0;
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
			}
			try {
				out.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given String to the given output Writer. Closes
	 * the write when done.
	 * 
	 * @param in
	 *            the String to copy from
	 * @param out
	 *            the Writer to copy to
	 * @throws IOException
	 *             in case of I/O errors
	 */
	public static void copy(String in, Writer out) throws IOException {
		
		if (in == null) {
			throw new IOException("No input String specified");
		}
		if (out == null) {
			throw new IOException("No Writer specified");
		}
		try {
			out.write(in);
		} finally {
			try {
				out.close();
			} catch (IOException ex) {
				
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	public static void readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		try {
			
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			int line = 1;
			
			while ((tempString = reader.readLine()) != null) {
				
				System.out.println("line " + line + ": " + tempString);
				line++;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
	}

	
	/**
	 * A方法追加文件：使用RandomAccessFile
	 * 
	 * @param fileName
	 *            文件名
	 * @param content
	 *            追加的内容
	 */
	public static void appendMethodA(String fileName, String content) {
		try {
			// 打开一个随机访问文件流，按读写方式
			RandomAccessFile randomFile = new RandomAccessFile(fileName, "rw");
			// 文件长度，字节数
			long fileLength = randomFile.length();
			// 将写文件指针移到文件尾。
			randomFile.seek(fileLength);
			randomFile.writeBytes(content);
			randomFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * B方法追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendMethodB(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * C方法追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public static void appendMethodC(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
	          FileOutputStream fos = new FileOutputStream(fileName, true);   
	          OutputStreamWriter out=new OutputStreamWriter(fos,"UTF-8");
	          out.write(content);
	          out.close();	
	          fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void copyFile(File sourceFile,File targeFile) throws IOException{
		BufferedInputStream inBuff= null;
		BufferedOutputStream outBuff = null;
		try{
			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
			outBuff  = new BufferedOutputStream(new FileOutputStream(targeFile));
			//缓冲数组
			byte[] b = new byte[1024*5];
			int len;
			while((len = inBuff.read(b))!=-1){
				outBuff.write(b,0,len);
			}
			outBuff.flush();
		}finally{
			if(inBuff!=null){
				inBuff.close();
			}
			if(outBuff!=null){
				outBuff.close();
			}
		}
		
		
	}
	
	
	/**
	 * 
	 * @param file
	 */
	public static void chmod(File file){		
			String command = "chmod 777 " + file.getAbsolutePath();	
			Runtime runtime = Runtime.getRuntime();
			try {
				Process proc = runtime.exec(command);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	public static void copy(InputStream in, File out) throws IOException {
		OutputStream outStream = new BufferedOutputStream(new FileOutputStream(out));
		copy(in, outStream);
	}
	
	public static void main(String[] args) {
		appendMethodB("D:/cc.xml", "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>");
		appendMethodB("D:/cc.xml", "中华人民共和国");
		appendMethodB("D:/cc.xml", "</root>");
	}

}
