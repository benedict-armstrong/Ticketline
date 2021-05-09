package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class FileDto {

    private Long id;
    private byte[] data;
    private String type;

    public FileDto() {}

    public FileDto(Long id, byte[] data, String type) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "FileDto{"
            + "id=" + id
            + ", data=" + (data == null ? "null" : "*BLOB*")
            + ", type='" + type + '\''
            + '}';
    }

    public static final class FileDtoBuilder {
        private Long id;
        private byte[] file;
        private String type;

        public FileDtoBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public FileDtoBuilder withFile(byte[] file) {
            this.file = file;
            return this;
        }

        public FileDtoBuilder withType(String type) {
            this.type = type;
            return this;
        }

        public FileDto build() {
            FileDto fileDto = new FileDto();
            fileDto.setId(id);
            fileDto.setData(file);
            fileDto.setType(type);
            return fileDto;
        }
    }

}
