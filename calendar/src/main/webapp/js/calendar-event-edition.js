$JQry(function () {
    $JQry(".calendar.calendar-event-edition textarea[name=description]")
        .each(function (index, element) {
            if (typeof tinymce != "undefined") {
                var $textarea = $JQry(element),
                    id = $textarea.attr("id");

                if (tinymce.get(id)) {
                    if (!$textarea.data("loaded")) {
                        tinymce.execCommand("mceRemoveEditor", true, id);
                        tinymce.execCommand("mceAddEditor", true, id);
                    }
                } else {
                    tinymce.init({
                        selector: ".calendar.calendar-event-edition textarea[name=description]",
                        language: "fr_FR",
                        plugins: "autosave link lists noneditable paste",
                        external_plugins: {
                            "osivia_link": "/osivia-services-editor-helpers/js/link/plugin.min.js"
                        },

                        branding: false,
                        menubar: false,
                        toolbar: "undo redo | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | bullist numlist | osivia_link",
                        statusbar: false,

                        element_format: "html",
                        formats: {
                            alignleft: {
                                selector: "p, ul, ol, li",
                                classes: "text-left"
                            },
                            aligncenter: {
                                selector: "p, ul, ol, li",
                                classes: "text-center"
                            },
                            alignright: {
                                selector: "p, ul, ol, li",
                                classes: "text-right"
                            },
                            alignjustify: {
                                selector: "p, ul, ol, li",
                                classes: "text-justify"
                            },
                            bold: {
                                inline: "strong"
                            },
                            italic: {
                                inline: "em"
                            },
                            underline: {
                                inline: "u"
                            },
                            strikethrough: {
                                inline: "s"
                            }
                        },

                        content_css: ["/osivia-portal-custom-web-assets/css/bootstrap.min.css"],
                        height: 200,

                        // Prevent relative URL conversion
                        convert_urls: false,
                        // Remove style on paste
                        paste_as_text: true,

                        browser_spellcheck: true
                    });
                }

                $textarea.data("loaded", true);
            }
        });

    $JQry(".calendar.calendar-event-edition").each(function (index, element) {
        var $element = $JQry(element);

        if (!$element.data("loaded-event-edition")) {
            // Initialize input widgets first
            $element.find(".calendar-event-dates .time").timepicker({
                "showDuration": true,
                "timeFormat": "H:i"
            });
            $element.find(".calendar-event-dates .date").datepicker({
                "format": "dd/mm/yy",
                "autoclose": true
            });

            // Initialize datepair
            $element.find(".calendar-event-dates").datepair({
                parseDate: function (input) {
                    return $JQry(input).datepicker("getDate");
                },
                updateDate: function (input, dateObj) {
                    return $JQry(input).datepicker("setDate", dateObj);
                }
            });


            $element.find("input[type=checkbox][name=allDay]").change(function (event) {
                var $target = $JQry(event.target),
                    $formGroup = $target.closest(".form-group"),
                    $dates = $formGroup.find(".calendar-event-dates"),
                    $timeContainer = $dates.find(".time").parent(),
                    checked = $target.is(":checked");

                if (checked) {
                    $timeContainer.addClass("hidden");
                } else {
                    $timeContainer.removeClass("hidden");
                }
            });


            $element.data("loaded-event-edition", true);
        }
    });
});
