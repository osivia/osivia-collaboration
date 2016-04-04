package org.osivia.services.statistics.service;

import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.services.statistics.model.StatisticsConfiguration;
import org.osivia.services.statistics.model.StatisticsView;
import org.osivia.services.statistics.repository.IStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Statistics service implementation.
 *
 * @author Cédric Krommenhoek
 * @see IStatisticsService
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService {

    /** Statistics repository. */
    @Autowired
    private IStatisticsRepository repository;


    /**
     * Constructor.
     */
    public StatisticsServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String loadData(PortalControllerContext portalControllerContext, StatisticsConfiguration configuration) throws PortletException {
        // Documents count by periods
        Map<String, Integer[]> periods = this.repository.getDocumentsCountsByPeriods(portalControllerContext, configuration);

        int index;
        if (StatisticsView.DIFFERENTIAL.equals(configuration.getView())) {
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
        dataset.put("label", "-");
        dataset.put("fillColor", "rgba(151, 187, 205, 0.5)");
        dataset.put("strokeColor", "rgba(151, 187, 205, 0.8");
        dataset.put("highlightFill", "rgba(151, 187, 205, 0.75");
        dataset.put("highlightStroke", "rgba(151, 187, 205, 1");
        dataset.put("data", values);

        // Datasets
        JSONArray datasets = new JSONArray();
        datasets.add(dataset);

        // Chart
        JSONObject chart = new JSONObject();
        chart.put("labels", labels);
        chart.put("datasets", datasets);

        // Table
        Element tbody = DOM4JUtils.generateElement("tbody", null, StringUtils.EMPTY);
        for (Entry<String, Integer[]> period : periods.entrySet()) {
            Element tr = DOM4JUtils.generateElement("tr", null, null);
            tbody.add(tr);
            
            Element label = DOM4JUtils.generateElement("td", null, period.getKey());
            tr.add(label);
            
            for (int i = 0; i < 2; i++) {
                Element value = DOM4JUtils.generateElement("td", null, String.valueOf(period.getValue()[i]));
                tr.add(value);
            }
        }

        // Data
        JSONObject data = new JSONObject();
        data.put("chart", chart);
        data.put("table", DOM4JUtils.write(tbody));

        return data.toString();
    }

}
