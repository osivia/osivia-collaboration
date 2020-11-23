package org.osivia.services.contact.portlet.model;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
public class FormCaptcha {

    /**
     * Captcha token.
     */
    private String token;
    /**
     * Captcha user input.
     */
    private String input;
    /**
     * Already validated indicator.
     */
    private boolean validated;


    /**
     * Constructor.
     */
    public FormCaptcha() {
        super();
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
}
