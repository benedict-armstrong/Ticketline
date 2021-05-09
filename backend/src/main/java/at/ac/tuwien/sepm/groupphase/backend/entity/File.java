package at.ac.tuwien.sepm.groupphase.backend.entity;

import at.ac.tuwien.sepm.groupphase.backend.entity.enumeration.FileType;

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
    private FileType type;

    public File() { }

    public File(byte[] data, FileType type) {
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

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
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
        private FileType type;

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

        public FileBuilder withType(FileType type) {
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

}
