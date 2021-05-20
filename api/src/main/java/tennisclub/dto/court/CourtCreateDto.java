package tennisclub.dto.court;

import tennisclub.enums.CourtType;

import javax.validation.constraints.NotBlank;

/**
 * @author Pavel Tobiáš
 */
public class CourtCreateDto {
    @NotBlank(message = "{court.name.notBlank}")
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
        return getAddress() != null ? getAddress().equals(that.getAddress()) : that.getAddress() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        return result;
    }
}
