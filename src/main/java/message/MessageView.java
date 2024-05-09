package message;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named("messageView")
@SessionScoped

public class MessageView implements Serializable {

    public void addMessage(FacesMessage.Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public void showSuccess(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }

    public void showFailure(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
    }

    public void showInvalidInput(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_WARN, summary, detail);
    }
    
    public void showInfo(String summary, String detail) {
        addMessage(FacesMessage.SEVERITY_INFO, summary, detail);
    }
}
