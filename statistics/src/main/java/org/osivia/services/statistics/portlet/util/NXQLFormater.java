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
package org.osivia.services.statistics.portlet.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyEntry;
import fr.toutatice.portail.cms.nuxeo.api.VocabularyHelper;

/**
 * NXQL formatter.
 */
public class NXQLFormater {

    /** Frontend date pattern. */
    private static final String FRONTEND_DATE_PATTERN = "dd/MM/yyyy";
    /** Backend date pattern. */
    private static final String BACKEND_DATE_PATTERN = "yyyy-MM-dd";


    /**
     * Default constructor.
     */
    public NXQLFormater() {
        super();
    }


    /**
     * Format text search.
     *
     * @param fieldName field name
     * @param searchValues search values
     * @return formatted text search
     */
    public String formatTextSearch(String fieldName, List<String> searchValues) {
        StringBuffer request = new StringBuffer();
        request.append("(");

        boolean firstItem = true;
        for (String searchWord : searchValues) {
            if (!firstItem) {
                request.append(" OR ");
            }
            request.append(fieldName + " ILIKE \"%" + searchWord + "%\"");
            firstItem = false;
        }

        request.append(")");

        return request.toString();
    }


    /**
     * Format vocabulary search.
     *
     * @param fieldName field name
     * @param selectedVocabsEntries selected vocabulary entries
     * @return formatted vocabulary search
     */
    public String formatVocabularySearch(String fieldName, List<String> selectedVocabsEntries) {
        StringBuffer clause = new StringBuffer();
        clause.append("(");

        boolean firstItem = true;
        for (String selectedVocabsEntry : selectedVocabsEntries) {
            if (!selectedVocabsEntry.contains("othersVocabEntries")) {
                if (!firstItem) {
                    clause.append(" OR ");
                }
                clause.append(fieldName + " STARTSWITH '" + selectedVocabsEntry + "'");
                firstItem = false;
            }
        }

        clause.append(")");
        return clause.toString();
    }


