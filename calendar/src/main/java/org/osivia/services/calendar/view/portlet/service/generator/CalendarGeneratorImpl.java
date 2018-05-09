package org.osivia.services.calendar.view.portlet.service.generator;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.time.DateUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.services.calendar.view.portlet.model.calendar.CalendarData;
import org.osivia.services.calendar.view.portlet.model.events.Event;
import org.osivia.services.calendar.view.portlet.model.events.EventsData;
import org.osivia.services.calendar.view.portlet.repository.CalendarViewRepository;
import org.osivia.services.calendar.view.portlet.service.CalendarViewService;
import org.osivia.services.calendar.view.portlet.utils.PeriodTypes;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Calendar generator implementation super-class.
 *
 * @author Cédric Krommenhoek
 * @see ICalendarGenerator
 */
public abstract class CalendarGeneratorImpl implements ICalendarGenerator {

    /** Planning compact maximum number of days to display. */
    protected static final int PLANNING_COMPACT_MAX = 5;


    /** Calendar repository. */
    @Autowired
    private CalendarViewRepository calendarRepository;

    /** Bundle factory. */
    protected final IBundleFactory bundleFactory;

    protected PeriodTypes periodType;

    /**
     * Constructor.
     */
    public CalendarGeneratorImpl() {
        super();

        // Bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PeriodTypes getPeriodType() throws PortletException {
        return periodType;
    }

    @Override
    public void setPeriodType(PeriodTypes period) throws PortletException {
    	periodType = period;
    	
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarData generateCalendarData(PortalControllerContext portalControllerContext, PeriodTypes periodType) throws PortletException {
        CalendarData calendarData = new CalendarData();
        // Period type
        calendarData.setPeriodType(periodType);
        // Generator
        calendarData.setGenerator(this);
        // Dates
        this.fillCalendarDates(portalControllerContext, calendarData, null);

        return calendarData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateCalendarData(PortalControllerContext portalControllerContext, CalendarData calendarData, Date selectedDate) throws PortletException {
        // Dates
        this.fillCalendarDates(portalControllerContext, calendarData, selectedDate);
    }



    /**
     * Fill calendar dates.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @param forcedSelectedDate forced selected date, may be null
     * @throws PortletException
     */
    protected void fillCalendarDates(PortalControllerContext portalControllerContext, CalendarData calendarData, Date forcedSelectedDate) throws PortletException {
        // Period type
        PeriodTypes periodType = calendarData.getPeriodType();

        // Selected date
        Date selectedDate = forcedSelectedDate;
        if (selectedDate == null) {
            // Parameterized date
            selectedDate = this.getSelectedDate(portalControllerContext, periodType);
        }
        calendarData.setSelectedDate(selectedDate);

        // Start date
        Date startDate = this.getStartDate(portalControllerContext, periodType, selectedDate);
        calendarData.setStartDate(startDate);
    }


    /**
     * Get selected date.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @return selected date
     */
    protected Date getSelectedDate(PortalControllerContext portalControllerContext, PeriodTypes periodType) {
        Date selectedDate;

        if (PeriodTypes.PLANNING.equals(periodType)) {
            selectedDate = new Date();
        } else {
            String parameter = portalControllerContext.getRequest().getParameter(CalendarViewService.DATE_PARAMETER);
            if (parameter == null) {
                selectedDate = new Date();
            } else {
                try {
                    selectedDate = CalendarViewService.SELECTED_DATE_FORMAT.parse(parameter);
                } catch (ParseException e) {
                    selectedDate = new Date();
                }
            }
        }

        return DateUtils.truncate(selectedDate, Calendar.DAY_OF_MONTH);
    }


    /**
     * Get start date.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @param selectedDate selected date
     * @return start date
     */
    protected Date getStartDate(PortalControllerContext portalControllerContext, PeriodTypes periodType, Date selectedDate) {
        Calendar calendar = GregorianCalendar.getInstance(portalControllerContext.getRequest().getLocale());
        calendar.setTime(selectedDate);
        if (!PeriodTypes.PLANNING.equals(periodType)) {
            // Set first day of week
            calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
        }
        return calendar.getTime();
    }


    /**
     * Get end date.
     *
     * @param portalControllerContext portal controller context
     * @param periodType period type
     * @param selectedDate selected date
     * @param startDate start date
     * @return end date
     */
    protected Date getEndDate(PortalControllerContext portalControllerContext, PeriodTypes periodType, Date selectedDate, Date startDate) {
        // End date
        Date endDate;
        
        if (PeriodTypes.PLANNING.equals(periodType)) {
            endDate = null;
        } else {
            Calendar calendar = GregorianCalendar.getInstance(portalControllerContext.getRequest().getLocale());
            calendar.setTime(startDate);
            calendar.add(periodType.getField(), 1);
            calendar.add(Calendar.MILLISECOND, -1);
            endDate = calendar.getTime();
        }
        
        return endDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventsData generateEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData) throws PortletException {
    	
    	// Events
        List<Event> events = this.calendarRepository.getEvents(portalControllerContext, calendarData.getStartDate(), calendarData.getEndDate());

        // Events data
        EventsData eventsData;
        if (events == null) {
            eventsData = null;
        } else {
            eventsData = this.generateSpecializedEventsData(portalControllerContext, calendarData, events);
        }

        return eventsData;
    }
    


    /**
     * Generate specialized events data.
     *
     * @param portalControllerContext portal controller context
     * @param calendarData calendar data
     * @param events events
     * @return specialized events data
     * @throws PortletException
     */
    protected abstract EventsData generateSpecializedEventsData(PortalControllerContext portalControllerContext, CalendarData calendarData, List<Event> events)
            throws PortletException;


    /**
     * Getter for bundleFactory.
     *
     * @return the bundleFactory
     */
    public IBundleFactory getBundleFactory() {
        return this.bundleFactory;
    }

}
