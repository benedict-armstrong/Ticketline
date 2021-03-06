package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.File;

public interface FileService {

    /**
     * Saves the file in the database.
     *
     * @param file the file to be persisted.
     * @return the file entity with set ID.
     */
    File addFile(File file);

    /**
     * Gives back count of saved pdf files.
     *
     * @return count of pdfs.
     */
    int getPdfCount();

}
