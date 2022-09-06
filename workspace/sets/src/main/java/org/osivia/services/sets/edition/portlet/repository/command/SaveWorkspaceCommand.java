package org.osivia.services.sets.edition.portlet.repository.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Save workspace command
 * @author Julien Barberet
 * @see INuxeoCommand
 */
public class SaveWorkspaceCommand implements INuxeoCommand {

	private static final String LIST_WEBID_PROPERTY= "webids";
	private static final String NAME_PROPERTY= "name";
	private static final String SETS_PROPERTY = "sets:sets";
	
	/** List of Set */
	private List<String> listSet;
	
	/** Workspace */
	private Document workspace;
	/** Sets id */
	private String setsId;
	
	/** 
	 * Constructor
	 * @param workspace
	 * @param listPinSet
	 */
    public SaveWorkspaceCommand(Document workspace, String setsId, List<String> listPinSet) {
		super();
		this.workspace = workspace;
		this.listSet = listPinSet;
		this.setsId = setsId;
	}

	/**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
    	// Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        DocRef docRef = new DocRef(workspace.getId());

        PropertyList list = (PropertyList) workspace.getProperties().get(SETS_PROPERTY);
        
        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();
        boolean quickAccessListFind = false;
		if (list != null && list.list().size() >0)
		{
			Map<String, Object> mapSet;
			PropertyList propertyListSet;
			for (Object map : list.list())
			{
				mapSet = new HashMap<>();
				mapSet.put(NAME_PROPERTY, ((PropertyMap) map).getString(NAME_PROPERTY));
				if (StringUtils.equals(setsId, ((PropertyMap) map).getString(NAME_PROPERTY)))
				{
					mapSet.put(LIST_WEBID_PROPERTY, listSet);
					quickAccessListFind = true;
				} else
				{
					
					propertyListSet = ((PropertyMap) map).getList(LIST_WEBID_PROPERTY);
					if (propertyListSet != null)
					{
						mapSet.put(LIST_WEBID_PROPERTY, propertyListSet.list());
					} else
					{
						mapSet.put(LIST_WEBID_PROPERTY, "");
					}
				}
				arrayList.add(mapSet);
			}
		}
		//If the setsId set does not exist, we add it to the arrayList
		if (!quickAccessListFind && listSet.size() >0)
		{
			Map<String, Object> mapSet = new HashMap<>();
			mapSet.put(NAME_PROPERTY, setsId);
			mapSet.put(LIST_WEBID_PROPERTY, listSet);
			arrayList.add(mapSet);
		}
		PropertyMap sets = new PropertyMap();
		sets.set(SETS_PROPERTY, convertToJson(arrayList));

        // Update
        workspace = documentService.update(docRef, sets, false);
        
        return workspace;
    }
    
	/**
	 * Convert object to Json
	 * @param object
	 * @return
	 * @throws IOException
	 */
	private String convertToJson(Object object) throws IOException
	{
		// JSON object writer
		ObjectMapper mapper = new ObjectMapper();
		ObjectWriter writer = mapper.writer();

		return writer.writeValueAsString(object);
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "Sets/" + (this.workspace == null? "" : this.workspace.getPath());
    }
}
