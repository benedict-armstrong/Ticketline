package at.ac.tuwien.sepm.groupphase.backend.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    private byte[] data;

    @Enumerated(EnumType.STRING)
    private Type type;

    public File() { }

    public File(byte[] data, Type type) {
        this.data = data;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "File{"
            + "id=" + id
            + ", data=" + (data == null ? "null" : "*BLOB*")
            + ", type=" + type
            + '}';
    }

    public static final class FileBuilder {
        private Long id;
        private byte[] data;
        private Type type;

        public FileBuilder() {}

        public static FileBuilder aFile() {
            return new FileBuilder();
        }

        public FileBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public FileBuilder withData(byte[] data) {
            this.data = data;
            return this;
        }

        public FileBuilder withType(Type type) {
            this.type = type;
            return this;
        }

        public File build() {
            File file = new File();
            file.setId(id);
            file.setData(data);
            file.setType(type);
            return file;
        }
    }

    /**
     * Lists all file formats supported by this application.
     */
    public enum Type {

        // Supported file formats
        IMAGE_JPG, IMAGE_JPEG, IMAGE_PNG;

        /*
         *  To add support for more file formats, complete the list of enum cases above.
         *  The names of the cases should mimic MIME types: "application/json" <=> APPLICATION_JSON
         *
         *  If your entity handles File entities or Types, the respective Mapper must extend FileTypeMapper.
         */

        /**
         * Parses the MIME type string and returns the corresponding File.Type case.
         *
         * @param mime an MIME as a string
         * @return the corresponding File.Type case
         * @throws IllegalArgumentException if the file format is not supported
         */
        public static Type fromMime(String mime) throws IllegalArgumentException {
            return valueOf(mime.toUpperCase().replace('/', '_'));
        }

        /**
         * Translates a File.Type case to the corresponding MIME type string.
         *
         * @return the MIME type string of this file type.
         */
        public String toMime() {
            return this.toString().toLowerCase().replace('_', '/');
        }

    }

}
