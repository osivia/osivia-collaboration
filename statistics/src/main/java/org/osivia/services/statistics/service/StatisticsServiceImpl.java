package org.osivia.services.statistics.service;

import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.statistics.model.StatisticsConfiguration;
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
        Map<String, Integer> periods = this.repository.getDocumentsCountByPeriods(portalControllerContext, configuration);

        // Labels
        JSONArray labels = new JSONArray();
        // Values
        JSONArray values = new JSONArray();

        for (Entry<String, Integer> period : periods.entrySet()) {
            labels.add(period.getKey());
            values.add(period.getValue());
        }

        // Dataset
        JSONObject dataset = new JSONObject();
        dataset.put("label", "Exemple");
        dataset.put("fillColor", "rgba(151, 187, 205, 0.5)");
        dataset.put("strokeColor", "rgba(151, 187, 205, 0.8");
        dataset.put("highlightFill", "rgba(151, 187, 205, 0.75");
        dataset.put("highlightStroke", "rgba(151, 187, 205, 1");
        dataset.put("data", values);

        // Datasets
        JSONArray datasets = new JSONArray();
        datasets.add(dataset);

        // Data
        JSONObject data = new JSONObject();
        data.put("labels", labels);
        data.put("datasets", datasets);

        return data.toString();
    }

}
