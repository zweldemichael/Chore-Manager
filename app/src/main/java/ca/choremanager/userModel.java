package choremanager.ca.choremanager;


public class userModel {
    String image;

    String name;

    String thumb;

    String usertype;
    String points;
    String uid;

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public userModel() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public userModel(String image, String name, String thumb, String usertype, String points, String uid) {
        this.image = image;
        this.name = name;
        this.thumb = thumb;
        this.usertype = usertype;
        this.points = points;
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
