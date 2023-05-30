$JQry(function() {
    var $container = $JQry(".editor-image");
    var timer;

    if (!$container.data("loaded")) {
        var $input = $container.find("input[name=filter]");

        search($container);

        $input.on("keyup", function(event) {
            // Clear timer
            clearTimeout(timer);

            if (event.key === "Enter") {
                event.preventDefault();

                search($container);
            } else {
                timer = setTimeout(function () {
                    search($container);
                }, 500);
            }
        });

        $input.on("change", function() {
            // Clear timer
            clearTimeout(timer);

            search($container);
        });

        $container.find("input:radio[name=scope]").change(function(event) {
            // Clear timer
            clearTimeout(timer);

            search($container);
        });


        $container.data("loaded", true);
    }


    function search($container) {
        var $placeholder = $container.find("[data-search-url]");

        jQuery.ajax({
            async: true,
            cache: false,
            dataType: "html",
            global: false,
            url: $placeholder.data("search-url"),
            data: {
                filter: $container.find("input[name=filter]").val(),
                scope: $container.find("input:radio[name=scope]:checked").val()
            },

            success: function (data, status, xhr) {
                $placeholder.html(data);
            }
        });
    }

});
