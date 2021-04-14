$JQry(function() {
    var $container = $JQry(".editor-link");
    var timer;


    if (!$container.data("loaded")) {
        search($container);


        $container.find("input[name=filter]").keyup(function(event) {
            // Clear timer
            clearTimeout(timer);

            timer = setTimeout(function () {
                search($container);
            }, 500);
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
