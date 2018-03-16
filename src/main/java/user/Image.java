package util;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Image {
    @Column(name = "file_name",
            nullable = false, updatable = false)
    private String fileName;

    @Column(nullable = false, updatable = false)
    private String extension;

    private Integer height;
    private Integer width;

    protected Image() {
    }

    public Image(String fileName, String extension) {
        this.fileName = fileName;
        this.extension = extension;
    }

    public Image(String fileName, String extension, Integer height, Integer width) {
        this(fileName, extension);
        this.height = height;
        this.width = width;
    }

    public String getFileName() {
        return fileName;
    }

    public String getExtension() {
        return extension;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (!fileName.equals(image.fileName)) return false;
        if (!extension.equals(image.extension)) return false;
        if (height != null ? !height.equals(image.height) : image.height != null) return false;
        return width != null ? width.equals(image.width) : image.width == null;
    }

    @Override
    public int hashCode() {
        int result = fileName.hashCode();
        result = 31 * result + extension.hashCode();
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        return result;
    }
}
