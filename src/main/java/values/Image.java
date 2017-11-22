package values;

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
}
