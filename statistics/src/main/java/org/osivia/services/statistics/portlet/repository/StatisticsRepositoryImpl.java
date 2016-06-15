package org.osivia.services.statistics.portlet.repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.statistics.portlet.model.StatisticsConfiguration;
import org.osivia.services.statistics.portlet.model.StatisticsVersion;
import org.osivia.services.statistics.portlet.model.StatisticsView;
import org.osivia.services.statistics.portlet.util.NXQLFormater;
import org.springframework.stereotype.Repository;

import bsh.EvalError;
import bsh.Interpreter;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Statistics repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see IStatisticsRepository
 */
@Repository
public class StatisticsRepositoryImpl implements IStatisticsRepository {

    /** Periods number window property name. */
    private static final String PERIODS_NUMBER_WINDOW_PROPERTY = "osivia.statistics.number";
    /** Request window property name. */
    private static final String REQUEST_WINDOW_PROPERTY = "osivia.statistics.request";
    /** Version window property name. */
    private static final String VERSION_WINDOW_PROPERTY = "osivia.statistics.version";


    /** Bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public StatisticsRepositoryImpl() {
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
    public StatisticsConfiguration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Configuration
        StatisticsConfiguration configuration = new StatisticsConfiguration();
        configuration.setNumber(NumberUtils.toInt(window.getProperty(PERIODS_NUMBER_WINDOW_PROPERTY)));
        configuration.setRequest(window.getProperty(REQUEST_WINDOW_PROPERTY));
        configuration.setVersion(StatisticsVersion.fromName(window.getProperty(VERSION_WINDOW_PROPERTY)));

        return configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveConfiguration(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        window.setProperty(PERIODS_NUMBER_WINDOW_PROPERTY, String.valueOf(configuration.getNumber()));
        window.setProperty(REQUEST_WINDOW_PROPERTY, StringUtils.trimToNull(configuration.getRequest()));
        window.setProperty(VERSION_WINDOW_PROPERTY, configuration.getVersion().getName());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Integer[]> getDocumentsCountsByPeriods(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration)
            throws PortletException {
        // Results
        Map<String, Integer[]> results = new LinkedHashMap<String, Integer[]>(configuration.getNumber());

        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Periods
        List<Period> periods = this.getPeriods(Calendar.MONTH, configuration.getNumber() + 1, locale);
        Iterator<Period> iterator = periods.iterator();
        Period firstPeriod = iterator.next();
        Period period = iterator.next();

        // Counts
        int periodCount = 0;
        int aggregateCount = 0;

        // Documents
        List<Document> documents = this.getDocuments(portalControllerContext, configuration);
        for (Document document : documents) {
            // Date
            Date date = document.getDate("dc:created");
            if (date != null) {
                if (firstPeriod != null) {
                    if (firstPeriod.startDate.after(date)) {
                        aggregateCount++;
                        continue;
                    } else {
                        firstPeriod = null;
                    }
                }

                while (period.startDate.before(date) && iterator.hasNext()) {
                    Integer[] counts = new Integer[2];
                    counts[0] = periodCount;
                    counts[1] = aggregateCount;
                    results.put(period.name, counts);
                    period = iterator.next();
                    
                    if (StatisticsView.DIFFERENTIAL.equals(configuration.getView())) {
                        periodCount = 0;
                    }
                }

                periodCount++;
                aggregateCount++;
            }
        }

        // Complete periods
        while (iterator.hasNext()) {
            Integer[] counts = new Integer[2];
            counts[0] = periodCount;
            counts[1] = aggregateCount;
            results.put(period.name, counts);
            period = iterator.next();
        }

        Integer[] counts = new Integer[2];
        counts[0] = periodCount;
        counts[1] = aggregateCount;
        results.put(period.name, counts);

        return results;
    }


    /**
     * Get Nuxeo controller.
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        // Request
        PortletRequest request = portalControllerContext.getRequest();
        // Response
        PortletResponse response = portalControllerContext.getResponse();
        // Portlet context
        PortletContext portletContext = portalControllerContext.getPortletCtx();

        return new NuxeoController(request, response, portletContext);
    }


    /**
     * Get periods.
     *
     * @param type periods type
     * @param number number of periods
     * @param locale current user locale
     * @return periods
     */
    private List<Period> getPeriods(int type, int number, Locale locale) {
        Period[] periods = new Period[number];

        // Calendar
        Calendar calendar = GregorianCalendar.getInstance(locale);

        // Truncate calendar
        if (Calendar.WEEK_OF_YEAR == type) {
            int firstDayOfWeek = calendar.getFirstDayOfWeek();
            while (calendar.get(Calendar.DAY_OF_WEEK) != firstDayOfWeek) {
                calendar.add(Calendar.DAY_OF_MONTH, -1);
            }
            calendar = DateUtils.truncate(calendar, Calendar.DAY_OF_MONTH);
        } else {
            calendar = DateUtils.truncate(calendar, type);
        }

        // Date format
        DateFormat dateFormat;
        if (Calendar.WEEK_OF_YEAR == type) {
            Bundle bundle = this.bundleFactory.getBundle(locale);
            dateFormat = new SimpleDateFormat("'" + bundle.getString("WEEK") + "' w", locale);
        } else if (Calendar.MONTH == type) {
            dateFormat = new SimpleDateFormat("MMM yyyy");
        } else {
            dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, locale);
        }

