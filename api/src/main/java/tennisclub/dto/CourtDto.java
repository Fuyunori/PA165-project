package tennisclub.dto;

import tennisclub.enums.CourtType;

public class CourtDto {
    private Long id;
    private String name;
    private String address;
    private CourtType type;
    private String previewImageUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CourtType getType() {
        return type;
    }

    public void setType(CourtType type) {
        this.type = type;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CourtDto)) return false;

        CourtDto courtDto = (CourtDto) o;

        if (getName() != null ? !getName().equals(courtDto.getName()) : courtDto.getName() != null) return false;
        return getAddress() != null ? getAddress().equals(courtDto.getAddress()) : courtDto.getAddress() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        return result;
    }
}
