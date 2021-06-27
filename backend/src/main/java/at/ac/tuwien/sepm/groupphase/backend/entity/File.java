package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @ToString.Exclude
    private byte[] data;

    @Enumerated(EnumType.STRING)
    private Type type;

    public File(byte[] data, Type type) {
        this.data = data;
        this.type = type;
    }

    /**
     * Lists all file formats supported by this application.
     */
    public enum Type {

        // Supported file formats
        IMAGE_JPG, IMAGE_JPEG, IMAGE_PNG, APPLICATION_PDF;

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
