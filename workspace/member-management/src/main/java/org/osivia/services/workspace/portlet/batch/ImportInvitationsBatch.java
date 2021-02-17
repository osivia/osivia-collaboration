package org.osivia.services.workspace.portlet.batch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.portlet.PortletContext;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.services.workspace.portlet.model.InvitationState;
import org.osivia.services.workspace.portlet.repository.GetInvitationsCommand;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.osivia.services.workspace.portlet.repository.SetProcedureInstanceAcl;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.sun.mail.smtp.SMTPTransport;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;
import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;
import fr.toutatice.portail.cms.nuxeo.api.forms.IFormsService;

/**
 * 
 * @author Loïc Billon
 *
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportInvitationsBatch extends NuxeoBatch {
	
	/** Process only MAX_RECORDS on a single file */
	public static final int MAX_RECORDS = 100;
	
	protected final static Log logger = LogFactory.getLog("batch");

	/** POJO for the batch */
	protected ImportObject dto;

	protected WorkspaceService workspaceService;

	/** current invitations in workspace */
	protected List<String> invitations;

	/** current request in workspace */
	protected List<String> requests;

	/** rejects file sended to the initiator */
	protected File rejects;
	
	/** mail title */
	protected String subject = "Import de comptes";
	
	private CSVPrinter rejectsPrinter;
	
	
	private static PortletContext portletCtx;
	
    /** regex for mails */
    private static final String MAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    
    /** Mail pattern. */
    private final Pattern mailPattern;

	private PersonUpdateService personService;


	/** white list pattern for mail adresses */
	private String[] whiteList;

    
	
	@Override
	public boolean isRunningOnMasterOnly() {
		return false;
	}

	public ImportInvitationsBatch(PortletContext portletCtx, ImportObject dto) {
		
		super(dto.getTemporaryFile().getName());
		
		ImportInvitationsBatch.portletCtx = portletCtx;
		this.dto = dto;
		
        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);
        
		personService = DirServiceFactory.getService(PersonUpdateService.class);
		
		workspaceService = DirServiceFactory.getService(WorkspaceService.class);
		
		String whiteListStr = System.getProperty("osivia.invitations.whitelist");
		if(StringUtils.isNotEmpty(whiteListStr)) {
			whiteList = whiteListStr.split(",");
		}
		
	}


	@Override
	protected PortletContext getPortletContext() {
		return ImportInvitationsBatch.portletCtx;
	}

	@Override
	public String getJobScheduling() {
		return null;
	}

	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {

		populateInvitations(false);

		populateInvitations(true);
		
		
		try {
			CSVParser parser = CSVParser.parse(dto.getTemporaryFile(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
			

			int count = 1, countreject = 0, countinvitation = 0, countalreadymember = 0, countwf = 0;
			
			boolean hasRejects = false;
			for(CSVRecord record : parser) {
				
				if(count >= MAX_RECORDS) { // security for large csv files
					break;
				}
				List<String> invitationsValidated = new ArrayList<String>();
				
				String uid = record.get(0); // skip blank lines
				
				// Duplicates
				if(invitationsValidated.contains(uid)) {
                	logger.debug(getBatchId()+" / "+count+"/"+uid+" : reject");
                	
    				rejectsPrinter = getRejectPrinter();
    				rejectsPrinter.printRecord(uid, "Doublon ");
                	
                	countreject++;
                	hasRejects = true;
				}
				if(StringUtils.isNotBlank(uid)) {
					
					// syntax control or reject

		    		// minify uid
		    		uid = StringUtils.lowerCase(uid);
		    		// trim
		    		uid = uid.trim();
		    		
		    		
		    		Matcher matcher = this.mailPattern.matcher(uid);
	                if (!matcher.matches() || !isAllowedInList(uid)) {
	                	
	                	logger.debug(getBatchId()+" / "+count+"/"+uid+" : reject");
	                	
	    				rejectsPrinter = getRejectPrinter();
	    				rejectsPrinter.printRecord(uid, "Entrée invalide / non autorisée ");
	                	
	                	countreject++;
	                	hasRejects = true;
	                }
	                else {

						// if person does not exist, init person
	                	boolean unknownUser = false;
		    			Person person = personService.getPerson(uid);
		    			if(person == null) {
		    				
	        	    		person = personService.getEmptyPerson();

    	    	    		person.setUid(uid);
    	    	    		person.setMail(uid);
    	    	    		person.setSn(uid);
    	    	    		person.setGivenName(uid);
    	    	    		person.setCn(uid);
    	    	    		person.setDisplayName(uid);
    	    	    		personService.create(person);
    	    	    		
    	    	    		unknownUser = true;
		    			}
	                							
						// if person is currently member, do nothing
		    			WorkspaceMember member = workspaceService.getMember(dto.getWorkspaceId(), uid);
		    			if(member == null) {

							// if person is currently on a workflow, do nothing

		    				if(!(invitations.contains(uid)) && !(requests.contains(uid))) {


			                	logger.debug(getBatchId()+" / "+count+"/"+uid+" : send invitation");
		    					
								// otherwise, send invitation
		                        // Variables
		    					
		    	                // Local group identifiers
		    	                List<String> localGroupIds;
		    	                if (CollectionUtils.isEmpty(dto.getLocalGroups())) {
		    	                    localGroupIds = new ArrayList<>(0);
		    	                } else {
		    	                    localGroupIds = new ArrayList<>(dto.getLocalGroups().size());
		    	                    for (CollabProfile localGroup : dto.getLocalGroups()) {
		    	                        localGroupIds.add(localGroup.getCn());
		    	                    }
		    	                }
		    	                
		    					
		                        Map<String, String> variables = new HashMap<>();
		                        variables.put(MemberManagementRepository.WORKSPACE_PATH_PROPERTY, dto.getCurrentWorkspace().getPath());
		                        variables.put(MemberManagementRepository.INITIATOR_PROPERTY, dto.getInitiator());
		                        variables.put(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY, dto.getWorkspaceId());
		                        variables.put(MemberManagementRepository.WORKSPACE_TITLE_PROPERTY, dto.getCurrentWorkspace().getTitle());
		                        variables.put(MemberManagementRepository.PERSON_UID_PROPERTY, uid);
		                        variables.put(MemberManagementRepository.INVITATION_STATE_PROPERTY, InvitationState.SENT.name());
		                        variables.put(MemberManagementRepository.ROLE_PROPERTY, dto.getRole().getId());
		                        variables.put(MemberManagementRepository.INVITATION_LOCAL_GROUPS_PROPERTY, StringUtils.join(localGroupIds, "|"));
		                        variables.put(MemberManagementRepository.INVITATION_MESSAGE_PROPERTY, dto.getMessage());
		                        variables.put(MemberManagementRepository.NEW_USER_PROPERTY, String.valueOf(unknownUser));

		                        if (unknownUser) {
		                            // Generated password
		                            String password = RandomStringUtils.randomAlphanumeric(8);
		                            variables.put(MemberManagementRepository.GENERATED_PASSWORD_PROPERTY, password);

		                        }
		                        

		                        // Start
		                        startProcedure(IFormsService.FORMS_WEB_ID_PREFIX + MemberManagementRepository.INVITATION_MODEL_ID, variables);

		                        // Update ACL
		                        // Workspace admin & owner groups
		                        List<CollabProfile> groups = new ArrayList<>();
		                        CollabProfile criteria = this.workspaceService.getEmptyProfile();
		                        criteria.setWorkspaceId(dto.getWorkspaceId());
		                        criteria.setRole(WorkspaceRole.ADMIN);
		                        groups.addAll(this.workspaceService.findByCriteria(criteria));
		                        criteria.setRole(WorkspaceRole.OWNER);
		                        groups.addAll(this.workspaceService.findByCriteria(criteria));

		                        // Update ACL
		                        INuxeoCommand command =  new SetProcedureInstanceAcl(dto.getWorkspaceId(), false, uid, groups);
		                        getNuxeoController().executeNuxeoCommand(command);
		                        
		                        invitationsValidated.add(uid);
		                        
		                        countinvitation++;
		                        
			                	logger.debug(getBatchId()+" / "+count+"/"+uid+" : invitation sent");

		    				}
		    				else {
		    					countwf++;
			    				rejectsPrinter = getRejectPrinter();
		    					
		    					if(invitations.contains(uid)) {
		    						logger.debug(getBatchId()+" / "+count+"/"+uid+" : already invited");
				    				rejectsPrinter.printRecord(uid, "Déjà invité");

		    					}
		    					if(requests.contains(uid)) {
		    						logger.debug(getBatchId()+" / "+count+"/"+uid+" : has already request to join");
		    						rejectsPrinter.printRecord(uid, "Demande d'invitation en cours");
		    					}
		    					hasRejects = true;
		    					
		    				}
		    				
		    			}
		    			else {
		    				countalreadymember++;
		                	logger.debug(getBatchId()+" / "+count+"/"+uid+" : already member");
		                	rejectsPrinter = getRejectPrinter();
    						rejectsPrinter.printRecord(uid, "Déjà membre");
    						hasRejects = true;
		    			}
	                }
				}
				
    	    	count++;
    	    	dto.setRecordNumber(dto.getRecordNumber() -1);
			}
			
			if(hasRejects) {
				rejectsPrinter.flush();

			}
		
			dto.setCount(count);
			dto.setCountalreadymember(countalreadymember);
			dto.setCountinvitation(countinvitation);
			dto.setCountreject(countreject);
			dto.setCountwf(countwf);
			sendMailReport();
			
			if(hasRejects) {
				rejectsPrinter.flush();

			}
		
		} catch (IOException | FormFilterException e) {
			throw new PortalException(e);
		}
		finally {
	        	
	        dto.getTemporaryFile().delete();

	        if(rejects != null) {
	        	rejects.delete();
	        }
	        
		}

	}


	private boolean isAllowedInList(String uid) {
		boolean ret = true; // default : no white list, all allowed
		
		if(whiteList != null) {
			ret = false; // if white list, deny
			for(int i = 0; i < whiteList.length; i++) {
				if(uid.contains(whiteList[i])) {
					ret = true; // if pattern found, allox
					break;
				}
			}
		}
		
		return ret;
	}


	protected void populateInvitations(boolean checkRequests) {
		
		NuxeoController nuxeoController = getNuxeoController();
		// if person is currently invited, do nothing
		GetInvitationsCommand invitationsCmd = new GetInvitationsCommand(dto.getWorkspaceId(), checkRequests);
		Documents invitationDocs = (Documents) nuxeoController.executeNuxeoCommand(invitationsCmd);
        List<String> workList = new ArrayList<>(invitationDocs.size());
        for (Document document : invitationDocs.list()) {
            // Variables
            PropertyMap variables = document.getProperties().getMap("pi:globalVariablesValues");

            // User identifier
            String uid = variables.getString(MemberManagementRepository.PERSON_UID_PROPERTY);
            // Invitation state
            InvitationState state = InvitationState.fromName(variables.getString(MemberManagementRepository.INVITATION_STATE_PROPERTY));

            if (InvitationState.SENT.equals(state)) {

                workList.add(uid);
            }
        }
        
        if(checkRequests) {
        	this.requests = workList;
        }
        else {
        	this.invitations = workList;

        }
	}
	
	protected void sendMailReport() throws PortalException {
        try {
			// Sender email
	        Person initiatorPerson = this.personService.getPerson(dto.getInitiator());
	        String initiatorMail = initiatorPerson.getMail();

	        Session mailSession = Session.getInstance(System.getProperties(), null);
	
	        // Message
	        MimeMessage message = new MimeMessage(mailSession);

			message.setSentDate(new Date());
	
            // From
			String fromProperty = System.getProperty("osivia.procedures.default.mail.from");
			
			if(fromProperty == null) {
				throw new PortalException("No osivia.procedures.default.mail.from configured !");
			}
			
            InternetAddress from = new InternetAddress(fromProperty);
            message.setFrom(from);

            // To
            InternetAddress[] to = InternetAddress.parse(initiatorMail);
            message.setRecipients(Message.RecipientType.TO, to);
            
            message.setSubject(subject, CharEncoding.UTF_8);
            
            boolean attachment = false;
            StringBuilder body = new StringBuilder();
            body.append("<p><b>Rapport d'invitations envoyées pour l'espace ");
            body.append(dto.getCurrentWorkspace().getTitle());
            
            body.append("<p>Fichier : "+dto.getTemporaryFileName()+" </p>");
            
            body.append("</b></p><p> Invitations envoyées : ");
            body.append(dto.getCountinvitation());
            body.append("</p>");
            
            
            CollabProfile profile = workspaceService.getEmptyProfile();
            profile.setWorkspaceId(dto.getWorkspaceId());
			List<CollabProfile> findByCriteria = workspaceService.findByCriteria(profile );
			body.append("<p> Rôle et groupes affectés : <ul> ");
			
			for(CollabProfile collab : findByCriteria) {
				
				if(collab.getRole() == dto.getRole()) {
					body.append("<li>"+collab.getDisplayName()+"</li>");
				}
				
				if(dto.getLocalGroups() != null) {
					for(CollabProfile group : dto.getLocalGroups()) {
						if(group.getCn().equals(collab.getCn())) {
							body.append("<li>"+collab.getDisplayName()+"</li>");
						}
					}
				}
			}
			
			body.append("</ul></p>");
			
			
			if(StringUtils.isNotBlank(dto.getMessage())) {
				body.append("<p>Message : "+dto.getMessage()+" </p>");
			}
						
            
            if(dto.getCountalreadymember() > 0) {
            	attachment = true;
            	
                body.append("<p> Personnes déjà membres de l'espace : ");
                body.append(dto.getCountalreadymember());
                body.append("</p>");
            }
            
            if(dto.getCountwf() > 0) {
            	attachment = true;
            	
                body.append("<p> Personnes déjà invitées : ");
                body.append(dto.getCountwf());
                body.append("</p>");
            }

            
            if(dto.getCountreject() > 0) {
            	attachment = true;
            	
                body.append("<p> Entrées invalides dans le fichier : ");
                body.append(dto.getCountreject());
                body.append("</p>");
            }            

            // Multipart
            Multipart multipart = new MimeMultipart();
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body.toString(), "text/html; charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            
            if(attachment) {
            	MimeBodyPart messageBodyPart = new MimeBodyPart();

            	DataSource source = new FileDataSource(rejects);
            	messageBodyPart.setDataHandler(new DataHandler(source));
            	messageBodyPart.setFileName(rejects.getName());
            	multipart.addBodyPart(messageBodyPart);
            }
            
            message.setContent(multipart);
            
            // SMTP transport
            SMTPTransport transport = (SMTPTransport) mailSession.getTransport();
            transport.connect();
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            
        } catch (MessagingException e) {
            throw new PortalException("Error sending email report", e);
        }

        
	}
	
	/**
	 * Create a file for rejects if not exist
	 * @return
	 * @throws IOException
	 */
	protected CSVPrinter getRejectPrinter() throws IOException {

		if(rejectsPrinter == null) {
			
			rejects = File.createTempFile(dto.getWorkspaceId() +"_"+new Date().getTime(), "_rejets.csv");			
			rejects.createNewFile();
			
			rejectsPrinter = new CSVPrinter(new FileWriter(rejects), CSVFormat.EXCEL);
		}
		return rejectsPrinter;
	}

	public ImportObject getDto() {
		return dto;
	}
	
	

}
