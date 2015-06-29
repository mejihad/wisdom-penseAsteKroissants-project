package fr.astek.api.utils;

import org.apache.commons.io.FileUtils;
import org.wisdom.api.http.FileItem;
import org.wisdom.api.http.MimeTypes;

import java.io.*;

/**
 * Created by jmejdoub on 22/06/2015.
 */
public class PakFakeFileItem implements FileItem {

    /**
     * The file.
     */
    private final File file;

    /**
     * The field of the form having sent the file.
     */
    private final String field;

    /**
     * Creates a new fake file item.
     *
     * @param file  the file, must not be {@literal null}
     * @param field the field name, can be {@literal null}
     */
    public PakFakeFileItem(File file, String field) {
        this.file = file;
        this.field = field;
    }

    /**
     * @return the field, may be {@literal null}.
     */
    @Override
    public String field() {
        return field;
    }

    /**
     * @return the name of the file.
     */
    @Override
    public String name() {
        return file.getName();
    }

    /**
     * @return the content of the file.
     */
    @Override
    public byte[] bytes() {
        try {
            return FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @return a stream on the content of the file.
     */
    @Override
    public InputStream stream() {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * This method is not usable from tests.
     *
     * @return {@literal false}
     */
    @Override
    public boolean isInMemory() {
        return false;
    }

    /**
     * Tries to guess the mime-type of the file by analyzing its extension.
     *
     * @return the mime-type, {@literal null} if unknown
     */
    @Override
    public String mimetype() {
        return MimeTypes.getMimeTypeForFile(file);
    }

    /**
     * @return the file's length.
     */
    @Override
    public long size() {
        return file.length();
    }

    /**
     * Gets a {@link java.io.File} object for this uploaded file. This file is a <strong>temporary</strong> file.
     * Depending on how is handled the file upload, the file may already exist, or not (in-memory) and then is created.
     *
     * @return a file object
     * @since 0.7.1
     */
    @Override
    public File toFile() {
        return file;
    }
}
