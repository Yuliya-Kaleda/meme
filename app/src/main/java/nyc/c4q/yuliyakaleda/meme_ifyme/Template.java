package nyc.c4q.yuliyakaleda.meme_ifyme;

/**
 * Created by Ramona Harrison
 * on 7/14/15.
 */
public class Template {
    private String id;
    private String name;
    private byte[] image;

    public Template(String id, String name, byte[] image) {
        this.id = id;
        this.name = name;
        this.image = image;
    }

    public Template() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
