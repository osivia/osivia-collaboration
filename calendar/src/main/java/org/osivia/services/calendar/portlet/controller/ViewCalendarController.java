package org.osivia.services.calendar.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.portlet.model.events.DailyCalendarEventsData;
import org.osivia.services.calendar.portlet.model.events.DailyEvent;
import org.osivia.services.calendar.portlet.model.events.EventsData;
import org.osivia.services.calendar.portlet.service.ICalendarService;
import org.osivia.services.calendar.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * View calendar controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletContextAware
 * @see PortletConfigAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewCalendarController extends CMSPortlet implements PortletContextAware, PortletConfigAware {

    /** Error JSP path. */
    private static final String ERROR_PATH = "calendar/error";

    /** Calendar data request attribute. */
    private static final String CALENDAR_DATA_ATTRIBUTE = "calendarData";
    /** Events data request attribute. */
    private static final String EVENTS_DATA_ATTRIBUTE = "eventsData";
    /** Selected date request attribute. */
    private static final String SELECTED_DATE_ATTRIBUTE = ICalendarService.SELECTED_DATE_PARAMETER;
    /** Error message request attribute. */
    private static final String ERROR_MESSAGE_ATTRIBUTE = "message";


    /** Calendar service. */
    @Autowired
    private ICalendarService calendarService;

    /** Portlet context. */
    private PortletContext portletContext;
    /** Portlet config. */
    private PortletConfig portletConfig;


    /**
     * Default constructor.
     */
    public ViewCalendarController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * View calendar render mapping
     *
     * @param request render request
     * @param response render response
     * @param selectedDate request parameter, may be null
     * @return view path
     * @throws PortletException
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response,
            @RequestParam(value = ICalendarService.SELECTED_DATE_PARAMETER, required = false) String selectedDate,
            @RequestParam(value = ICalendarService.PERIOD_TYPE_PARAMETER, required = false) String periodType,
            @ModelAttribute CalendarData calendarData)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        if (null != periodType) calendarData.setPeriodType(PeriodTypes.fromName(periodType));
        
        // Selected date
        request.setAttribute(SELECTED_DATE_ATTRIBUTE, selectedDate);

        // Events data
        request.setAttribute(EVENTS_DATA_ATTRIBUTE, this.calendarService.getEventsData(portalControllerContext, calendarData));

        // Title
        String title = this.calendarService.getTitle(portalControllerContext);
        if (StringUtils.isNotBlank(title)) {
            response.setTitle(title);
        }

        // Portlet URI
        this.calendarService.definePortletUri(portalControllerContext);

        return this.calendarService.getViewPath(portalControllerContext, calendarData);
    }


    /**
     * Exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @param exception current exception
     * @return error path
     */
    @ExceptionHandler(value = Exception.class)
    public String handleException(PortletRequest request, PortletResponse response, Exception exception) {
        // Error message
        request.setAttribute(ERROR_MESSAGE_ATTRIBUTE, exception.getMessage());

        return ERROR_PATH;
    }

    
    /**
     * Chargement des données du scheduler
     *
     * @param request resource request
     * @param response resource response
     * @param path parent path, may be null for root node
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping(value = "initSchedulerData")
    public void initSchedulerData(ResourceRequest request, ResourceResponse response, @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE) CalendarData calendarData,
    		@RequestParam(value = ICalendarService.PERIOD_TYPE_PARAMETER, required = false) String periodTypeName,
    		@RequestParam(value = ICalendarService.SELECTED_DATE_PARAMETER, required = false) Date selectedDate)
            throws PortletException, IOException{
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        dataLoading(response, portalControllerContext, calendarData);

    }
    
    /**
     * Chargement des données de la période précédente
     *
     * @param request resource request
     * @param response resource response
     * @param path parent path, may be null for root node
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping(value = "loadData")
    public void loadData(ResourceRequest request, ResourceResponse response, @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE) CalendarData calendarData,
    		@RequestParam("start") Date startDate,
    		@RequestParam("end") Date endDate)
            throws PortletException, IOException{
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        //@TODO Changer la période "week". Il ne faut plus que 2 calendarData: un pour la vue calendrier et un pour la vue planning
        //CalendarData calendarData = this.calendarService.getCalendarData(portalControllerContext, "week");
        calendarData.setStartDate(startDate);
        calendarData.setEndDate(endDate);
        
        //this.calendarService.selectPreviousPeriod(portalControllerContext, calendarData);
        
        dataLoading(response, portalControllerContext, calendarData);
    }
    
    /**
     * Chargement des données par rapport aux date de début et de fin du calendarData
     * @param response
     * @param portalControllerContext
     * @param calendarData
     * @throws PortletException
     * @throws IOException
     */
    private void dataLoading(ResourceResponse response, PortalControllerContext portalControllerContext, CalendarData calendarData) 
    		throws PortletException, IOException
    {
      	EventsData eventsData = this.calendarService.getEventsData(portalControllerContext, calendarData);
        List<DailyEvent> listEvent = ((DailyCalendarEventsData) eventsData).getEvents();
        
    	JSONArray array = new JSONArray();
        JSONObject object = null;
        Iterator<DailyEvent> iterator = listEvent.iterator();
        DailyEvent event = null;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        while (iterator.hasNext())
        {
        	event = iterator.next();
        	object = new JSONObject();
            object.put("text", event.getTitle());
            object.put("start_date", formater.format(event.getStartDate()));
            object.put("end_date", formater.format(event.getEndDate()));
            array.add(object);
        }
        

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(array.toString());
        printWriter.close();
    }
    
    /**
     * Get calendar data.
     *
     * @param request portlet request
     * @param response portlet response
     * @param periodTypeName period type name request parameter, may be null
     * @return calendar data
     * @throws PortletException
     */
    @ModelAttribute(value = CALENDAR_DATA_ATTRIBUTE)
    public CalendarData getCalendarData(PortletRequest request, PortletResponse response, @RequestParam(value = ICalendarService.PERIOD_TYPE_PARAMETER,
            required = false) String periodTypeName) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.calendarService.getCalendarData(portalControllerContext, periodTypeName);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }

}
