package at.ac.tuwien.sepm.groupphase.backend.entity.enumeration;

public enum FileType {

    // Supported file formats
    IMAGE_JPG, IMAGE_JPEG, IMAGE_PNG;

    /*
     *  To add support for more file formats, complete the list of enum cases above.
     *  The names of the cases should mimic MIME types: "application/json" <=> APPLICATION_JSON
     *
     *  If your entity handles File entities or FileTypes, the respective Mapper must extend FileTypeMapper.
     */

    public static FileType fromMime(String mime) throws IllegalArgumentException {
        return valueOf(mime.toUpperCase().replace('/', '_'));
    }

    public String toMime() {
        return this.toString().toLowerCase().replace('_', '/');
    }

}