package choremanager.ca.choremanager;

/**
 * Created by asadullahsansi on 11/30/17.
 */

public class choreAssignModel {
    String deadline;
    String value;
    String assignto;

    public choreAssignModel() {

    }

    public String getChoreassign() {
        return assignto;
    }

    public void setChoreassign(String choreassign) {
        this.assignto = choreassign;
    }

    public choreAssignModel(String deadline, String value, String choreassign) {
        this.deadline = deadline;
        this.value = value;
        this.assignto = choreassign;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