        for (int i = 0; i < number; i++) {
            // Date
            Date date = calendar.getTime();

            // Period
            Period period = new Period();
            period.name = dateFormat.format(date);
            period.startDate = date;
            periods[i] = period;

            // Decrements calendar
            calendar.add(type, -1);
        }

        CollectionUtils.reverseArray(periods);

        return Arrays.asList(periods);
    }


    /**
     * Get Nuxeo documents.
     *
     * @param portalControllerContext portal controller context
     * @param configuration statistics configuration
     * @return documents
     * @throws PortletException
     */
    private List<Document> getDocuments(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration) throws PortletException {
        List<Document> results;

        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_PORTLET_CONTEXT);

        // Request
        String request;
        try {
            request = this.beanShellInterpretation(nuxeoController, configuration);
        } catch (EvalError e) {
            throw new PortletException(e);
        }

        if (request != null) {
            // Filter
            NuxeoQueryFilterContext filter = configuration.getVersion().getFilter();

            // Nuxeo command
            INuxeoCommand command = new ListDocumentsCommand(request, filter);
            Documents documents = (Documents) nuxeoController.executeNuxeoCommand(command);

            results = documents.list();
        } else {
            results = new ArrayList<Document>(0);
        }

        return results;
    }


    /**
     * BeanShell interpretation.
     *
     * @param nuxeoController Nuxeo controller
     * @param configuration statistics configuration
     * @return request
     * @throws EvalError
     */
    private String beanShellInterpretation(NuxeoController nuxeoController, StatisticsConfiguration configuration) throws EvalError {
        String result = null;

        // Request
        String request = configuration.getRequest();

        if (!StringUtils.contains(request, "basePath") || (nuxeoController.getBasePath() != null)) {
            // Portlet request
            PortletRequest portletRequest = nuxeoController.getRequest();

            // BeanShell interpreter
            Interpreter interpreter = new Interpreter();
            interpreter.set("params", PageSelectors.decodeProperties(portletRequest.getParameter("selectors")));
            interpreter.set("basePath", nuxeoController.getBasePath());
            interpreter.set("NXQLFormater", new NXQLFormater());

            result = (String) interpreter.eval(request);
        }

        return result;
    }


    /**
     * Period inner-class.
     *
     * @author Cédric Krommenhoek
     */
    private class Period {

        /** Period name. */
        private String name;
        /** Period start date. */
        private Date startDate;


        /**
         * Constructor.
         */
        public Period() {
            super();
        }

    }

}
