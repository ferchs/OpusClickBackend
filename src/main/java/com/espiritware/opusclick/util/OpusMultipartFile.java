package com.espiritware.opusclick.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

public class OpusMultipartFile implements MultipartFile {

	private final String name;

	private String originalFilename;

	private String contentType;

	private final byte[] content;


	/**
	 * Create a new MockMultipartFile with the given content.
	 * @param name the name of the file
	 * @param content the content of the file
	 */
	public OpusMultipartFile(String name, byte[] content) {
		this(name, "", null, content);
	}

	/**
	 * Create a new MockMultipartFile with the given content.
	 * @param name the name of the file
	 * @param contentStream the content of the file as stream
	 * @throws IOException if reading from the stream failed
	 */
	public OpusMultipartFile(String name, InputStream contentStream) throws IOException {
		this(name, "", null, FileCopyUtils.copyToByteArray(contentStream));
	}

	/**
	 * Create a new MockMultipartFile with the given content.
	 * @param name the name of the file
	 * @param originalFilename the original filename (as on the client's machine)
	 * @param contentType the content type (if known)
	 * @param content the content of the file
	 */
	public OpusMultipartFile(String name, String originalFilename, String contentType, byte[] content) {
		Assert.hasLength(name, "Name must not be null");
		this.name = name;
		this.originalFilename = (originalFilename != null ? originalFilename : "");
		this.contentType = contentType;
		this.content = (content != null ? content : new byte[0]);
	}

	/**
	 * Create a new MockMultipartFile with the given content.
	 * @param name the name of the file
	 * @param originalFilename the original filename (as on the client's machine)
	 * @param contentType the content type (if known)
	 * @param contentStream the content of the file as stream
	 * @throws IOException if reading from the stream failed
	 */
	public OpusMultipartFile(String name, String originalFilename, String contentType, InputStream contentStream)
			throws IOException {

		this(name, originalFilename, contentType, FileCopyUtils.copyToByteArray(contentStream));
	}

    
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getOriginalFilename() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public String getContentType() {
		return "";
	}

	@Override
	public boolean isEmpty() {
		return (this.content.length == 0);
	}

	@Override
	public long getSize() {
		return this.content.length;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return this.content;
	}

	@Override
	public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(content);
	}

	@Override
	public void transferTo(File dest) throws IOException, IllegalStateException {
		FileCopyUtils.copy(this.content, dest);
	}

}
