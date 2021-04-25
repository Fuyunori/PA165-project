package tennisclub.dto;

import tennisclub.enums.CourtType;

public class CourtCreateDto {
    private String name;
    private String address;
    private CourtType type;
    private String previewImageUrl;

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
        if (!(o instanceof CourtCreateDto)) return false;

        CourtCreateDto that = (CourtCreateDto) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getAddress() != null ? !getAddress().equals(that.getAddress()) : that.getAddress() != null) return false;
        if (getType() != that.getType()) return false;
        return getPreviewImageUrl() != null ? getPreviewImageUrl().equals(that.getPreviewImageUrl()) : that.getPreviewImageUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getPreviewImageUrl() != null ? getPreviewImageUrl().hashCode() : 0);
        return result;
    }
}
