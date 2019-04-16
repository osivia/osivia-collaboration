package org.osivia.services.calendar.view.portlet.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtEnd;
import net.fortuna.ical4j.model.property.LastModified;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;

/**
 * Calendar integration portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CalendarIntegrationService
 */
@Service
public class CalendarIntegrationServiceImpl implements CalendarIntegrationService {

    /** Portlet repository. */
    @Autowired
    private CalendarViewRepository repository;


    private final ProdId prodId;


    /**
     * Constructor.
     */
    public CalendarIntegrationServiceImpl() {
        super();

        // ProdId
        this.prodId = new ProdId("-//OSIVIA Portal//4.7//FR");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void integrate(PortalControllerContext portalControllerContext, OutputStream outputStream, String format) throws PortletException, IOException {
        // Calendar
        Calendar calendar = new Calendar();
        calendar.getProperties().add(this.prodId);
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);


        // Events
        List<Event> events = this.repository.getEvents(portalControllerContext, null, null);

        if (CollectionUtils.isNotEmpty(events)) {
            for (Event event : events) {
                VEvent vevent = createVEvent(portalControllerContext, event);
                if (vevent != null) {
                    calendar.getComponents().add(vevent);
                }
            }
        }
        
        CalendarOutputter outputter = new CalendarOutputter();
        try {
            outputter.output(calendar, outputStream);
        } catch (ValidationException e) {
            throw new PortletException(e);
        }
    }


    /**
     * Create calendar event.
     * 
     * @param portalControllerContext portal controller context
     * @param event event
     * @throws PortletException
     * @throws IOException
     */
    protected VEvent createVEvent(PortalControllerContext portalControllerContext, Event event) throws PortletException, IOException {
        VEvent vevent;

        // Start
        Date start;
        if (event.getStartDate() == null) {
            start = null;
        } else if (event.isAllDay()) {
            start = new Date(event.getStartDate());
        } else {
            DateTime dateTime = new DateTime(event.getStartDate());
            dateTime.setUtc(true);
            start = dateTime;
        }

        // Summary
        String summary = event.getTitle();

        if ((start != null) && StringUtils.isNotEmpty(summary)) {
            vevent = new VEvent(start, summary);
            PropertyList properties = vevent.getProperties();

            // End
            if (event.getEndDate() != null) {
                DtEnd end;
                if (event.isAllDay()) {
                    end = new DtEnd(new Date(event.getEndDate()));
                } else {
                    end = new DtEnd(new DateTime(event.getEndDate()));
                    end.setUtc(true);
                }
                properties.add(end);
            }

            // UID
            if (StringUtils.isNotEmpty(event.getId())) {
                Uid uid = new Uid(event.getId());
                properties.add(uid);
            }

            // Last modified
            if (event.getLastModified() != null) {
                LastModified lastModified = new LastModified(new DateTime(event.getLastModified()));
                properties.add(lastModified);
            }

            // Location
            if (StringUtils.isNotBlank(event.getLocation())) {
                Location location = new Location(event.getLocation());
                properties.add(location);
            }

            // Description
            if (StringUtils.isNotBlank(event.getDescription())) {
                Description description = new Description(event.getDescription());
                properties.add(description);
            }
        } else {
            vevent = null;
        }

        return vevent;
    }

}
