/*
 * (C) Copyright 2014 Académie de Rennes (http://www.ac-rennes.fr/), OSIVIA (http://www.osivia.com) and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-2.1.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 *
 *
 */
package fr.toutatice.faq.portail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.path.PortletPathItem;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.PortletErrorHandler;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentAttachmentDTO;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * FAQ portlet.
 *
 * @author Jean-Sébastien Steux
 * @see CMSPortlet
 */
public class FaqPortlet extends CMSPortlet {

    /** View JSP path. */
    private static final String VIEW_JSP = "/WEB-INF/jsp/view.jsp";
    /** Error JSP path. */
    private static final String ERROR_JSP = "/WEB-INF/jsp/error.jsp";
    /** Admin JSP path. */
    private static final String ADMIN_JSP = "/WEB-INF/jsp/admin.jsp";


    /** Document DAO. */
    private DocumentDAO documentDAO;


    /**
     * Default constructor.
     */
    public FaqPortlet() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init(PortletConfig config) throws PortletException {
        super.init(config);

        // Document DAO
        this.documentDAO = DocumentDAO.getInstance();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws IOException, PortletException {
        // Action
        String action = request.getParameter(ActionRequest.ACTION_NAME);
        // Current window
        PortalWindow window = WindowFactory.getWindow(request);

        if ("admin".equals(request.getPortletMode().toString())) {
            if ("save".equals(action)) {
                // Save action

                // Path
                window.setProperty(Constants.WINDOW_PROP_URI, StringUtils.trimToNull(request.getParameter("path")));
            }

            response.setPortletMode(PortletMode.VIEW);
            response.setWindowState(WindowState.NORMAL);
        }
    }


    /**
     * Admin view display.
     *
     * @param request request
     * @param response response
     * @throws PortletException
     * @throws IOException
     */
    @RenderMode(name = "admin")
    public void doAdmin(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        // Current window
        PortalWindow window = WindowFactory.getWindow(request);

        // Path
        request.setAttribute("path", window.getProperty(Constants.WINDOW_PROP_URI));

        response.setContentType("text/html");
        this.getPortletContext().getRequestDispatcher(ADMIN_JSP).include(request, response);
    }


    /**
     * {@inheritDoc}
     * @throws IOException 
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        String errorMessageKey = null;

        try {
            // Nuxeo controller
            //NuxeoController nuxeoController = new NuxeoController(request, response, this.getPortletContext());
                        
            // Current window
        	PortalWindow window = WindowFactory.getWindow(request);

            // Path
            String path = request.getParameter("curItemPath");
            
            if (path == null) {

                path = window.getProperty(Constants.WINDOW_PROP_URI);
            }
            
            NuxeoDocumentContext documentContext = null;
            
            if(path != null) {
            	documentContext = NuxeoController.getDocumentContext(request, response, getPortletContext(), path);
            }
            else {
            	documentContext = NuxeoController.getDocumentContext(request, response, getPortletContext());
            }
            
            


            // Contextualization indicator
            if ("1".equals(window.getProperty("osivia.cms.contextualization"))) {
                request.setAttribute("contextualization", "1");
            }
            
            

            if (documentContext != null) {
                // Fetch
                Document document = documentContext.getDoc();
                
                NuxeoDocumentContext faqContext = null;
                Document faq;
                Document question;

                // Type
                String type = document.getType();

                if ("Question".equals(type)) {
                    // FAQ question
                    question = document;

                    // FAQ folder path
                    String faqPath = NuxeoController.getParentPath(document.getPath());

                    // Fetch FAQ folder
                    
                    faqContext = NuxeoController.getDocumentContext(request, response, getPortletContext(), faqPath);
                    
                    faq = faqContext.getDoc();
                    
                    
                } else if ("FaqFolder".equals(type)) {
                    // FAQ folder
                	faqContext = documentContext;
                    faq = document;
                    question = null;
                } else {
                    // Error : not a FAQ type
                    errorMessageKey = "ERROR_NOT_FAQ_TYPE";
                    faq = null;
                    question = null;
                }

                if (faq != null) {
                    // Get all questions
                    String faqPath = faq.getPath();
                    String faqLiveId = faqContext.getPublicationInfos(BasicPublicationInfos.class).getLiveId();
                    
                    NuxeoController nuxeoController = new NuxeoController(request, response, getPortletContext());
                    NuxeoQueryFilterContext queryContext = nuxeoController.getQueryFilterContextForPath(faqPath);
                    INuxeoCommand command = new FaqFetchChildrenCommand(queryContext, faqLiveId);
                    Documents questions = (Documents) nuxeoController.executeNuxeoCommand(command);

                    // For CMS generic functions (such as rich text conversions)
                    if (question != null) {
                        nuxeoController.setCurrentDoc(question);
                    } else {
                        nuxeoController.setCurrentDoc(faq);
                    }

                    // Breadcrumb
                    List<PortletPathItem> portletPath = new ArrayList<PortletPathItem>();
                    if (question != null) {
                        this.addPathItem(portletPath, question);
                    }
                    this.addPathItem(portletPath, faq);
                    request.setAttribute("osivia.portletPath", portletPath);

                    // Title
                    if (question != null) {
                        response.setTitle(question.getTitle());
                    } else {
                        response.setTitle(faq.getTitle());
                    }

                    // Menubar
                    nuxeoController.insertContentMenuBarItems();

                    // DAO
                    DocumentDTO faqDTO = this.documentDAO.toDTO(faq);
                    request.setAttribute("faq", faqDTO);

                    if (question != null) {
                        // DTO
                        DocumentDTO dto = this.documentDAO.toDTO(question);
                        this.generateAttachments(nuxeoController, question, dto);

                        // VO
                        Question vo = new Question();
                        BeanUtils.copyProperties(vo, dto);
                        vo.setMessage(nuxeoController.transformHTMLContent(question.getString("note:note")));

                        request.setAttribute("question", vo);
                    }

                    List<DocumentDTO> questionsDTO = new ArrayList<DocumentDTO>(questions.size());
                    for (Document doc : questions.list()) {
                        DocumentDTO dto = this.documentDAO.toDTO(doc);
                        questionsDTO.add(dto);
                    }
                    request.setAttribute("questions", questionsDTO);
                }
            } else {
                // Error : path undefined
                errorMessageKey = "ERROR_PATH_UNDEFINED";
            }
        } catch (NuxeoException e) {
            PortletErrorHandler.handleGenericErrors(response, e);
        } catch (IllegalAccessException e) {
        	throw new PortletException(e);
		} catch (InvocationTargetException e) {
			throw new PortletException(e);
		}

        response.setContentType("text/html");

        PortletRequestDispatcher dispatcher;
        if (errorMessageKey == null) {
            dispatcher = this.getPortletContext().getRequestDispatcher(VIEW_JSP);
        } else {
            request.setAttribute("errorKey", errorMessageKey);
            dispatcher = this.getPortletContext().getRequestDispatcher(ERROR_JSP);
        }

        dispatcher.include(request, response);
    }


    /**
     * Utility method used to add path item to list.
     *
     * @param portletPath portlet path list
     * @param document current document
     */
    private void addPathItem(List<PortletPathItem> portletPath, Document document) {
        Map<String, String> renderParams = new HashMap<String, String>();
        renderParams.put("curItemPath", document.getPath());

        PortletPathItem pathItem = new PortletPathItem(renderParams, document.getTitle());

        portletPath.add(0, pathItem);
    }


    /**
     * Generate document attachments.
     *
     * @param nuxeoController Nuxeo controller
     * @param document Nuxeo document
     * @param documentDTO document DTO
     */
    private void generateAttachments(NuxeoController nuxeoController, Document document, DocumentDTO documentDTO) {
        List<DocumentAttachmentDTO> attachments = documentDTO.getAttachments();
        PropertyList files = document.getProperties().getList("files:files");
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                PropertyMap map = files.getMap(i);

                DocumentAttachmentDTO attachment = new DocumentAttachmentDTO();

                // Attachment name
                String name = map.getString("filename");
                attachment.setName(name);

                // Attachement URL
                String url = nuxeoController.createAttachedFileLink(document.getPath(), String.valueOf(i));
                attachment.setUrl(url);

                attachments.add(attachment);
            }
        }
    }

}