    /**
     * Format other vocabulary entries search.
     *
     * @param portletRequest portlet request
     * @param vocabsNames vocabulary names
     * @param fieldName field name
     * @param selectedVocabsEntries selected vocabulary entries
     * @return formatted other vocabulary entries search
     * @throws Exception
     */
    public String formatOthersVocabularyEntriesSearch(PortletRequest portletRequest, List<?> vocabsNames, String fieldName, List<String> selectedVocabsEntries)
            throws Exception {
        StringBuffer clause = new StringBuffer();

        int nbOtherEntries = 0;
        for (String selectedEntry : selectedVocabsEntries) {
            if (selectedEntry.contains("othersVocabEntries")) {
                if (nbOtherEntries > 0) {
                    clause.append(" OR ");
                }
                nbOtherEntries++;
                StringBuffer clauseBeforeOther = new StringBuffer();
                StringBuffer otherClause = new StringBuffer();

                String selectedValuesBeforeOthers = StringUtils.substringBeforeLast(selectedEntry, "/");
                if ("othersVocabEntries".equalsIgnoreCase(selectedValuesBeforeOthers)) {
                    selectedValuesBeforeOthers = "";
                } else {
                    selectedValuesBeforeOthers += "/";
                }

                if (StringUtils.isNotEmpty(selectedValuesBeforeOthers)) {
                    clauseBeforeOther.append(" ( ");
                    clauseBeforeOther.append(fieldName);
                    clauseBeforeOther.append(" STARTSWITH '");
                    clauseBeforeOther.append(selectedValuesBeforeOthers);
                    clauseBeforeOther.append("' ");
                    clauseBeforeOther.append(") AND ");
                }


                // Récupération du niveau du dernier vocabulaire affiché
                int selectedLevel = selectedEntry.split("/").length;

                // Récupération de l'arbre des vocabulaires
                List<String> vocabNames = new ArrayList<String>();
                for (Object vocab : vocabsNames) {
                    vocabNames.add(vocab.toString());
                }


                NuxeoController nuxeoController = (NuxeoController) portletRequest.getAttribute("nuxeoController");
                VocabularyEntry vocabEntry = VocabularyHelper.getVocabularyEntry(nuxeoController, vocabNames);


                // Récupération du dernier vocabulaire sélectionné avant l'entrée "Autres"
                int levelIndex = 0;
                VocabularyEntry lastVocab = vocabEntry;

                String[] entries = selectedEntry.split("/");
                while (levelIndex < selectedLevel) {
                    String entry = entries[levelIndex];
                    if (!"othersVocabEntries".equals(entry)) {
                        lastVocab = lastVocab.getChild(entry);
                    }
                    levelIndex++;
                }

                Collection<VocabularyEntry> vocabsEntries = lastVocab.getChildren().values();
                if ((vocabsEntries != null) && (vocabsEntries.size() > 0)) {
                    otherClause.append(" ( NOT (");

                    boolean firstItem = true;
                    for (VocabularyEntry displayedEntry : vocabsEntries) {
                        String entry = displayedEntry.getId();

                        if (!firstItem) {
                            otherClause.append(" OR ");
                        }

                        otherClause.append(fieldName);
                        otherClause.append(" STARTSWITH '");
                        otherClause.append(selectedValuesBeforeOthers);
                        otherClause.append(entry);
                        otherClause.append("' ");

                        firstItem = false;
                    }

                    otherClause.append(" ) ) ");
                }

                clause.append(clauseBeforeOther.toString());
                clause.append(otherClause.toString());
            }
        }

        StringBuffer otherClause = new StringBuffer();
        if (nbOtherEntries > 0) {
            otherClause.append("(");

            // Les documents Nuxeo dont le champ fieldName n'est pas renseigné ou existant ne doivent pas être retournés
            // otherClause.append("(");
            // otherClause.append(fieldName);
            // otherClause.append(" LIKE '%%') AND (");

            otherClause.append(clause.toString());
            otherClause.append(")");
        }

        String resultClause = this.formatVocabularySearch(fieldName, selectedVocabsEntries);
        if (resultClause.equals("()")) {
            resultClause = "";
        }

        if (nbOtherEntries > 0) {
            if (resultClause.length() > 0) {
                resultClause = "(" + resultClause + " OR " + otherClause.toString() + ")";
            } else {
                resultClause = otherClause.toString();
            }
        }

        return resultClause;
    }


    /**
     * Format date search.
     *
     * @param fieldName field name
     * @param searchValue search value
     * @return formatted date search
     */
    public String formatDateSearch(String fieldName, List<String> searchValue) {
        StringBuffer request = new StringBuffer();

        if ((searchValue != null) && (searchValue.size() > 0)) {
            request.append("(");

            int index = 0;
            for (String datesInterval : searchValue) {
                String[] interval = datesInterval.split("%");
                try {
                    // Dates
                    String[] frontPattern = new String[]{FRONTEND_DATE_PATTERN};
                    Date beginDate = DateUtils.parseDate(interval[0], frontPattern);
                    Date endDate = DateUtils.parseDate(interval[1], frontPattern);
                    // Add one day to end date
                    endDate = DateUtils.addDays(endDate, 1);

                    // Backend dates format
                    String from = DateFormatUtils.format(beginDate, BACKEND_DATE_PATTERN);
                    String to = DateFormatUtils.format(endDate, BACKEND_DATE_PATTERN);

                    // Request generation
                    if (index > 0) {
                        request.append(" OR ");
                    }
                    request.append("(");
                    request.append(fieldName);
                    request.append(" BETWEEN DATE '");
                    request.append(from);
                    request.append("' AND DATE '");
                    request.append(to);
                    request.append("')");
                } catch (ParseException e) {
                    continue;
                }
                index++;
            }
            request.append(")");
        }

        return request.toString();
    }


    /**
     * Format advanced search.
     *
     * @param searchValues search values
     * @return formatted advanced search
     */
    public String formatAdvancedSearch(List<String> searchValues) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("ecm:fulltext = '");
        buffer.append(StringUtils.join(searchValues, " "));
        buffer.append(" -noindex'");
        NuxeoQueryFilterContext queryFilter = new NuxeoQueryFilterContext( );

        return NuxeoQueryFilter.addPublicationFilter(queryFilter, buffer.toString());
    }

}
