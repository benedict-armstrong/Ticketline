package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

public class PaginationDto {

    private Integer page;

    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    private static final class PaginationDtoBuilder {
        private Integer page;
        private Integer size;

        public PaginationDtoBuilder aPaginationDto() {
            return new PaginationDtoBuilder();
        }

        public PaginationDtoBuilder withPage(Integer page) {
            this.page = page;
            return this;
        }

        public PaginationDtoBuilder withSize(Integer size) {
            this.size = size;
            return this;
        }

        public PaginationDto build() {
            PaginationDto paginationDto = new PaginationDto();
            paginationDto.setPage(page);
            paginationDto.setSize(size);
            return paginationDto;
        }
    }

}
