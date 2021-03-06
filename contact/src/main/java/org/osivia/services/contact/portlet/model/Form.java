package org.osivia.services.contact.portlet.model;

public class Form
{
  private String from;
  private String fromLabel;
  private String to;
  private String object;
  private String objectLabel;
  private String body;
  private String bodyLabel;
  private String nom;
  private String nomLabel;
  private String captcha;
  private String token;
  private boolean captchaValidate;  
  private boolean sent;
  
  public String getTo()
  {
    return to;
  }
  
  public void setTo(String to)
  {
    this.to = to;
  }
  
  public String getObject()
  {
    return object;
  }
  
  public void setObject(String object)
  {
    this.object = object;
  }
  
  public String getBody()
  {
    return body;
  }
  
  public void setBody(String body)
  {
    this.body = body;
  }
  
  public String getFrom()
  {
    return from;
  }
  
  public void setFrom(String from)
  {
    this.from = from;
  }
  
  public boolean isSent()
  {
    return sent;
  }
  
  public void setSent(boolean sent)
  {
    this.sent = sent;
  }

  
  public String getNom()
  {
    return nom;
  }
  
  public void setNom(String nom)
  {
    this.nom = nom;
  }
  
  
  public String getFromLabel()
  {
    return fromLabel;
  }
  
  public void setFromLabel(String fromLabel)
  {
    this.fromLabel = fromLabel;
  }
  
  public String getObjectLabel()
  {
    return objectLabel;
  }
  
  public void setObjectLabel(String objectLabel)
  {
    this.objectLabel = objectLabel;
  }
  
  public String getBodyLabel()
  {
    return bodyLabel;
  }
  
  public void setBodyLabel(String bodyLabel)
  {
    this.bodyLabel = bodyLabel;
  }
  
  public String getNomLabel()
  {
    return nomLabel;
  }
  
  public void setNomLabel(String nomLabel)
  {
    this.nomLabel = nomLabel;
  }

	public String getCaptcha() {
		return captcha;
	}
	
	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public boolean isCaptchaValidate() {
		return captchaValidate;
	}

	public void setCaptchaValidate(boolean captchaValidate) {
		this.captchaValidate = captchaValidate;
	}

}

