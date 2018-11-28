package net.javaguides.springboot.fileuploaddownload.exception;

public class FileStorageException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
