package org.osivia.services.statistics.portlet.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.dom4j.Element;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.statistics.IStatisticsService;
import org.osivia.portal.api.statistics.SpaceStatistics;
import org.osivia.portal.api.statistics.SpaceVisits;
import org.osivia.services.statistics.portlet.model.CreationsView;
import org.osivia.services.statistics.portlet.model.StatisticsForm;
import org.osivia.services.statistics.portlet.model.StatisticsWindowSettings;
import org.osivia.services.statistics.portlet.model.VisitsView;
import org.osivia.services.statistics.portlet.repository.StatisticsPortletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Statistics portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see IStatisticsPortletService
 */
@Service
public class StatisticsPortletServiceImpl implements StatisticsPortletService {

    /** History default number of days. */
    private static final int DEFAULT_DAYS_HISTORY = 30;
    /** History default number of months. */
    private static final int DEFAULT_MONTHS_HISTORY = 12;

    /** Red color. */
    private static final String RED = "rgb(255, 99, 132)";
    /** Blue color. */
    private static final String BLUE = "rgb(54, 162, 235)";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private StatisticsPortletRepository repository;
    
    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Statistics service. */
    @Autowired
    private IStatisticsService statisticsService;


    /**
     * Constructor.
     */
    public StatisticsPortletServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public StatisticsWindowSettings getWindowSettings(PortalControllerContext portalControllerContext) throws PortletException {
        return this.repository.getWindowSettings(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveWindowSettings(PortalControllerContext portalControllerContext, StatisticsWindowSettings windowSettings) throws PortletException {
        this.repository.saveWindowSettings(portalControllerContext, windowSettings);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public StatisticsForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        StatisticsForm form = this.applicationContext.getBean(StatisticsForm.class);
        form.setCreationsView(CreationsView.DEFAULT);
        form.setVisitsView(VisitsView.DEFAULT);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getCreations(PortalControllerContext portalControllerContext, CreationsView view) throws PortletException {
        // Window settings
        StatisticsWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        // Documents count by periods
        Map<String, Integer[]> periods = this.repository.getDocumentsCountsByPeriods(portalControllerContext, windowSettings);

        // Result
        JSONObject result = new JSONObject();
        result.put("type", "bar");
        result.put("options", this.getChartOptions(portalControllerContext));
        result.put("data", this.getCreationsChartData(portalControllerContext, periods, view));
        result.put("table", this.getCreationsTableBody(portalControllerContext, periods));

        return result;
    }


    /**
     * Get creations chart JSON data.
     * 
     * @param portalControllerContext portal controller
     * @param periods periods
     * @param view creations view
     * @return JSON
     * @throws PortletException
     */
    private JSONObject getCreationsChartData(PortalControllerContext portalControllerContext, Map<String, Integer[]> periods, CreationsView view)
            throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);

        int index;
        if (CreationsView.DIFFERENTIAL.equals(view)) {
            index = 0;
        } else {
            index = 1;
        }

        // Labels
        JSONArray labels = new JSONArray();
        // Values
        JSONArray values = new JSONArray();

        for (Entry<String, Integer[]> period : periods.entrySet()) {
            labels.add(period.getKey());
            values.add(period.getValue()[index]);
        }

        // Dataset
        JSONObject dataset = new JSONObject();
        dataset.put("label", bundle.getString(view.getKey()));
        dataset.put("borderColor", BLUE);
        dataset.put("backgroundColor", BLUE);
        dataset.put("data", values);

        // Datasets
        JSONArray datasets = new JSONArray();
        datasets.add(dataset);

        // Data
        JSONObject data = new JSONObject();
        data.put("labels", labels);
        data.put("datasets", datasets);

        return data;
    }


    /**
     * Get creations table body HTML content.
     * 
     * @param portalControllerContext portal controller context
     * @param periods periods
     * @return HTML content
     */
    private String getCreationsTableBody(PortalControllerContext portalControllerContext, Map<String, Integer[]> periods) {
        // Table body
        Element tbody = DOM4JUtils.generateElement("tbody", null, StringUtils.EMPTY);

        for (Entry<String, Integer[]> period : periods.entrySet()) {
            // Row
            Element tr = DOM4JUtils.generateElement("tr", null, null);
            tbody.add(tr);

            // Label
            Element label = DOM4JUtils.generateElement("td", null, period.getKey());
            tr.add(label);

            // Values
            for (int i = 0; i < 2; i++) {
                Element value = DOM4JUtils.generateElement("td", null, String.valueOf(period.getValue()[i]));
                tr.add(value);
            }
        }

        return DOM4JUtils.writeCompact(tbody);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject getVisits(PortalControllerContext portalControllerContext, VisitsView view) throws PortletException {
        // Space path
        String path = this.repository.getSpacePath(portalControllerContext);

        // Space statistics
        SpaceStatistics statistics;
        try {
            statistics = this.statisticsService.getSpaceStatistics(portalControllerContext, path);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Result
        JSONObject result = new JSONObject();
        result.put("type", "line");
        result.put("options", this.getChartOptions(portalControllerContext));
        if (statistics != null) {
            result.put("data", this.getVisitsChartData(portalControllerContext, view, statistics));
            result.put("table", this.getVisitsTableBody(portalControllerContext, view, statistics));
        }

        return result;
    }


    /**
     * Get visits chart JSON data.
     * 
     * @param portalControllerContext portal controller context
     * @param view visits view
     * @param statistics space statistics
     * @return JSON
     * @throws PortletException
     */
    private JSONObject getVisitsChartData(PortalControllerContext portalControllerContext, VisitsView view, SpaceStatistics statistics)
            throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);
        
        // Window settings
        StatisticsWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        // Calendar field
        int field;
        // History size
        int historySize;
        // Date formats
        DateFormat labelFormat;
        DateFormat subLabelFormat;
        // Periods
        Map<Date, SpaceVisits> periods;
        
        if (VisitsView.MONTHS.equals(view)) {
            field = Calendar.MONTH;
            historySize = windowSettings.getVisitsMonths();
            if (historySize < 1) {
                historySize = DEFAULT_MONTHS_HISTORY;
            }
            labelFormat = new SimpleDateFormat("MMM", locale);
            subLabelFormat = new SimpleDateFormat("yyyy", locale);
            periods = statistics.getHistorizedMonthsVisits();
        } else {
            field = Calendar.DAY_OF_MONTH;
            historySize = windowSettings.getVisitsDays();
            if (historySize < 1) {
                historySize = DEFAULT_DAYS_HISTORY;
            }
            labelFormat = new SimpleDateFormat("d", locale);
            subLabelFormat = new SimpleDateFormat("MMM", locale);
            periods = statistics.getHistorizedDaysVisits();
        }
        
        // Current period
        Date currentPeriod = DateUtils.truncate(new Date(), field);

        // JSON arrays
        JSONArray labels = new JSONArray();
        JSONArray hitsData = new JSONArray();
        JSONArray uniqueVisitorsData = new JSONArray();

        for (int i = 1; i <= historySize; i++) {
            Date date;
            SpaceVisits visits;
            if (i == historySize) {
                date = currentPeriod;

                if (VisitsView.MONTHS.equals(view)) {
                    visits = statistics.getCurrentMonthVisits();
                } else {
                    visits = statistics.getCurrentDayVisits();
                }
            } else {
                if (VisitsView.MONTHS.equals(view)) {
                    date = DateUtils.addMonths(currentPeriod, i - historySize);
                } else {
                    date = DateUtils.addDays(currentPeriod, i - historySize);
                }

                visits = periods.get(date);
            }

            // Calendar
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            // Display sub label indicator
            boolean displaySubLabel = (i == 1) || (VisitsView.DAYS.equals(view) && calendar.get(field) == 1)
                    || (VisitsView.MONTHS.equals(view) && calendar.get(field) == 0);

            // Label
            String label = labelFormat.format(date);
            if (displaySubLabel) {
                JSONArray multilineLabel = new JSONArray();
                multilineLabel.add(label);
                multilineLabel.add(subLabelFormat.format(date));
                labels.add(multilineLabel);
            } else {
                labels.add(label);
            }

            // Hits && unique visitors
            if (visits == null) {
                hitsData.add(0);
                uniqueVisitorsData.add(0);
            } else {
                hitsData.add(visits.getHits());
                if (i == historySize) {
                    uniqueVisitorsData.add(visits.getVisitors().size() + visits.getAnonymousVisitors());
                } else {
                    uniqueVisitorsData.add(visits.getUniqueVisitors());
                }
            }
        }


        // Hits
        JSONObject hits = new JSONObject();
        hits.put("label", bundle.getString("VISITS_HITS"));
        hits.put("borderColor", RED);
        hits.put("backgroundColor", RED);
        hits.put("fill", false);
        hits.put("lineTension", 0);
        hits.put("data", hitsData);

        // Unique visitors
        JSONObject uniqueVisitors = new JSONObject();
        uniqueVisitors.put("label", bundle.getString("VISITS_UNIQUE_VISITORS"));
        uniqueVisitors.put("borderColor", BLUE);
        uniqueVisitors.put("backgroundColor", BLUE);
        uniqueVisitors.put("fill", false);
        uniqueVisitors.put("lineTension", 0);
        uniqueVisitors.put("data", uniqueVisitorsData);

        // Datasets
        JSONArray datasets = new JSONArray();
        datasets.add(hits);
        datasets.add(uniqueVisitors);

        // Data
        JSONObject data = new JSONObject();
        data.put("labels", labels);
        data.put("datasets", datasets);

        return data;
    }


    /**
     * Get visits table body HTML content.
     * 
     * @param portalControllerContext portal controller context
     * @param period period
     * @param statistics space statistics
     * @return HTML content
     * @throws PortletException
     */
    private String getVisitsTableBody(PortalControllerContext portalControllerContext, VisitsView view, SpaceStatistics statistics) throws PortletException {
        // Locale
        Locale locale = portalControllerContext.getRequest().getLocale();

        // Window settings
        StatisticsWindowSettings windowSettings = this.getWindowSettings(portalControllerContext);

        // Calendar field
        int field;
        // History size
        int historySize;
        // Date format
        DateFormat labelFormat;
        // Periods
        Map<Date, SpaceVisits> periods;

        if (VisitsView.MONTHS.equals(view)) {
            field = Calendar.MONTH;
            historySize = windowSettings.getVisitsMonths();
            if (historySize < 1) {
                historySize = DEFAULT_MONTHS_HISTORY;
            }
            labelFormat = new SimpleDateFormat("MMMM yyyy", locale);
            periods = statistics.getHistorizedMonthsVisits();
        } else {
            field = Calendar.DAY_OF_MONTH;
            historySize = windowSettings.getVisitsDays();
            if (historySize < 1) {
                historySize = DEFAULT_DAYS_HISTORY;
            }
            labelFormat = DateFormat.getDateInstance(DateFormat.FULL, locale);
            periods = statistics.getHistorizedDaysVisits();
        }

        // Current period
        Date currentPeriod = DateUtils.truncate(new Date(), field);
        
        
        // Table body
        Element tbody = DOM4JUtils.generateElement("tbody", null, StringUtils.EMPTY);
        
        for (int i = 1; i <= historySize; i++) {
            Date date;
            SpaceVisits visits;
            if (i == historySize) {
                date = currentPeriod;

                if (VisitsView.MONTHS.equals(view)) {
                    visits = statistics.getCurrentMonthVisits();
                } else {
                    visits = statistics.getCurrentDayVisits();
                }
            } else {
                if (VisitsView.MONTHS.equals(view)) {
                    date = DateUtils.addMonths(currentPeriod, i - historySize);
                } else {
                    date = DateUtils.addDays(currentPeriod, i - historySize);
                }

                visits = periods.get(date);
            }
            
            // Hits & unique visitors
            String hitsValue;
            String uniqueVisitorsValue;
            if (visits == null) {
                hitsValue = String.valueOf(0);
                uniqueVisitorsValue = String.valueOf(0);
            } else {
                hitsValue = String.valueOf(visits.getHits());
                if (i == historySize) {
                    uniqueVisitorsValue = String.valueOf(visits.getVisitors().size() + visits.getAnonymousVisitors());
                } else {
                    uniqueVisitorsValue = String.valueOf(visits.getUniqueVisitors());
                }
            }


            // Row
            Element tr = DOM4JUtils.generateElement("tr", null, null);
            tbody.add(tr);

            // Label
            Element label = DOM4JUtils.generateElement("td", null, labelFormat.format(date));
            tr.add(label);

            // Hits
            Element hits = DOM4JUtils.generateElement("td", null, hitsValue);
            tr.add(hits);

            // Unique visitors
            Element uniqueVisitors = DOM4JUtils.generateElement("td", null, uniqueVisitorsValue);
            tr.add(uniqueVisitors);
        }
        
        return DOM4JUtils.writeCompact(tbody);
    }


    /**
     * Get chart JSON options.
     * 
     * @param portalControllerContext portal controller context
     * @return JSON
     */
    private JSONObject getChartOptions(PortalControllerContext portalControllerContext) {
        // Ticks
        JSONObject ticks = new JSONObject();
        ticks.put("min", 0);

        // Y axis #1
        JSONObject yAxis1 = new JSONObject();
        yAxis1.put("id", "y-axis-1");
        yAxis1.put("type", "linear");
        yAxis1.put("position", "left");
        yAxis1.put("ticks", ticks);

        // Y axes
        JSONArray yAxes = new JSONArray();
        yAxes.add(yAxis1);

        // Scales
        JSONObject scales = new JSONObject();
        scales.put("yAxes", yAxes);


        // Tooltips
        JSONObject tooltips = new JSONObject();
        tooltips.put("mode", "index");
        tooltips.put("intersect", false);


        // Options
        JSONObject options = new JSONObject();
        options.put("maintainAspectRatio", false);
        options.put("scales", scales);
        options.put("tooltips", tooltips);

        return options;
    }

}
