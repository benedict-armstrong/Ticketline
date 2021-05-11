package at.ac.tuwien.sepm.groupphase.backend.entity.enumeration;

/**
 * Lists all file formats supported by this application.
 */
public enum FileType {

    // Supported file formats
    IMAGE_JPG, IMAGE_JPEG, IMAGE_PNG;

    /*
     *  To add support for more file formats, complete the list of enum cases above.
     *  The names of the cases should mimic MIME types: "application/json" <=> APPLICATION_JSON
     *
     *  If your entity handles File entities or FileTypes, the respective Mapper must extend FileTypeMapper.
     */

    /**
     * Parses the MIME type string and returns the corresponding FileType case.
     *
     * @param mime an MIME as a string
     * @return the corresponding FileType case
     * @throws IllegalArgumentException if the file format is not supported
     */
    public static FileType fromMime(String mime) throws IllegalArgumentException {
        return valueOf(mime.toUpperCase().replace('/', '_'));
    }

    /**
     * Translates a FileType case to the corresponding MIME type string.
     *
     * @return the MIME type string of this file type.
     */
    public String toMime() {
        return this.toString().toLowerCase().replace('_', '/');
    }

}