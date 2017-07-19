$JQry(function() {
    var $radioButtons = $JQry(".editor-link input[type=radio][name=urlType]");

    $radioButtons.change(function(event) {
        $radioButtons.each(function(index, element) {
            var $element = $JQry(element),
                $input = $element.closest("input"),
                $label = $input.closest("label"),
                $collapse = $label.siblings(".collapse");

            if ($input.is(":checked")) {
                if (!$collapse.hasClass("in")) {
                    $collapse.collapse("show");
                }
            } else {
                if ($collapse.hasClass("in")) {
                    $collapse.collapse("hide");
                }
            }
        });
    });


    $JQry(".editor-link select.select2").each(function(index, element) {
        var $element = $JQry(element),
            url = $element.data("url"),
            ajaxDataFunction = $element.data("ajax-data-function"),
            options = {
                theme : "bootstrap"
            };

        options["ajax"] = {
            url : url,
            dataType : "json",
            delay : 1000,
            data : function(params) {
                var result;

                if (ajaxDataFunction) {
                    result = window[ajaxDataFunction]($element, params);
                } else {
                    result = {
                        filter: params.term,
                        page: params.page
                    };
                }

                return result;
            },
            processResults : function(data, params) {
                params.page = params.page || 1;

                return {
                    results: data.items,
                    pagination: {
                        more: (params.page * data.pageSize) < data.total
                    }
                };
            },
            cache : true
        };


        options["templateResult"] = function(params) {
            var $result = $JQry(document.createElement("div")),
                $vignetteContainer, $vignette, $titleContainer, $icon, $title, $description;

            if (params.loading) {
                $result.text(params.text);
            } else if (params.message) {
                $result.addClass("text-muted");
                $result.text(params.message);
            } else {
                $result.addClass("document clearfix");

                // Vignette container
                $vignetteContainer = $JQry(document.createElement("div"));
                $vignetteContainer.addClass("vignette");
                $vignetteContainer.appendTo($result);

                if (params.vignette) {
                    // Vignette
                    $vignette = $JQry(document.createElement("img"));
                    $vignette.attr("src", params.vignette);
                    $vignette.attr("alt", "");
                    $vignette.appendTo($vignetteContainer);
                }

                // Document title container
                $titleContainer = $JQry(document.createElement("div"));
                $titleContainer.addClass("title");
                $titleContainer.appendTo($result);

                if (params.icon) {
                    // Document icon
                    $icon = $JQry(document.createElement("i"));
                    $icon.addClass(params.icon);
                    $icon.text("");
                    $icon.appendTo($titleContainer);
                }

                // Document title
                $title = $JQry(document.createElement("span"));
                $title.text(params.title);
                $title.appendTo($titleContainer);

                // Document description
                if (params.description) {
                    $description = $JQry(document.createElement("div"));
                    $description.addClass("description small text-muted");
                    $description.text(params.description);
                    $description.appendTo($result);
                }
            }

            return $result;
        };


        options["templateSelection"] = function(params) {
            var $element = $JQry(params.element),
                $selection,
                icon, title,
                $titleContainer, $icon, $title;

            if (params.selected) {
                icon = $element.data("icon");
                title = params.text;
            } else {
                icon = params.icon;
                title = params.title;
            }

            // Selection
            $selection = $JQry(document.createElement("span"));
            $selection.addClass("document");

            // Document title container
            $titleContainer = $JQry(document.createElement("span"));
            $titleContainer.addClass("title");
            $titleContainer.appendTo($selection);

            if (icon) {
                // Document icon
                $icon = $JQry(document.createElement("i"));
                $icon.addClass(icon);
                $icon.text("");
                $icon.appendTo($titleContainer);
            }

            // Document title
            $title = $JQry(document.createElement("span"));
            $title.text(title);
            $title.appendTo($titleContainer);

            return $selection;
        };


        // Internationalization
        options["language"] = {};
        if ($element.data("error-loading") !== undefined) {
            options["language"]["errorLoading"] = function() {
                return $element.data("error-loading");
            }
        }
        if ($element.data("loading-more") !== undefined) {
            options["language"]["loadingMore"] = function() {
                return $element.data("loading-more");
            }
        }
        if ($element.data("searching") !== undefined) {
            options["language"]["searching"] = function() {
                return $element.data("searching");
            }
        }
        if ($element.data("no-results") !== undefined) {
            options["language"]["noResults"] = function() {
                return $element.data("no-results");
            }
        }


        // Force width to 100%
        $element.css({
            width: "100%"
        });


        $element.select2(options);


        if ($element.data("onchange") == "submit") {
            $element.on("select2:unselecting", function(event) {
                var $target = $JQry(event.target);

                $element.data("unselecting", true);
            });

            $element.change(function(event) {
                var $form = $element.closest("form"),
                    $submit = $form.find("button[type=submit][name=save]");

                $submit.click();
            });

            $element.on("select2:opening", function(event) {
                var $target = $JQry(event.target);

                if ($target.data("unselecting")) {
                    event.preventDefault();
                }
            });
        }
    });

});
